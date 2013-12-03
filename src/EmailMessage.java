package envelope;

import javax.swing.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

class EmailMessage
{
	private final JTextField to;
	private final JTextField cc;
	private final JTextField bcc;
	private final JTextField subject;

	private final JPanel filesPanel;
	private final LinkedList<File> files;

	private final JEditorPane message;

	public EmailMessage()
	{
		this.to = new JTextField();
		this.cc = new JTextField();
		this.bcc = new JTextField();
		this.subject = new JTextField();

		this.filesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		this.files = new LinkedList<File>();

		this.message = new JEditorPane();
	}

	private JButton generateRemoveButton(File file, JDialog dialog, JLabel label)
	{
		JButton button = new JButton("x");
		
		// Aesthetics
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.setOpaque(false);
		// Make sure that the height of the button is the same as the label so that the gridlayout doesn't expand the height of the other fields
		button.setForeground(Color.GRAY);

		// Add listeners
		RemoveFileListener removeFileListener = new RemoveFileListener(file, this.getFiles(), dialog, this.getFilesPanel(), label, button);
		button.addActionListener(removeFileListener);
		button.addMouseListener(removeFileListener);

		return button;
	}

	public void addFile(File file, JDialog dialog)
	{
		// First check that the file is not already an attachment
		if(!this.getFiles().contains(file)){
			this.getFiles().add(file);

			JLabel label = new JLabel(file.getName());
			JButton remove = this.generateRemoveButton(file, dialog, label);
			this.getFilesPanel().add(label);
			this.getFilesPanel().add(remove);
		}
	}

	public JTextField getTo()
	{
		return this.to;
	}

	public JTextField getCC()
	{
		return this.cc;
	}

	public JTextField getBCC()
	{
		return this.bcc;
	}

	public JTextField getSubject()
	{
		return this.subject;
	}

	public JPanel getFilesPanel()
	{
		return this.filesPanel;
	}

	public LinkedList<File> getFiles()
	{
		return this.files;
	}

	public JEditorPane getMessage()
	{
		return this.message;
	}
}
