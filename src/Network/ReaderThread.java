package Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class ReaderThread extends Thread
{		
	private Socket socket; 
	private PrintWriter out;
	private BufferedReader in;
	private boolean exit = false;
	private boolean read = true;
	private  List<String> messages = new ArrayList<String>();		
	private String ip;
	private int port;
	private Object lock = new Object();
	private NetworkHandler networkHandler;
	private int timeout = 1;
	
	public ReaderThread(String ip,int port, NetworkHandler networkHandler)
	{
		this.networkHandler = networkHandler;
		this.ip = ip;
		this.port = port;		
		this.socket = new Socket();
	}
	
	public ReaderThread(Socket socket, NetworkHandler networkHandler)
	{
		this.networkHandler = networkHandler;
		this.socket = socket;
		try {
			socket.setSoTimeout(timeout);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Socket getSocket()
	{
		return socket;
	}
	
	public synchronized void sendMessage(Message message)
	{
		Util.DebugLog.writeError("Sending:"+message.getText());
	    out.write(message.getText());
	    out.flush();
	}
	
	public synchronized void sendMessage(String message)
	{
		Util.DebugLog.writeError("Sending:"+message);
	    out.write(message);
	    out.flush();
	}
	
	public synchronized List<String> getMessages()
	{	
		ArrayList<String> tempMessages;
		synchronized (lock) 
		{
			tempMessages = new ArrayList<String>(messages);
			messages.clear();			
		}
		return tempMessages;		
	}
	
	public void run()
	{
		try 
		{
			Util.DebugLog.writeError("reader thread starting");
			
			//If the socket is not alive and connected then try and create one
			//Also, register this new socket with the network handler
			if(!socket.isConnected() || socket.isClosed())
			{
				socket = new Socket(ip, port);
				socket.setSoTimeout(timeout);
				networkHandler.setOpponentSocket(socket);
			}
			
			out = new PrintWriter(socket.getOutputStream(), true);
		    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		    
			String line = "";
			int value = 0;
			exit = false;
			
			while(!exit)
			{
				line = "";
				read = true;
			    while(read && !exit)
			    {		
			    	try 
			    	{ 
			    		value = in.read(); 
			    	} 
			    	catch (SocketTimeoutException e) 
			    	{
			    		read = false;
			    		continue;
			    	} 
			    	catch (IOException e) 
			    	{
						read = false; 
						exit = true;
					}
			    	
			    	if(value == 10) //end of the line seen
			    	{
			    		read = false;
			    		continue;
			    	}
			    	else if(value == -1) //disconnected
			    	{
						read = false; 
						exit = true;
			    	}
			    	
					line += Character.toString((char) value);
			    }
			    
			    if(!line.equals(""))
			    {
			    	Util.DebugLog.writeError("Received:"+line);
			    	
					synchronized (lock) 
					{
				    	messages.add(line);			
					}
			    }
			}
		} 
		catch (IOException e1) 
		{
			Util.DebugLog.writeError("Can't connect to ip: "+ip+" socket: "+socket);
		}
		Util.DebugLog.writeError("reader thread quitting");	

	}

	public void exit(boolean value) 
	{
		exit = value;		
	}
}
