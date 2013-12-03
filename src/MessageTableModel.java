package envelope;

import javax.swing.table.*;
import javax.mail.*;
import java.text.*;
import java.io.*;

class MessageTableModel extends AbstractTableModel
{
	private String[] columnNames;
	private Object[][] data;

	public MessageTableModel()
	{
		this.columnNames = new String[] { "Subject","From", "Attachment","Date" };
	}

	@Override
	public int getRowCount()
	{
		if(data != null){
			return this.data.length;
		} else {
			return 0;
		}
	}

	@Override
	public int getColumnCount()
	{
		return this.columnNames.length;
	}

	@Override
	public String getColumnName(int column)
	{
		return this.columnNames[column];
	}

	@Override
	public Object getValueAt(int row, int column)
	{
		if(this.data != null){
			return this.data[row][column];
		} else {
			return null;
		}
	}

	public void setData(Email email)
	{
		this.data = new Object[email.getMessagesCount()][getColumnCount()];
		
		for (int i = 0; i < email.getMessagesCount(); i++)
		{
			Message m = email.getMessage(i);

			// Subject
			try {
				this.data[i][0] = m.getSubject();
			} catch (MessagingException e) {
				this.data[i][0] = "";
			}
			
			// From
			try {
				this.data[i][1] = convertAddressesToString(m.getFrom());
			} catch (MessagingException e) {
				this.data[i][1] = "";
			}

			// Attachment
			if(hasAttachment(m))
			{
				this.data[i][2] = "Yes";
			} else {
				this.data[i][2] = "";
			}


			// Date
			try {
				this.data[i][3] = dateToString(m.getReceivedDate());
			} catch (MessagingException e) {
				this.data[i][3] = "";
			}
		}
	}

	private boolean hasAttachment(Message m)
	{
		Multipart multipart = null;
		try {
			if(m.getContent() instanceof Multipart) {
				multipart = (Multipart)m.getContent();
				boolean answer = false;	
				for(int i = 0; i < multipart.getCount(); i++)
				{
					try {
						BodyPart bodyPart = multipart.getBodyPart(i);
						String disposition = bodyPart.getDisposition();
						if(disposition != null && (disposition.equalsIgnoreCase(Part.ATTACHMENT) || disposition.equalsIgnoreCase(Part.INLINE)))
						{
							answer = true;
							break;
						}
					}
					catch (MessagingException e) {}
				}

				return answer;
			} else {
				return false;
			}
		} catch (IOException ex) {
			return false;
		} catch (MessagingException ex) {
			return false;
		}
	
	}

	private String convertAddressesToString(Address[] as)
	{
		if(as.length > 0)
		{
			String returnString = as[0].toString();
			for(int i = 1; i < as.length; i++)
			{
				returnString += "; " + as[i];
			}
			return returnString;
		}
		else
		{
			return "";
		}
	}

	private String dateToString(java.util.Date d)
	{
		if(d == null)
			return "";
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		return df.format(d);
	}
}
