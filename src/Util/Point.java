package Util;

public class Point
{
	public float x = 0;
	public float y = 0;
	public float z = 0;
	
	public Point()
	{

	}
	
	public Point(float x, float y)
	{
		this.x = x;
		this.y = y;		
	}
	
	public Point(float x, float y, float z)
	{
		this.x = x;
		this.y = y;		
		this.z = z;
	}

	public Point(Vector3 vector)
	{
		this.x = vector.x;
		this.y = vector.y;		
		this.z = vector.z;
	}
	
	public Point add(Point point)
	{
		x += point.x;
		y += point.y;
		z += point.z;
		return this;
	}
	
	public Point sub(Point point)
	{
		x -= point.x;
		y -= point.y;
		z -= point.z;
		return this;
	}
	
	public Point mul(Point point)
	{
		x *= point.x;
		y *= point.y;
		z *= point.z;
		return this;
	}
	
	public Point div(Point point)
	{
		x /= point.x;
		y /= point.y;
		z /= point.z;
		return this;
	}
	
	public Point add(float value)
	{
		x += value;
		y += value;
		z += value;
		return this;
	}
	
	public Point sub(float value)
	{
		x -= value;
		y -= value;
		z -= value;
		return this;
	}
	
	public Point mul(float value)
	{
		x *= value;
		y *= value;
		z *= value;
		return this;
	}
	
	public Point div(float value)
	{
		x /= value;
		y /= value;
		z /= value;
		return this;
	}
	
	public Point rotate(float angle)
	{
		double theta = Math.toRadians(angle);
		double cos = Math.cos(theta);
		double sin = Math.sin(theta);
		
		float newX = (float)((x*cos) - (y*sin));		
		float newY = (float)((x*sin) + (y*cos));
		
		x = newX;
		y = newY;
		
		return this;
	}
	
	static public Point add(Point p1, Point p2)
	{
		return new Point(
				p1.x + p2.x,
				p1.y + p2.y,
				p1.z + p2.z
				);
		
	}
	
	static public Point sub(Point p1, Point p2)
	{
		return new Point(
				p1.x - p2.x,
				p1.y - p2.y,
				p1.z - p2.z
				);
		
	}
	
	static public Point mul(Point p1, Point p2)
	{
		return new Point(
				p1.x * p2.x,
				p1.y * p2.y,
				p1.z * p2.z
				);
		
	}
	
	static public Point div(Point p1, Point p2)
	{
		return new Point(
				p1.x / p2.x,
				p1.y / p2.y,
				p1.z / p2.z
				);
		
	}
	
	@Override
	public String toString() 
	{
		return "("+x+","+y+","+z+")";
	}
}
