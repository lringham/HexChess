package Util;

public class Line
{
	public Point p1 = new Point();
	public Point p2 = new Point(); 

	public Line(Point p1, Point p2)
	{
		this.p1.x = p1.x;
		this.p1.y = p1.y;
		
		this.p2.x = p2.x;
		this.p2.y = p2.y;
	}

	public Point getPointAtY(float y)
	{
		if (((p1.y <= y && p2.y >= y) || (p1.y >= y && p2.y <= y))) //check if the y is in the domain of the line
		{
			if (Float.compare(Float.NaN, calculateYIntercept()) == 0) //if the line is vertical
				return new Point(p1.x, y);
			else
				return new Point((y - calculateYIntercept()) / calculateSlope(), y);
		}
		else
			return null;
	}
	
	public Point getMidPoint()
	{
		
		return new Point((p1.x + p2.x) / 2f, (p1.y + p2.y) / 2f);
	}
	
	public float calculateSlope()
	{
		float xDiff = p2.x - p1.x;
		float yDiff = p2.y - p1.y;
		if (xDiff == 0.0f) //vertical line
		{
			return Float.NaN;
		}
		else
		{
			return yDiff / xDiff;
		}
	}

	public float calculateYIntercept()
	{
		float slope = calculateSlope();
		if (Float.compare(Float.NaN, slope) != 0) // if the line is not vertical 
		{
			return p1.y - (p1.x * slope);
		}
		else
		{
			return Float.NaN;
		}
	}
	
	public Point getPointAt(float percent)
	{
		float val = clamp(percent, 0f, 1f);
		float px = (p2.x - p1.x) * val + p1.x;
		float py = (p2.y - p1.y) * val + p1.y;
		return new Point(px, py);
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
}