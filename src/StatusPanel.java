package envelope;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;

class StatusPanel extends JPanel
{
	private final JLabel label;

	public StatusPanel(JFrame frame)
	{
		super();
		this.setBorder(new BevelBorder(BevelBorder.LOWERED));
		this.setPreferredSize(new Dimension(frame.getWidth(), 16));
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.label = new JLabel();
		this.label.setHorizontalAlignment(SwingConstants.LEFT);
		this.add(this.getLabel());
	}	

	public void printException(Exception e)
	{
		this.printException(e, "Exception");
	}

	public void printException(Exception e, String prefix)
	{
		this.setText(prefix + ": " + e.getMessage());
	}

	public void setText(String text)
	{
		this.getLabel().setText(text);
	}

	private JLabel getLabel()
	{
		return this.label;
	}
}
