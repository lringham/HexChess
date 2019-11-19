package Util;

public class Color
{
	public float red = 1.0f;
	public float green = 1.0f;
	public float blue = 1.0f;
	public float alpha = 1.0f;

	public Color(float red, float green, float blue, float alpha)
	{
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}
	
	public Color(float red, float green, float blue)
	{
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = 1.f;
	}
	
	public Color()
	{
				
	}
}
