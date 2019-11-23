package com.talentos.util;

import java.io.IOException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import com.sun.mail.smtp.SMTPMessage;

public class EnviarCorreo {

	/**
	   * Build an HTML message with an image embedded in the message.
	   * 
	   * @param session
	   * @return a multipart MIME message where the main part is an HTML message and the 
	   * second part is an image that will be displayed within the HTML.
	   * @throws MessagingException
	   * @throws IOException
	*/
	public static Message buildMessageWithEmbeddedImage(Session session)
	      throws MessagingException, IOException {
	    SMTPMessage m = new SMTPMessage(session);
	    MimeMultipart content = new MimeMultipart("related");
	    
	    // ContentID is used by both parts
	    String cid = ContentIdGenerator.getContentId();
	    
	    // HTML part
	    MimeBodyPart textPart = new MimeBodyPart();
	    textPart.setText("<html><head>"
	      + "<title>This is not usually displayed</title>"
	      + "</head>\n"
	      + "<body><div><b>Hi there!</b></div>"
	      + "<div>Sending HTML in email is so <i>cool!</i> </div>\n"
	      + "<div>And here's an image: <img src=\"cid:"
	      + cid
	      + "\" /></div>\n" + "<div>I hope you like it!</div></body></html>", 
	      "US-ASCII", "html");
	    content.addBodyPart(textPart);

	    // Image part
	    MimeBodyPart imagePart = new MimeBodyPart();
	    imagePart.attachFile("resources/teapot.jpg");
	    imagePart.setContentID("<" + cid + ">");
	    imagePart.setDisposition(MimeBodyPart.INLINE);
	    content.addBodyPart(imagePart);
	    
	    m.setContent(content);
	    m.setSubject("Demo HTML message");
	    return m;
	  }

}
