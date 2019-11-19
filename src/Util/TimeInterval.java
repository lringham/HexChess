package Util;

public class TimeInterval
{
	private float length;
	private float initialTime;	
	private boolean reoccuring = false;
	private GameTime timer = new GameTime();

	public TimeInterval(float length)
	{
		this.length = length;
		this.reoccuring = true;
	}	
	
	public TimeInterval(float length, boolean reoccuring)
	{
		this.length = length;
		this.reoccuring = reoccuring;
	}	
	
	public boolean elapsed()
	{
		boolean elapsed = timer.getTime() - initialTime >= length;
			
		if(elapsed && reoccuring)
			resetInterval();
		
		return elapsed;
	}
	
	public float getElapsedTime()
	{
		return clamp(timer.getTime() / length, 0f, 1f);
	}
	
	public void resetInterval()
	{
		initialTime = timer.getTime();
	}
	
	public void resetInterval(float length)
	{
		this.length = length;
		initialTime = timer.getTime();
	}
	
	public float clamp(float val, float min, float max)
	{
		if(val <= min)
			return min;
		else if(val >= max)
			return max;
		else
			return val;
	}

	public float getLength() 
	{
		return length;
	}
}
