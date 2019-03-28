package Utility;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import logsAndExceptionWriter.WriteLogsAndExceptions;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
 
public class SendEmailTLS 
{
   public static void sendEmail(String content) throws Exception 
   {
	   try
	   {
		   ReadObject objects = new ReadObject();
		   Properties allObjects =  objects.getObjectRepository();
		   //Mention user name and password as per your configuration
		   final String SMTPUsername = allObjects.getProperty("SMTPUsername");
		   final String SMTPPassword = allObjects.getProperty("SMTPPassword");
		   String FromAddress = allObjects.getProperty("FromAddress");
		   String ToAddress = allObjects.getProperty("ToAddress");
		   String CcAddress = allObjects.getProperty("CcAddress");
		   String Subject = allObjects.getProperty("Subject");
		   String SMTPPort = allObjects.getProperty("SMTPPort");
		   String SMTPHost = allObjects.getProperty("SMTPHost");
		   String Starttls = allObjects.getProperty("Starttls");
		  
		   java.util.Properties props = null;
           props = System.getProperties();
           props.put("mail.smtp.user", SMTPUsername);
           props.put("mail.smtp.host", SMTPHost);
           props.put("mail.smtp.auth", "true");
           props.put("mail.smtp.debug", "true");

           if(!"".equals(SMTPPort))
           {
               props.put("mail.smtp.port", SMTPPort);
               props.put("mail.smtp.socketFactory.port", SMTPPort);
           }

           if(!"".equals(Starttls))
               props.put("mail.smtp.starttls.enable",Starttls);

           Session session = Session.getInstance(props, new javax.mail.Authenticator() {
               protected PasswordAuthentication getPasswordAuthentication() {
                   return new PasswordAuthentication(SMTPUsername, SMTPPassword);
               }
           });
           session.setDebug(true);

           MimeMessage msg = new MimeMessage(session);
           msg.setFrom(new InternetAddress(FromAddress));
           msg.setSubject(Subject);                
           msg.setText(content, "ISO-8859-1");
           msg.setSentDate(new Date());
           msg.setHeader("content-Type", "text/html;charset=\"ISO-8859-1\"");
           InternetAddress[] to = InternetAddress.parse(ToAddress , true);
           msg.setRecipients(Message.RecipientType.TO,to);
           InternetAddress[] cc = InternetAddress.parse(CcAddress , true);
           msg.setRecipients(Message.RecipientType.CC,cc);
           //msg.addRecipient(Message.RecipientType.TO, new InternetAddress(ToAddress));
           msg.saveChanges();

           Transport transport = session.getTransport("smtp");
           transport.connect(SMTPHost, SMTPUsername, SMTPPassword);
           transport.sendMessage(msg, msg.getAllRecipients());
           transport.close();
           System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()) +"Your email sent successfully");
	     
	   }
	   catch(Exception e)
	   {
		   WriteLogsAndExceptions.appendToFile(e);
	   }
   }
 
}