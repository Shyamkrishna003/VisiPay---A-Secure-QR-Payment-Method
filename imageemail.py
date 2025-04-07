# Python code to illustrate Sending mail with attachments
# from your Gmail account 
  
# libraries to be imported
import smtplib
import os
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText
from email.mime.base import MIMEBase
from email import encoders
def filesssss(path,email):
	print(email)
	fromaddr = "hariharan0987pp@gmail.com"
	toaddr = email
	   
	# instance of MIMEMultipart
	msg = MIMEMultipart()
	  
	# storing the senders email address  
	msg['From'] = fromaddr
	  
	# storing the receivers email address 
	msg['To'] = toaddr
	  
	# storing the subject 
	msg['Subject'] = "Details of fileupload"
	  
	# string to store the body of the mail
	body = "QR of Half image"

	  
	# attach the body with the msg instance
	msg.attach(MIMEText(body, 'plain'))
	  
	# open the file to be sent 
	filename = "file.jpg"
	if not os.path.exists(path):
		open(path, 'w').close()  # Create the file if it doesn't exist
	attachment = open(path, "rb")
	  
	# instance of MIMEBase and named as p
	p = MIMEBase('application', 'octet-stream')
	  
	# To change the payload into encoded form
	p.set_payload((attachment).read())
	  
	# encode into base64
	encoders.encode_base64(p)
	   
	p.add_header('Content-Disposition', "attachment; filename= %s" % filename)
	  
	# attach the instance 'p' to instance 'msg'
	msg.attach(p)
	  
	# creates SMTP session
	s = smtplib.SMTP('smtp.gmail.com', 587)
	  
	# start TLS for security
	s.starttls()
	  
	# Authentication
	s.login(fromaddr, "rjcbcumvkpqynpep")
	  
	# Converts the Multipart msg into a string
	text = msg.as_string()
	  
	# sending the mail
	s.sendmail(fromaddr, toaddr, text)
	  
	# terminating the session
	s.quit()