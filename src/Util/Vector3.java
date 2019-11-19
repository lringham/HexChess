package Util;

public class Vector3
{
	public float x = 0.0f;
	public float y = 0.0f;
	public float z = 0.0f;
	
	public Vector3(float x, float y, float z)
	{
		this.x = x;
		this.y = y;	
		this.z = z;
	}
		
	public Vector3()
	{
		this.x = 0f;
		this.y = 0f;				
		this.z = 0f;
	}
	
	public float magnitude()
	{
		return (float) Math.sqrt(x*x + y*y + z*z);	
	}
	
	public Vector3 direction()
	{
		float magnitude = magnitude();
		return new Vector3(x / magnitude, y / magnitude, z / magnitude);
	}
	
	public void toUnit()
	{
		float magnitude = magnitude();
		x = x / magnitude;
		y = y / magnitude;
		z = z / magnitude;
	}
	
	public float dotProduct(Vector3 vector)
	{
		return x*vector.x + y*vector.y + z*vector.z;
	}
	
	public Vector3 crossProduct(Vector3 vector)
	{
		return new Vector3(
				y*vector.z - z*vector.y,
			    z*vector.x - x*vector.z, 
				x*vector.y - y*vector.x
			);
	}
	
	public float angleCCW(Vector3 vector)
	{
		return (float)Math.acos(dotProduct(vector) / (magnitude() * vector.magnitude()));
	}
	
	public float angleCW(Vector3 vector)
	{
		return 360.0f - angleCCW(vector);
	}
	
	public Vector3 add(Vector3 vector)
	{
		return new Vector3(x + vector.x, y + vector.y, z + vector.z);		
	}	
	
	public Vector3 sub(Vector3 vector)
	{
		return new Vector3(x - vector.x, y - vector.y, z - vector.z);		
	}	
	
	public void mul(float factor)
	{
		x *= factor;
		y *= factor;
		z *= factor;
	}
	
	public static Vector3 add(Vector3 vector1, Vector3 vector2)
	{
		return new Vector3(vector1.x + vector2.x, vector1.y + vector2.y, vector1.z + vector2.z);		
	}	
	
	public static Vector3 sub(Vector3 vector1, Vector3 vector2)
	{
		return new Vector3(vector1.x - vector2.x, vector1.y - vector2.y, vector1.z - vector2.z);		
	}

	public static Vector3 mul(Vector3 vec3, float factor)
	{
		return new Vector3(
		vec3.x * factor,
		vec3.y * factor,
		vec3.z * factor);
	}
}
