package envelope;

import javax.swing.event.*;
import javax.swing.*;
import javax.mail.*;
import java.io.IOException;

class MessageTableSelectionListener implements ListSelectionListener {
	private final JMimePane messagePane;
	private final Email email;
	private int oldIndex;

	public MessageTableSelectionListener(JMimePane messagePane, Email email)
	{
		this.messagePane = messagePane;
		this.email = email;
		this.oldIndex = 0;
	}	

	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		ListSelectionModel lsm = (ListSelectionModel)e.getSource();

		if (!e.getValueIsAdjusting()){
			// Replace the content of the messagePane
			// Work out the selected index, it could either be e.getFirstIndex() or e.getLastIndex()
			int selectedIndex;
			if(e.getFirstIndex() == this.getOldIndex())
			{
				selectedIndex = e.getLastIndex();
				this.setOldIndex(e.getLastIndex());
			} else {
				selectedIndex = e.getFirstIndex();
				this.setOldIndex(e.getFirstIndex());
			}

			this.getMessagePane().setContent(email.getMessage(selectedIndex));
		}
	}

	private int getOldIndex()
	{
		return this.oldIndex;
	}

	private void setOldIndex(int i)
	{
		this.oldIndex = i;
	}

	private JMimePane getMessagePane()
	{
		return this.messagePane;
	}

	private Email getEmail()
	{
		return this.email;
	}
}
