package RenderingSystem;

import Util.Line;
import Util.Point;

public class MoveAnimation implements Animation
{
	private Line path = null;
	private Drawable drawable = null;
	private float duration = 0;
	private float elapsedTime = 0;
	
	public MoveAnimation(Drawable drawable, float duration, Point start, Point end) 
	{
		this.path = new Line(start, end);
		this.drawable = drawable;
		
		if(duration > 0)
			this.duration = duration;
		else
			throw new RuntimeException("Duration can't be zero!");
	}

	@Override
	public void update(float deltaTime) 
	{
		elapsedTime += deltaTime;
		
		if(elapsedTime > duration)
			elapsedTime = duration;
		
		if(elapsedTime <= duration)		
		{
			float pointInLine = (float)Math.sin((Math.PI/2f) * (elapsedTime / duration));
			drawable.setPosition(path.getPointAt(pointInLine));
		}
	}

	@Override
	public boolean isCompleted() 
	{
		return elapsedTime >= duration;
	}	
}
