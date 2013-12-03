package envelope;

import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import java.util.*;
import java.awt.*;

class RemoveFileListener implements ActionListener, MouseListener
{
	private final File file;
	private final LinkedList<File> files;
	private final JDialog dialog;
	private final JLabel label;
	private final JButton button;
	private final JPanel filesPanel;

	private Color oldColour;

	public RemoveFileListener(File file, LinkedList<File> files, JDialog dialog, JPanel filesPanel, JLabel label, JButton button)
	{
		this.file = file;
		this.files = files;
		this.dialog = dialog;
		this.filesPanel = filesPanel;
		this.label = label;
		this.button = button;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		this.files.remove(this.file);
		this.filesPanel.remove(this.label);
		removeButton(this.filesPanel, this.button, this.dialog);
	}

	private static void removeButton(JPanel filesPanel, JButton button, JDialog dialog)
	{
		filesPanel.remove(button);
		dialog.validate();
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		this.oldColour = this.button.getForeground();
		this.button.setForeground(Color.RED);
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		this.button.setForeground(this.oldColour);
	}

	@Override
	public void mouseReleased(MouseEvent e){}

	@Override
	public void mousePressed(MouseEvent e){}

	@Override
	public void mouseClicked(MouseEvent e){}
}
