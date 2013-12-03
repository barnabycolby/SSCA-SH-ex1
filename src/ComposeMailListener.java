package envelope;

import javax.swing.*;
import java.awt.event.*;

class ComposeMailListener implements ActionListener
{
	private final JFrame frame;
	private final Email email;

	public ComposeMailListener(JFrame frame, Email email)
        {
                this.frame = frame;
                this.email = email;
        }

	@Override
	public void actionPerformed(ActionEvent e)
	{
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run()
			{
				new ComposeMailDialog(frame, email);
			}
		});
	}
}
