package GameStates;
import java.util.ArrayList;

import RenderingSystem.Drawable;
import RenderingSystem.RenderingSystem;
import RenderingSystem.Sprite;
import Util.GameTime;
import Util.Point;
import Util.Screen;
import Util.TimeInterval;


public abstract class GameState
{	
	public float transitionInTime = 1000f;
	public float transitionOutTime = 1000f;
	public TimeInterval transitionInterval = null;
	
	private boolean changeState = false;
	private boolean transitioningIn = false;
	private boolean transitioningOut = false;
	private boolean transitionedOut = false;
	private boolean transitionedIn = false;
	private StateTypes transitionTarget;	
	protected Sprite background = null;
	ArrayList<Drawable> drawables = new ArrayList<Drawable>();
	
	GameTime timer;
	public float fadeAmount = 0.f;
	
	public enum StateTypes
	{
		SPLASH_SCREEN,
		MAIN_MENU,
		NEW_GAME,
		FIND_GAME,
		JOIN_GAME,
		PLAY, 
	};
	
	public abstract void initializeState();
	public abstract void destroyState();
	
	public void draw(float deltaTime)
	{
		if(background != null)
			background.draw(deltaTime);
	}
	
	public void update(float deltaTime)
	{
		if(transitioningIn)
		{			
			if(transitionInterval.elapsed())
			{
				transitioningIn = false;
				transitionedIn = true;
				fadeAmount = 0.f;
			}
			else
				fadeAmount = 1.f - transitionInterval.getElapsedTime();
				
		}
		
		else if(transitioningOut)
		{	
			if(transitionInterval.elapsed())
			{
				changeState = true;
				transitionedOut = true;
				transitioningOut = false;
				fadeAmount = 1.f;
				destroyState();
			}
			else
				fadeAmount = transitionInterval.getElapsedTime();				
		}	
	}
	
	public void setBackground(Sprite background)
	{
		this.background = background;
		this.background.setLayer(RenderingSystem.DRAW_LAYER.BACKGROUND);
		this.background = background;
		this.background.setWidth(Screen.getWidth());
		this.background.setHeight(Screen.getHeight());
		this.background.setPosition(new Point(0,background.getHeight()));
		
		drawables.add(background);		
	}
	
	public void transitionIn()
	{				
		if(!transitioningIn && !transitioningOut)
		{
			transitioningIn = true;
			transitionInterval = new TimeInterval(transitionInTime, false);
		}
	}
	
	public void transitionOut(StateTypes target)
	{
		if(!transitioningOut && !transitioningIn)
		{
			transitioningOut = true;
			this.transitionTarget = target;
			transitionInterval = new TimeInterval(transitionOutTime, false);
		}
	}

	public StateTypes getTarget()
	{
		return transitionTarget;
	}

	public boolean transitioningOut()
	{
		return transitioningOut;
	}

	protected boolean transitionedOut() 
	{
		return transitionedOut;
	}
	
	protected boolean transitionedIn() 
	{
		return transitionedIn;
	}
	
	public boolean transitioningIn()
	{
		return transitioningIn;
	}
	
	protected boolean transitioning() 
	{		
		return (transitioningIn() || transitioningOut());
	}
	
	public boolean pollStateChange()
	{
		return changeState;
	}
}
