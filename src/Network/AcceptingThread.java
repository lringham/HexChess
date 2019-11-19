package Network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class AcceptingThread extends Thread
{
	Socket client;
	private ServerSocket serverSocket;
	private NetworkHandler networkHandler;
	public boolean running = false;
	public int timeout = 500;
	
	public AcceptingThread(ServerSocket socket, NetworkHandler networkHandler)
	{
		this.serverSocket = socket;
		this.networkHandler = networkHandler;
		
		try {
			socket.setSoTimeout(100000);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public void run()
	{	
		Util.DebugLog.writeError("accepting thread started");
		running = true;
		boolean opponentNotConnected = true;
		
		try 
		{
			serverSocket.setSoTimeout(timeout);
		} 
		catch (SocketException e1) 
		{
			Util.DebugLog.writeError(e1.getMessage());
		}
		
		while(opponentNotConnected && running)
		{
			try 
			{			
				client = serverSocket.accept();
				Util.DebugLog.writeError("opponent connected!");
				networkHandler.setOpponent(client);
				running = false;
			}	
			catch(SocketTimeoutException e)
			{
				;//keep trying until someone says to stop
			}
			catch (IOException e) 
			{
				running = false;
			}
		}
		Util.DebugLog.writeError("accepting thread exiting");
	}
}
