package Util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DebugLog 
{
	public static boolean print = true;
	public static boolean fileOpened = false;
	private static BufferedWriter bw = null;
	private static String filename = "errorLog.txt";
	
	public static void writeError(String message)
	{
		if(print)
			System.out.println(message);
		
		try 
		{	
			if(!fileOpened)
			{
				File file = new File(filename);
				
				if (!file.exists()) 
					file.createNewFile();				
				
				bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile(), false));
				fileOpened = true;				
			}
		 
			bw.write(message+"\r\n");
			bw.flush();
		} 
		catch (IOException e) 
		{
			System.out.println("Can't write to error log... where's your god now?");	
		}
	}
	
	public static void destroy()
	{
		try {
			if(bw != null)
				bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
