package com.learning.javaMailAPI;

import java.io.File;
import java.util.Properties;

import jakarta.mail.Address;
import jakarta.mail.Authenticator;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.NoSuchProviderException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class App {
	public static void main(String[] args) {
		System.out.println("Preparing to send message");
		String message = "Hello Dear user this is message for security check";
		String subject = "codersArea : Confirmation";
		String to = "helloJava@gmail.com";
		String from = "dhanjeetthakur@gmail.com";
		sendEmailWithText(message, subject, to, from);
		sendEmailWithAttach(message, subject, to, from);
		readEmails(from);
		readEmailsFromSender(from, from);
	}

	
	private static void sendEmailWithText(String message, String subject, String to, final String from) {
		// 1. Get the System Properties
		Properties properties = System.getProperties();

		// Setting the properties value
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true"); // Enable the SSL(Secure Sockets Layers)
		properties.put("mail.smtp.auth", "true");// Enabling the Authentication

		Session session = Session.getInstance(properties, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, "xxxxxxxxxxxxxxxx");
				//here instead of "xxxxxxxxxxxxxxxx" --> Paste the 16-character generated password
			}
		});
		session.setDebug(true);

		MimeMessage msg = new MimeMessage(session);
		try {
			msg.setFrom(from);// Set the "From address" of mail
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			msg.setSubject(subject);
			msg.setText(message);
			
			//Send the message
			Transport.send(msg);
			System.out.println("Sent Success.................");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	
	private static void sendEmailWithAttach(String message, String subject, String to, final String from) {
		// 1. Get the System Properties
		Properties properties = System.getProperties();

		// Setting the properties value
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true"); // Enable the SSL(Secure Sockets Layers)
		properties.put("mail.smtp.auth", "true");// Enabling the Authentication

		Session session = Session.getInstance(properties, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, "xxxxxxxxxxxxxxxx");
			}
		});
		session.setDebug(true);

		MimeMessage m = new MimeMessage(session);
		try {
			m.setFrom(from);// Set the "From address" of mail
			m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			m.setSubject(subject);

			// attachment
			MimeMultipart mimeMultipart = new MimeMultipart();

			MimeBodyPart textMime = new MimeBodyPart();// this is to hold txt msg

			MimeBodyPart fileMime = new MimeBodyPart();// this is to hold file for email
			try {
				textMime.setText(message);

				File f = new File("C:\\Users\\Desktop\\Profile.JPG");
				fileMime.attachFile(f);
				mimeMultipart.addBodyPart(fileMime);
				mimeMultipart.addBodyPart(textMime);
			} catch (Exception e) {
				e.printStackTrace();
			}

			m.setContent(mimeMultipart);

			Transport.send(m);
			System.out.println("Sent Success.................");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	private static void readEmails(String from) {
		try {
			// create a properties filed
			Properties properties = System.getProperties();
			properties.put("mail.pop3.host", "pop.gmail.com");
			properties.put("mail.pop3.port", "995");
			properties.put("mail.pop3.starttls.enable", "true");

			Session emailSession = Session.getDefaultInstance(properties);

			//// create the POP3 store object and connect with the pop server
			Store store = emailSession.getStore("pop3s");

			// store.connect(host, user, password);
			store.connect("pop.gmail.com",from, "xxxxxxxxxxxxxxxx");

			// create the folder object and open it
			Folder emailFolder = store.getFolder("INBOX");
			System.out.println("*******************"+emailFolder.getName());
			emailFolder.open(Folder.READ_ONLY);

			// retrieve the messages from the folder in an array and print it
			Message[] messages = emailFolder.getMessages();
			System.out.println("messages.length---" + messages.length);

			for (int i = 0; i < 4; i++) {
				Message message = messages[i];
				System.out.println("---------------------------------");
				System.out.println("Email Number " + (i + 1));
				System.out.println("Subject: " + message.getSubject());
				System.out.println("From: " + message.getFrom()[0]);
				System.out.println("Text: " + message.getContent().toString());

			}

			// close the store and folder objects
			emailFolder.close(false);
			store.close();

		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	
	
	private static void readEmailsFromSender(String from, String senderEmail) {
	    try {
	        // create properties
	        Properties properties = System.getProperties();
	        properties.put("mail.pop3.host", "pop.gmail.com");
	        properties.put("mail.pop3.port", "995");
	        properties.put("mail.pop3.starttls.enable", "true");

	        Session emailSession = Session.getDefaultInstance(properties);

	        // create the POP3 store object and connect with the pop server
	        Store store = emailSession.getStore("pop3s");

	        store.connect("pop.gmail.com", from, "xxxxxxxxxxxxxxxx");

	        // create the folder object and open it
	        Folder emailFolder = store.getFolder("INBOX");
	        emailFolder.open(Folder.READ_ONLY);

	        // retrieve all messages
	        Message[] messages = emailFolder.getMessages();

	        // Process messages from a specific sender
	        for (int i = 0; i < messages.length; i++) {
	            Message message = messages[i];
	            Address[] fromAddresses = message.getFrom();
	            if (fromAddresses.length > 0) {
	                String senderAddress = fromAddresses[0].toString();
	                if (senderAddress.contains(senderEmail)) {
	                    System.out.println("---------------------------------");
	                    System.out.println("Email Number " + (i + 1));
	                    System.out.println("Subject: " + message.getSubject());
	                    System.out.println("From: " + senderAddress);
	                    System.out.println("Text: " + message.getContent().toString());
	                }
	            }
	        }

	        // close the store and folder objects
	        emailFolder.close(false);
	        store.close();

	    } catch (NoSuchProviderException e) {
	        e.printStackTrace();
	    } catch (MessagingException e) {
	        e.printStackTrace();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	

}
