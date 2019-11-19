package Util;

public class Quaternion 
{
	public float x;
	public float y;
	public float z;
	public float w;
	
	public Quaternion(float x, float y, float z, float w)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public float length()
	{
		return (float) Math.sqrt(x*x + y*y + z*z + w*w);
	}
	
	public Quaternion normalize()
	{
		float length = length();
		
		x /= length;
		y /= length;
		z /= length;
		w /= length;
		
		return this;
	}
	
	public Quaternion conjugate()
	{
		return new Quaternion(-x, -y, -z, w);
	}
	
	public Quaternion mul(Quaternion q)
	{
		float wNew = w*q.w - x*q.x - y*q.y - z*q.z;
		float xNew = x*q.w + w*q.x + y*q.z - z*q.y;
		float yNew = y*q.w + w*q.y + z*q.x - x*q.z;
		float zNew = z*q.w + w*q.z + x*q.y - y*q.x;

		return new Quaternion(xNew, yNew, zNew, wNew);
	}
	
	public Quaternion mul(Vector3 v)
	{
		float wNew = -x*v.x - y*v.y - z*v.z;
		float xNew =  w*v.x + y*v.z - z*v.y;
		float yNew =  w*v.y + z*v.x - x*v.z;
		float zNew =  w*v.z + x*v.y - y*v.x;

		return new Quaternion(xNew, yNew, zNew, wNew);
	}
}
