package Network;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

public class NetworkHandler 
{
	private static final String configFile = "Assets\\Network\\config.txt";
	int 						timeout = 1;
	private int 				portScanLimit = 10;
	
	private Socket 				server = null;
	private ReaderThread 		serverThread = null;
	private boolean 			serverConnected = false;
	private int 				serverPort = 0;
	private String 				serverIP = "";	
	
	private Socket 				opponent = null;
	private ReaderThread 		opponentThread = null;

	private static int 			myPort = 0;
	private static ServerSocket listenerSocket = null;
	private AcceptingThread 	acceptingThread = null;
	private boolean 			listenerSocketBound = false;	
	
	public NetworkHandler()
	{		
		BufferedReader reader;
		String line = "";
		
		try
		{
			reader = new BufferedReader(new FileReader(configFile));
			while((line = reader.readLine()) != null)
			{
				String header = "";
				line = line.substring(0, line.indexOf("#"));				
				String[] parsedLine = line.split("\\s+");
				if(parsedLine.length > 0)
					header = parsedLine[0];
				
				switch(header)
				{
				case "ServerIP":
					serverIP = parsedLine[1];
					break;
				case "ServerPort":
					serverPort = Integer.parseInt(parsedLine[1]);
					break;
				case "MyPort":
					myPort = Integer.parseInt(parsedLine[1]);
					break;					
				}
			}
			reader.close();
		}
		catch(IOException e)
		{
			Util.DebugLog.writeError("Network config file could not loaded\n"+e.getMessage());
		}
	}
	
	public boolean isServerConnected()
	{
		return serverConnected;
	}
	
	
	public boolean isOpponentConnected()
	{
		boolean opponentThreadAlive = opponentThread != null ?  opponentThread.isAlive() : false;
		if(opponent == null)
			return false;
		else
			return !(!opponent.isConnected() || opponent.isClosed() || opponent.isInputShutdown() || opponent.isOutputShutdown() || !opponentThreadAlive);
	}

	public boolean isOpponentConnecting()
	{
		boolean opponentThreadAlive = opponentThread != null ?  opponentThread.isAlive() : false;
		return opponentThreadAlive && !isOpponentConnected();
	}

	
	protected void setOpponentSocket(Socket socket)
	{
		opponent = socket;			
	}
	
	protected void setOpponent(Socket socket)
	{
		opponent = socket;	
		opponentThread = new ReaderThread(socket,this);
		opponentThread.start();
	}
	
	protected void setOpponent(String ip, int port)
	{
		opponentThread = new ReaderThread(ip, port, this);				
		opponentThread.start();
		disconnectServer();

	}
	
