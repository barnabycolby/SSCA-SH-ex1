package envelope;

import javax.swing.*;
import java.util.*;
import javax.mail.*;
import java.io.*;
import java.awt.*;

class JMimePane extends JPanel
{
	private final StatusPanel statusPanel;

	public JMimePane(StatusPanel statusPanel)
	{
		super();
		this.statusPanel = statusPanel;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}

	public void setContent(Message message)
	{
		this.removeAll();

		try {
			if(message.getContentType().contains("multipart"))
			{
				Multipart multipart = (Multipart)message.getContent();
				this.setContent(multipart);
			} else if(message.getContentType().contains("TEXT/PLAIN")) {
				JTextPane pane = new JTextPane();
				pane.setEditable(false);
				pane.setContentType("text/plain");
				pane.setText(message.getContent().toString());
				this.add(pane);
			} else if(message.getContentType().contains("TEXT/HTML")) {
				JTextPane pane = new JTextPane();
				pane.setEditable(false);
				pane.setContentType("text/html");
				pane.setText(message.getContent().toString());
				this.add(pane);
			}else {
				JTextPane pane = new JTextPane();
				pane.setEditable(false);
				pane.setContentType(message.getContentType());
				pane.setText(message.getContent().toString());
				this.add(pane);
			}

			this.validate();
		}
		catch (MessagingException e)
		{
			this.statusPanel.printException(e);
		}
		catch (IOException e)
		{
			this.statusPanel.printException(e);
		}
	}

	private void setContent(Multipart multipart) throws MessagingException, IOException
	{
		for(int i = 0; i < multipart.getCount(); i++)
		{
			BodyPart bodyPart = multipart.getBodyPart(i);
			JTextPane pane = new JTextPane();
			pane.setEditable(false);
			String disposition = bodyPart.getDisposition();
			if(disposition != null && (disposition.equalsIgnoreCase(Part.ATTACHMENT) || disposition.equalsIgnoreCase(Part.INLINE))) {
				this.add(new DownloadAttachmentJButton(bodyPart, this.statusPanel));
			} else {
				if(bodyPart.getContentType().contains("TEXT/HTML")) {
					pane.setContentType("text/html");
				} else if(bodyPart.getContentType().contains("TEXT/PLAIN")) {
					pane.setContentType("text/plain");
				} else {
					pane.setContentType(bodyPart.getContentType());
				}

				pane.setText(bodyPart.getContent().toString());
				this.add(pane);
			}
			
		}
	}
}
