package edu.iastate.cs.theseguys.network;


public class LogoutResponse extends AbstractMessage {
	private static final long serialVersionUID = -510932825868392337L;
	private boolean confirmed;
	
	public LogoutResponse(boolean confirmed)
	{
		this.confirmed = confirmed;
	}
	
	
	public boolean getConfirmed()
	{
		return confirmed;
	}
	
}