	public void disconnectServer() 
	{
		try 
		{
			if(serverThread != null)
			{							
				serverThread.exit(true);
				serverThread.join();				
				serverThread = null;
			} 
			if(server != null)
			{
				serverConnected = false;
				server.close();
				server = null;
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}

	public void disconnectOpponent() 
	{
		resetAcceptingThread();
		try 
		{
			if(opponentThread != null)
			{			
				opponentThread.exit(true);
				opponentThread.join();
				opponentThread = null;
			} 
			if(opponent != null)
			{
				opponent.close();
				opponent = null;
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}	
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
	
	public boolean connectServer()
	{
		try 
		{
			serverThread = new ReaderThread(serverIP, serverPort,this);
			server = serverThread.getSocket();
			server.setSoTimeout(timeout);
			serverConnected = true;
			
			serverThread.start();
			return true;
		} 
		catch (IOException e) 
		{
			Util.DebugLog.writeError("Unable to connect to server ("+serverIP+":"+serverPort+")");
			return false;
		}
	}
		
		
	public void sendServerMessage(Message message)
	{
		if(serverConnected)
		{			
			serverThread.sendMessage(message);
		}
	}
	
	public void sendServerMessage(String message)
	{
		if(serverConnected)
		{			
			serverThread.sendMessage(message);
		}
	}
	
	public void sendOpponentMessage(Message message)
	{
		if(isOpponentConnected())
		{			
			opponentThread.sendMessage(message);
		}
	}
	
	public void sendOpponentMessage(String message)
	{
		if(isOpponentConnected())
		{			
			opponentThread.sendMessage(message);
		}
	}
	
	public List<String> getServerMessages()
	{		
		return serverThread.getMessages();
	}
	
	public List<String> getOpponentMessages()
	{		
		return opponentThread.getMessages();
	}
	
	public void destroy()
	{
		disconnectOpponent();
		disconnectServer();
		if(acceptingThread != null && acceptingThread.isAlive())
		{
			acceptingThread.running = false;
			try 
			{
				acceptingThread.join();
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}

	public static String getIP() 
	{
		
	    String ip;
	    try {
	        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
	        while (interfaces.hasMoreElements()) 
	        {
	            NetworkInterface iface = interfaces.nextElement();
	            // filters out 127.0.0.1 and inactive interfaces
	            if (iface.isLoopback() || !iface.isUp())
	                continue;

	            Enumeration<InetAddress> addresses = iface.getInetAddresses();
	            while(addresses.hasMoreElements()) 
	            {
	                InetAddress addr = addresses.nextElement();
	                ip = addr.getHostAddress();
	                return ip;
	                //Util.DebugLog.writeError(iface.getDisplayName() + " " + ip);
	            }
	        }
	    } catch (SocketException e) {
	        throw new RuntimeException(e);
	    }
	    
		return "";
	}
	
	public static int getPort() 
	{
		return myPort;
	}

	public void bindListenerSocket()
	{
		while(!listenerSocketBound)
		{
			int origPort = myPort;
			try 
			{
				listenerSocket = new ServerSocket(myPort);
				listenerSocketBound = true;
			} 
			catch (IOException e) 
			{
				if(origPort - myPort >= portScanLimit)
					throw new RuntimeException("Cannot find open port on " + myPort);
				else
					myPort++;				
			}
		}		
	}
		

	public void bindListenerSocket(int port)
	{
		while(!listenerSocketBound)
		{
			int origPort = port;
			try 
			{
				listenerSocket = new ServerSocket(port);
				listenerSocketBound = true;
			} 
			catch (IOException e) 
			{
				if(origPort - port >= portScanLimit)
					throw new RuntimeException("Cannot find open port on " + origPort);
				else
					port++;				
			}
		}		
	}
	
	public void acceptConnection() 
	{
		if(listenerSocketBound && acceptingThread == null)
		{
			acceptingThread = new AcceptingThread(listenerSocket, this);
			acceptingThread.start();
		}
		else if(!listenerSocketBound)
			Util.DebugLog.writeError("listenerSocket not bound");
		else
			Util.DebugLog.writeError("acceptingThread already exists");
	}

	public void resetAcceptingThread()
	{
		if(acceptingThread != null)
		{
			listenerSocketBound = false;
			if(acceptingThread.isAlive() && acceptingThread != null)
			{
				acceptingThread.running = false;
				try {
					acceptingThread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			acceptingThread = null;
		}
	}
	
	public boolean acceptConnection(Integer port) 
	{
		ServerSocket socket = null;
		boolean socketBound = false;
		
		while(!socketBound)
		{
			int origPort = port;
			try 
			{
				socket = new ServerSocket(port);
				socketBound = true;
			} 
			catch (IOException e) 
			{
				if(origPort - port >= portScanLimit)
					throw new RuntimeException("Cannot find open port on " + origPort);
				else
					port++;	
				return false;
			}
		}		
		
		if(socketBound && acceptingThread == null)
		{
			acceptingThread = new AcceptingThread(socket, this);
			acceptingThread.start();
			return true;
		}
		else if(!socketBound)
			Util.DebugLog.writeError("Socket not bound");
		else
			Util.DebugLog.writeError("AcceptingThread already exists");
		return false;
	}

	public void joinGame(String ip, Integer port) {
		setOpponent(ip, port);
	}
}
