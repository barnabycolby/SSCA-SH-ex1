package envelope;

class UnrecognisedEmailServiceException extends Exception
{
	public UnrecognisedEmailServiceException(String message)
	{
		super(message);
	}
}
