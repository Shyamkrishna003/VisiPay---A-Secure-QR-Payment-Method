package com.example.bank_transaction;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class user_upload_image extends AppCompatActivity implements JsonResponse {

    private static final int CAMERA_PIC_REQUEST = 0;
    private static final int GALLERY_CODE = 201;
    private static final String TAG = "UserUploadImage";

    private ImageButton imageButton, imageButton1, imageButton2, imageButton3;
    private Button uploadButton;
    private Uri mImageCaptureUri;
    private byte[] byteArray = null;
    private SharedPreferences sh;
    private String img1, img2;
    private Bitmap bitmap1, bitmap2;
    public static String encodedImage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_upload_image);
        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        imageButton = findViewById(R.id.imageButton);
        imageButton1 = findViewById(R.id.imageButton1);
        imageButton2 = findViewById(R.id.imageButton2);
        imageButton3 = findViewById(R.id.imageButton3);
        uploadButton = findViewById(R.id.uploadButton);
    }

    private void setupListeners() {
        imageButton.setOnClickListener(v -> selectImageOption());
        uploadButton.setOnClickListener(v -> handleImageUpload());
    }

    private void selectImageOption() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};
        new AlertDialog.Builder(this)
                .setTitle("Select Image")
                .setItems(items, (dialog, item) -> {
                    if (items[item].equals("Take Photo")) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, CAMERA_PIC_REQUEST);
                    } else if (items[item].equals("Choose from Gallery")) {
                        Intent intent = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, GALLERY_CODE);
                    } else {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void handleImageUpload() {
        if (encodedImage.isEmpty()) {
            Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show();
            return;
        }

        String log_id = sh.getString("log_ids", "");
        String uploadUrl = "http://" + IpSettings.text + "/api/upload_image";

        Map<String, byte[]> data = new HashMap<>();
        data.put("image", byteArray);
        data.put("log_id", log_id.getBytes());

        FileUploadAsync fileUpload = new FileUploadAsync(uploadUrl);
        fileUpload.json_response = this;
        fileUpload.execute(data);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK || data == null) {
            return;
        }

        try {
            Bitmap bitmap;
            if (requestCode == GALLERY_CODE) {
                mImageCaptureUri = data.getData();
                try (InputStream inputStream = getContentResolver().openInputStream(mImageCaptureUri)) {
                    bitmap = BitmapFactory.decodeStream(inputStream);
                }
            } else if (requestCode == CAMERA_PIC_REQUEST) {
                bitmap = (Bitmap) data.getExtras().get("data");
            } else {
                return;
            }

            if (bitmap == null) {
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                return;
            }

            processBitmap(bitmap);

        } catch (Exception e) {
            Log.e(TAG, "Error processing image", e);
            Toast.makeText(this, "Image processing failed: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void processBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byteArray = baos.toByteArray();
        encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        imageButton.setImageBitmap(bitmap);
    }

    @Override
    public void response(JSONObject jo) {
        try {
            String status = jo.getString("stat");
            if (!status.equalsIgnoreCase("success")) {
                Toast.makeText(this, "Upload failed", Toast.LENGTH_SHORT).show();
                return;
            }

            JSONArray ja1 = jo.getJSONArray("data");
            img1 = ja1.getJSONObject(0).getString("img1");
            img2 = ja1.getJSONObject(0).getString("img2");

            String ip = sh.getString("ip", "");
            String path1 = "http://" + ip + "/" + img1.replace("~", "");
            String path2 = "http://" + ip + "/" + img2.replace("~", "");

            Log.d(TAG, "Loading images from: " + path1 + " and " + path2);

            loadImage(path1, imageButton1, true);
            loadImage(path2, imageButton2, false);

        } catch (Exception e) {
            Log.e(TAG, "Error processing response", e);
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void loadImage(String imageUrl, ImageButton imageButton, boolean isFirstImage) {
        Picasso.with(getApplicationContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .config(Bitmap.Config.ARGB_8888)
                .into(imageButton, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        if (imageButton.getDrawable() == null) {
                            Log.e(TAG, "Image loaded but drawable is null");
                            return;
                        }

                        Bitmap bitmap = ((BitmapDrawable) imageButton.getDrawable()).getBitmap();
                        if (bitmap == null) {
                            Log.e(TAG, "Failed to convert drawable to bitmap");
                            return;
                        }

                        if (isFirstImage) {
                            bitmap1 = bitmap;
                        } else {
                            bitmap2 = bitmap;
                        }

                        processDecryptedImage();
                    }

                    @Override
                    public void onError() {
                        Log.e(TAG, "Failed to load image: " + imageUrl);
                        Toast.makeText(user_upload_image.this,
                                "Failed to load image", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void processDecryptedImage() {
        if (bitmap1 == null || bitmap2 == null) {
            return;
        }

        try {
            Bitmap decryptedBitmap = VisualCryptography.decryptShares(bitmap1, bitmap2);
            if (decryptedBitmap == null) {
                Log.e(TAG, "Decrypted bitmap is null");
                Toast.makeText(this, "Failed to decrypt image shares", Toast.LENGTH_SHORT).show();
                return;
            }

            // Invert the colors of the decrypted image
            Bitmap invertedBitmap = invertColors(decryptedBitmap);

            // Convert the inverted bitmap to black-and-white
            Bitmap processedBitmap = preprocessImage(invertedBitmap);

            imageButton3.setImageBitmap(processedBitmap);

            String qrCodeText = scanQRCode(processedBitmap);

            if (qrCodeText != null) {
                Toast.makeText(this, "QR Code: " + qrCodeText, Toast.LENGTH_LONG).show();
            } else {
                Log.w(TAG, "No QR code found in decrypted image");
                Toast.makeText(this, "No QR Code found in image", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error processing decrypted image", e);
            Toast.makeText(this, "Error processing image: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
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
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        Bitmap processedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        // Calculate average brightness
        long totalBrightness = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = originalBitmap.getPixel(x, y);
                int red = Color.red(pixel);
                int green = Color.green(pixel);
                int blue = Color.blue(pixel);
                totalBrightness += (red + green + blue) / 3;
            }
        }
        int averageBrightness = (int) (totalBrightness / (width * height));

        // Adjust threshold based on average brightness
        int threshold = averageBrightness < 128 ? 110 : 140;

        // Apply adaptive thresholding
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = originalBitmap.getPixel(x, y);
                int red = Color.red(pixel);
                int green = Color.green(pixel);
                int blue = Color.blue(pixel);
                
                // Enhanced grayscale conversion
                int gray = (int) (0.299 * red + 0.587 * green + 0.114 * blue);
                
                // Apply local contrast enhancement
                int newColor = (gray > threshold) ? Color.WHITE : Color.BLACK;
                processedBitmap.setPixel(x, y, newColor);
            }
        }

        return processedBitmap;
    }

    private String scanQRCode(Bitmap bitmap) {
        try {
            // Try multiple sizes for better detection
            int[] targetWidths = {800, 1000, 600};
            
            for (int targetWidth : targetWidths) {
                float scaleFactor = (float) targetWidth / bitmap.getWidth();
                int targetHeight = (int) (bitmap.getHeight() * scaleFactor);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true);

                // Try both normal and inverted images
                String result = tryQRScan(scaledBitmap);
                if (result != null) {
                    return result;
                }

                // Try with inverted colors
                result = tryQRScan(invertColors(scaledBitmap));
                if (result != null) {
                    return result;
                }
            }
            
            return null;
        } catch (Exception e) {
            Log.e(TAG, "QR Code scanning failed", e);
            return null;
        }
    }

    private String tryQRScan(Bitmap bitmap) {
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
            hints.put(DecodeHintType.POSSIBLE_FORMATS, Arrays.asList(BarcodeFormat.QR_CODE));
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            reader.setHints(hints);

            Result result = reader.decode(binaryBitmap);
            return result.getText();
        } catch (NotFoundException e) {
            return null;
        }
    }
}
