package envelope;

import javax.mail.*;
import java.io.*;
import javax.swing.*;
import java.awt.event.*;

class DownloadAttachmentJButton extends JButton implements ActionListener
{
	private final BodyPart bodyPart;
	private final StatusPanel statusPanel;

	public DownloadAttachmentJButton(BodyPart bp, StatusPanel sp)
	{
		super();
		try {
			this.setText("Download " + bp.getFileName());
		} catch (MessagingException e) {
			this.setText("Download attachment");
		}
		this.bodyPart = bp;
		this.statusPanel = sp;

		this.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		try {
			this.statusPanel.setText("Downloading " + this.bodyPart.getFileName() + "...");
			InputStream is = this.bodyPart.getInputStream();
			File f = new File(this.bodyPart.getFileName());
			FileOutputStream fos = new FileOutputStream(f);
			byte[] buf = new byte[4096];
			int bytesRead;
			int count = 0;
			while((bytesRead = is.read(buf)) != -1)
			{
				fos.write(buf, 0, bytesRead);
				count += bytesRead;
				this.statusPanel.setText("Downloading " + this.bodyPart.getFileName() + "..." + count);
			}
			this.statusPanel.setText("Downloading " + this.bodyPart.getFileName() + "...done");
			fos.close();
		}
		catch (IOException ex) {}
		catch (MessagingException ex) {}
	}
}
