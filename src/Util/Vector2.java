package Util;

public class Vector2
{
	public float x = 0f;
	public float y = 0f;	
	
	public Vector2(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vector2()
	{
		this.x = 0f;
		this.y = 0f;				
	}
	
	public Vector2(Point point) 
	{
		this.x = point.x;
		this.y = point.y;
	}

	public float magnitude()
	{
		return (float) Math.sqrt(x*x + y*y);	
	}
	
	public Vector2 direction()
	{
		float magnitude = magnitude();
		return new Vector2(x / magnitude, y / magnitude);
	}
	
	public void toUnit()
	{
		float magnitude = magnitude();
		x = x / magnitude;
		y = y / magnitude;
	}
	
	public float dotProduct(Vector2 vector)
	{
		return x*vector.x + y*vector.y;
	}
		
	public float angleBetweenRad(Vector2 vector)
	{
		return ((float)Math.atan2((double) determinant(vector),dotProduct(vector)));
	}

	public float angleBetweenDeg(Vector2 vector)
	{
		return (float) (angleBetweenRad(vector) * (-180f / Math.PI));
	}
	
	private float determinant(Vector2 vector) 
	{
		return ((x*vector.y) - (y*vector.x));
	}
	
	public void add(Vector2 vector)
	{
		x += vector.x;
		y += vector.y;		
	}	
	
	public void sub(Vector2 vector)
	{
		x -= vector.x;
		y -= vector.y;
	}

	public void mul(Vector2 vector)
	{
		x *= vector.x;
		y *= vector.y;		
	}	
	
	public void div(Vector2 vector)
	{
		x /= vector.x;
		y /= vector.y;		
	}
	
	public void rotate(float angle)
	{
		double theta = Math.toRadians(angle);
		double cos = Math.cos(theta);
		double sin = Math.sin(theta);
		
		x = (float)(x*cos - y*sin);
		y = (float)(x*sin + y*cos);
	}
	
	public static Vector2 add(Vector2 vector1, Vector2 vector2)
	{
		return new Vector2(vector1.x + vector2.x, vector1.y + vector2.y);		
	}	
	
	public static Vector2 sub(Vector2 vector1, Vector2 vector2)
	{
		return new Vector2(vector1.x - vector2.x, vector1.y - vector2.y);		
	}
	
	public static Vector2 mul(Vector2 vector1, Vector2 vector2)
	{
		return new Vector2(vector1.x * vector2.x, vector1.y * vector2.y);		
	}	
	
	public static Vector2 div(Vector2 vector1, Vector2 vector2)
	{
		return new Vector2(vector1.x / vector2.x, vector1.y / vector2.y);		
	}
	
	public static Vector2 rotate(Vector2 vector, float angle)
	{
		double theta = Math.toRadians(angle);
		double cos = Math.cos(theta);
		double sin = Math.sin(theta);
		
		return new Vector2((float)(vector.x*cos - vector.y*sin), (float)(vector.x*sin + vector.y*cos));		
	}
	
	private static float determinant(Vector2 vector1, Vector2 vector2) 
	{
		return ((vector1.x*vector2.y) - (vector1.y*vector2.x));
	}
	
	public static float dotProduct(Vector2 vector1, Vector2 vector2)
	{
		return vector1.x*vector2.x + vector1.y*vector2.y;
	}
	
	public static float angleBetweenRad(Vector2 vector1, Vector2 vector2)
	{
		return ((float)Math.atan2((double) determinant(vector1, vector2),dotProduct(vector1, vector2)));
	}
	
	public static float angleBetweenDeg(Vector2 vector1, Vector2 vector2)
	{
		return (float) (angleBetweenRad(vector1, vector2) * (180f / Math.PI));
	}
}
