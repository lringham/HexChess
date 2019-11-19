package Util;

public class GameTime
{
	double initialTime = 0;
	double lastTime = 0;
	double currentTime = 0;
	double fpsCap = 0.01666666666667;
	
	public GameTime()
	{
		initialTime = (double)System.nanoTime() / (double)1000000;
		getDelta();
	}
	
	public float getDelta() 
	{
		currentTime = (double)System.nanoTime() / (double)1000000;		
		double deltaTime = currentTime - lastTime;
		lastTime = currentTime;
	    	
//		if(deltaTime < fpsCap)
//		{
//			try
//			{
//				Thread.sleep((long)(deltaTime - fpsCap));
//			}
//			catch (InterruptedException e)
//			{
//				e.printStackTrace();
//			}
//		}
		
	    return (float)deltaTime;
	}
	
	public float getTime() 
	{
	    return (float) (((double)System.nanoTime() / (double)1000000) - initialTime);
	}
}
