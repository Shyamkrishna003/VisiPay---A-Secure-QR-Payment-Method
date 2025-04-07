package com.example.bank_transaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.RGBLuminanceSource;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.EnumMap;
import java.util.Map;

public class user_local_image extends AppCompatActivity implements JsonResponse {
    public static final String TAG = "UserLocalImage";
    public ImageButton imageButton1, imageButton2, imageButton3;
    public SharedPreferences sh;
    public String img1, img2;
    public Bitmap bitmap1, bitmap2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); // Hide the title
        getSupportActionBar().hide(); // Hide the title bar
        setContentView(R.layout.activity_user_local_image);

        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        imageButton1 = findViewById(R.id.imageButton1);
        imageButton2 = findViewById(R.id.imageButton2);
        imageButton3 = findViewById(R.id.imageButton3);

        JsonReq JR = new JsonReq();
        JR.json_response = (JsonResponse) user_local_image.this;
        String q = "/viewimages?log_id=" + sh.getString("log_ids", "");
        q = q.replace(" ", "%20");
        JR.execute(q);
    }

    @Override
    public void response(JSONObject jo) {
        try {
            String status = jo.getString("stat");

            if (status.equalsIgnoreCase("success")) {
                JSONArray ja1 = jo.getJSONArray("data");
                img1 = ja1.getJSONObject(0).getString("img1");
                img2 = ja1.getJSONObject(0).getString("img2");

                String pth = "http://" + sh.getString("ip", "") + "/" + img1;
                pth = pth.replace("~", "");
                Log.d("ImagePath1", pth);

                String pth1 = "http://" + sh.getString("ip", "") + "/" + img2;
                pth1 = pth1.replace("~", "");
                Log.d("ImagePath2", pth1);

                // Load images using Picasso
                loadImage(pth, imageButton1, true);
                loadImage(pth1, imageButton2, false);
            } else {
                Toast.makeText(this, "Upload failed", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void loadImage(String imageUrl, ImageButton imageButton, boolean isFirstImage) {
        Picasso.with(getApplicationContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(imageButton, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        if (imageButton.getDrawable() == null) return;

                        // Convert the loaded image to Bitmap
                        Bitmap bitmap = ((BitmapDrawable) imageButton.getDrawable()).getBitmap();

                        if (isFirstImage) {
                            bitmap1 = bitmap;
                        } else {
                            bitmap2 = bitmap;
                        }

                        // Decrypt and scan QR code if both images are loaded
                        if (bitmap1 != null && bitmap2 != null) {
                            Bitmap decryptedBitmap = VisualCryptography.decryptShares(bitmap1, bitmap2);

                            // Invert the decrypted image colors to fix the black-and-white inversion
                            Bitmap invertedBitmap = invertColors(decryptedBitmap);

                            // Set the inverted bitmap to the ImageButton
                            imageButton3.setImageBitmap(invertedBitmap);

                            if (invertedBitmap != null) {
                                // Preprocess the inverted image (convert to black-and-white if necessary)
                                Bitmap processedBitmap = preprocessImage(invertedBitmap);

                                // Scan QR code asynchronously
                                new AsyncTask<Bitmap, Void, String>() {
                                    @Override
                                    protected String doInBackground(Bitmap... bitmaps) {
                                        return scanQRCode(bitmaps[0]);
                                    }

                                    @Override
                                    protected void onPostExecute(String result) {
                                        if (result != null) {
                                            Toast.makeText(user_local_image.this, "QR Code: " + result, Toast.LENGTH_LONG).show();

                                            String loginId = null, amount = null;
                                            // Assume the QR content format: login_id=USER123&Amount=1500
                                            String[] params = result.split("&");
                                            for (String param : params) {
                                                if (param.startsWith("customer_id=")) {
                                                    loginId = param.split("=")[1];
                                                } else if (param.startsWith("Amount=")) {
                                                    amount = param.split("=")[1];
                                                }
                                            }

                                            if (loginId != null && amount != null) {
                                                // Redirect to the next activity and pass the data
                                                Intent nextPage = new Intent(getApplicationContext(), user_qr_pay.class);
                                                nextPage.putExtra("login_id", loginId);
                                                nextPage.putExtra("amount", amount);
                                                startActivity(nextPage);
                                            } else {
//                                                Toast.makeText(this, "Invalid QR Code Data", Toast.LENGTH_LONG).show();
                                            }
//                                            Toast.makeText(user_local_image.this, "QR Code: " + result, Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(user_local_image.this, "No QR Code found", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }.execute(processedBitmap);
                            }
                        }
                    }

                    @Override
                    public void onError() {
                        Log.e("ImageError", "Failed to load image: " + imageUrl);
                    }
                });
    }

    // Method to invert the colors of the decrypted image
    private Bitmap invertColors(Bitmap originalBitmap) {
        Bitmap invertedBitmap = Bitmap.createBitmap(originalBitmap.getWidth(), originalBitmap.getHeight(), Bitmap.Config.ARGB_8888);

        for (int x = 0; x < originalBitmap.getWidth(); x++) {
            for (int y = 0; y < originalBitmap.getHeight(); y++) {
                int pixelColor = originalBitmap.getPixel(x, y);

                // Get the current pixel color components
                int red = Color.red(pixelColor);
                int green = Color.green(pixelColor);
                int blue = Color.blue(pixelColor);

                // Invert the colors (white becomes black, black becomes white)
                int invertedColor = Color.rgb(255 - red, 255 - green, 255 - blue);

                invertedBitmap.setPixel(x, y, invertedColor);
            }
        }

        return invertedBitmap;
    }

    // Method to preprocess the image (convert it to black-and-white)
    private Bitmap preprocessImage(Bitmap originalBitmap) {
        Bitmap processedBitmap = Bitmap.createBitmap(originalBitmap.getWidth(), originalBitmap.getHeight(), Bitmap.Config.ARGB_8888);

        for (int x = 0; x < originalBitmap.getWidth(); x++) {
            for (int y = 0; y < originalBitmap.getHeight(); y++) {
                int pixelColor = originalBitmap.getPixel(x, y);
                int red = (pixelColor >> 16) & 0xFF;
                int green = (pixelColor >> 8) & 0xFF;
                int blue = pixelColor & 0xFF;

                // Convert to grayscale using the luminance method
                int gray = (int) (0.3 * red + 0.59 * green + 0.11 * blue); // Standard luminance formula

                // Apply a threshold to get a binary image
                int newColor = (gray > 128) ? Color.WHITE : Color.BLACK;
                processedBitmap.setPixel(x, y, newColor);
            }
        }

        return processedBitmap;
    }

    // Method to scan QR code from a Bitmap
    private String scanQRCode(Bitmap bitmap) {
        try {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int[] pixels = new int[width * height];
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

            RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));

            MultiFormatReader reader = new MultiFormatReader();
            Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
            hints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
            reader.setHints(hints);

            Result result = reader.decodeWithState(binaryBitmap);
            if (result != null) {
                return result.getText();
            } else {
                Log.e(TAG, "QR Code not found.");
                return null;
            }
        } catch (NotFoundException e) {
            Log.e(TAG, "QR Code not found", e);
            return null;
        } catch (Exception e) {
            Log.e(TAG, "QR Code scanning failed", e);
            return null;
        }
    }
}
