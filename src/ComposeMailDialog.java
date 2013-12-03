package envelope;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class ComposeMailDialog extends JDialog implements ActionListener
{
	private final JFrame frame;
	private final Email email;
	private final EmailMessage emailMessage;
	private final JButton send;
	private final JButton attach;

	public ComposeMailDialog(JFrame frame, Email email)
	{
		// Create the dialog
		super(frame, "Compose Mail", false);

		this.frame = frame;
		this.email = email;

		// Create a menu
		JMenuBar menuBar = new JMenuBar();
		this.send = new JButton("Send");
		this.attach = new JButton("Attach");
		this.send.addActionListener(this);
		this.attach.addActionListener(this);
		menuBar.add(send);
		menuBar.add(attach);

		// Create a class to contain the email data
		this.emailMessage = new EmailMessage();

		// Create the header pane
		JPanel headerPane = this.createHeaderPane();

		this.setSize(400, 300);
		this.setLayout(new BorderLayout());
		this.setJMenuBar(menuBar);
		this.add(headerPane, BorderLayout.NORTH);
		this.add(this.emailMessage.getMessage(), BorderLayout.CENTER);
		this.setVisible(true);
	}

	private JPanel createHeaderPane()
	{
		JPanel headerPane = new JPanel();
		
		JPanel textLabels = new JPanel();
		textLabels.setLayout(new GridLayout(5,1));
		textLabels.add(new JLabel("To:"));
		textLabels.add(new JLabel("CC:"));
		textLabels.add(new JLabel("BCC:"));
		textLabels.add(new JLabel("Subject:"));
		textLabels.add(new JLabel("Attachments:"));

		JPanel textFields = new JPanel();
		textFields.setLayout(new GridLayout(5,1));
		textFields.add(this.emailMessage.getTo());
		textFields.add(this.emailMessage.getCC());
		textFields.add(this.emailMessage.getBCC());
		textFields.add(this.emailMessage.getSubject());
		textFields.add(this.emailMessage.getFilesPanel());

		headerPane.setLayout(new BorderLayout());
		headerPane.add(textLabels, BorderLayout.WEST);
		headerPane.add(textFields, BorderLayout.CENTER);

		return headerPane;
	}


	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == this.send) {
			this.email.send(this.emailMessage);
			// Close the dialog
			this.dispose();
		} else if(e.getSource() == this.attach) {
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(this);	
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				this.emailMessage.addFile(fc.getSelectedFile(), this);
				// Ensure that the new attachment is painted
				this.validate();
			}
		}
	}
}
