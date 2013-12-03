package envelope;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.pop3.DefaultFolder;
import java.io.IOException;
import javax.activation.*;
import javax.swing.*;

class Email {
	private final StatusPanel statusPanel;
	private Store store;
	private Folder inbox;
	private Message[] messages;
	private final String emailAddress;
	private final String password;
	private String POP3Address;
	private String IMAPAddress;
	private final String SMTPAddress;
	private final String SMTPPort;
	private final ProtocolType protocol;

	// Constructor
	public Email(StatusPanel statusPanel, String emailAddress, String password) throws UnrecognisedEmailServiceException
	{
		this.statusPanel = statusPanel;
		this.emailAddress = emailAddress;
		this.password = password;
		if(this.getEmailAddress().contains("@gmail.com"))
		{
			this.protocol = ProtocolType.IMAP;
			this.IMAPAddress = "imap.gmail.com";
			this.SMTPAddress = "smtp.gmail.com";
			this.SMTPPort = "587";
		} else {
			throw new UnrecognisedEmailServiceException("Unrecognised email service: " + this.getEmailAddress());
		}
	}

	public Email(StatusPanel statusPanel, String emailAddress, String password, String address, String smtpAddress, String smtpPort, ProtocolType protocol)
	{
		this.statusPanel = statusPanel;
		this.emailAddress = emailAddress;
		this.password = password;
		this.SMTPAddress = smtpAddress;
		this.SMTPPort = smtpPort;
		this.protocol = protocol;

		switch(protocol){
		case IMAP:
			this.IMAPAddress = address;
			break;
		case POP3:
			this.POP3Address = address;
			break;
		}
	}

	public void initialise() throws NoSuchProviderException, MessagingException
	{
		// Connect to the mail server and retrieve the mail store	
		statusPanel.setText("Getting store...");
		this.store = getConnectedStore();
		statusPanel.setText("Getting store...Done.");
		
		// Retrieve the inbox
		statusPanel.setText("Retrieving inbox...");
		switch(this.protocol) {
		case IMAP:
			this.inbox = (IMAPFolder)this.getStore().getFolder("inbox");
			break;
		case POP3:
			// POP only downloads the inbox
			// In this example it downloads all folders, see the follwing article for details
			// https://support.google.com/mail/answer/16418?hl=en
			this.inbox = this.getStore().getFolder("INBOX");
			break;
		}
		statusPanel.setText("Retrieving inbox...Done.");

		// Check that the inbox is open
		if(!this.getInbox().isOpen())
			this.getInbox().open(Folder.READ_WRITE);

		// Retrieve the messages in the inbox
		this.messages = this.getInbox().getMessages();	
	}

	public void refresh()
	{
		try {
			this.initialise();
		} catch (NoSuchProviderException e) {
			this.statusPanel.printException(e);
		} catch (MessagingException e) {
			this.statusPanel.printException(e);
		}
	}

	public String[] getSubjects()
	{
		String[] subjects = new String[this.getMessages().length];
		// Get the subjects in reverse order
		for(int i = 0; i < this.getMessages().length; i++)
		{
			int j = subjects.length - 1 - i;
			try {
				subjects[j] = this.getMessages()[i].getSubject();
			}
			catch (MessagingException e){}
		}

		return subjects;
	}

	public Message getMessage(int i)
	{
		Message[] ms = this.getMessages();
		return ms[ms.length - 1 - i];
	}

	public int getMessagesCount()
	{
		return this.getMessages().length;
	}

	private Store getConnectedStore() throws NoSuchProviderException, MessagingException
	{
		Session session = getRetrieveSession();
		statusPanel.setText("Connecting to the mailbox...");
		Store store = null;
		switch(this.protocol)
		{
		case IMAP:
			store = session.getStore("imaps");
			store.connect(getIMAPAddress(), getEmailAddress(), getPassword());
			break;
		case POP3:
			store = session.getStore("pop3s");
			store.connect(getPOP3Address(), getEmailAddress(), getPassword());
			break;
		}
		
		statusPanel.setText("Connecting to the mailbox...Done.");
		return store;
	}

	private Session getRetrieveSession()
	{
		// Set some mail properties
		Properties properties = System.getProperties();
		switch(this.protocol) {
		case IMAP:
			properties.setProperty("mail.store.protocol", "imaps");
			break;
		case POP3:
			properties.setProperty("mail.store.protocol", "pop3s");
			break;
		}

		properties.setProperty("mail.user", getEmailAddress());
		properties.setProperty("mail.password", getPassword());

		// Establish a mail session
		Session session = Session.getDefaultInstance(properties);

		return session;
	}

	private Session getSendSession()
	{
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.starttls.enable", "true");
		properties.setProperty("mail.smtp.host", this.getSMTPAddress());
		properties.setProperty("mail.smtp.port", this.getSMTPPort());
		properties.setProperty("mail.password", this.getPassword());

		return Session.getDefaultInstance(properties);
	}

	public void send(EmailMessage messageInfo)
	{
		try {
			Session session = this.getSendSession();
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(this.getEmailAddress()));
			message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(messageInfo.getTo().getText()));
			message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(messageInfo.getCC().getText()));
			message.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(messageInfo.getBCC().getText()));

			message.setSubject(messageInfo.getSubject().getText());

			Multipart multiPart = new MimeMultipart();
			MimeBodyPart messageText = new MimeBodyPart();
			messageText.setText(messageInfo.getMessage().getText());
			multiPart.addBodyPart(messageText);

			// Add attachments if they exist
			for(int i = 0; i < messageInfo.getFiles().size(); i++)
			{
				MimeBodyPart attachment = new MimeBodyPart();
				DataSource source = new FileDataSource(messageInfo.getFiles().get(i));
				attachment.setDataHandler(new DataHandler(source));
				attachment.setFileName(messageInfo.getFiles().get(i).getName());
				multiPart.addBodyPart(attachment);
			}

			message.setContent(multiPart);
			message.saveChanges();

			// Send the message
			Transport tr = session.getTransport("smtp");
			this.statusPanel.setText("Connecting to the server...");
			tr.connect(this.getSMTPAddress(), this.getEmailAddress(), this.getPassword());

			this.statusPanel.setText("Sending message...");
			tr.sendMessage(message, message.getAllRecipients());
			this.statusPanel.setText("Sending message...Done.");
		}
		catch (NoSuchProviderException e)
		{
			this.statusPanel.printException(e);	
		}
		catch (MessagingException e)
		{
			this.statusPanel.printException(e);
		}
	}

	private String getEmailAddress()
	{
		return this.emailAddress;
	}

	private String getPassword()
	{
		return this.password;
	}

	private String getPOP3Address()
	{
		return this.POP3Address;
	}

	private String getIMAPAddress()
	{
		return this.IMAPAddress;
	}

	private String getSMTPAddress()
	{
		return this.SMTPAddress;
	}

	private String getSMTPPort()
	{
		return this.SMTPPort;
	}

	private Message[] getMessages()
	{
		return this.messages;
	}

	private Store getStore()
	{
		while(this.store == null)
		{
			try
			{
				this.initialise();
			}
			catch (NoSuchProviderException e){}
			catch (MessagingException e){}
		}
		
		return this.store;
	}

	private Folder getInbox()
	{
		while(this.inbox == null)
		{
			try
			{
				this.initialise();
			}
			catch (NoSuchProviderException e){
				this.statusPanel.printException(e);
			} catch (MessagingException e){
				this.statusPanel.printException(e);
			}
		}

		return this.inbox;
	}
}
