package envelope;

import java.awt.event.*;

class RefreshListener implements ActionListener
{
	private final Email email;
	private final MessageTableModel messageTableModel;

	public RefreshListener(Email email, MessageTableModel mtm)
	{
		this.email = email;
		this.messageTableModel = mtm;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		this.email.refresh();
		this.messageTableModel.setData(this.email);
	}
}
