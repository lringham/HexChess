package GameStates;
import RenderingSystem.RenderingSystem;
import Util.Color;

public class SplashScreenState extends GameState
{	

	float splashScreenDelay = 500.f;
	float splashScreenElapsedTime = 0.f;
	
	
	public SplashScreenState()
	{
		RenderingSystem.backgroundColor = new Color(0.f, 0.f, 0.f);
		super.transitionIn();
		
		setBackground(RenderingSystem.generateSprite("logo"));
	}

	@Override
	public void update(float gameTime)
	{
		if(!transitioningIn())
		{
			splashScreenElapsedTime += gameTime;
			if(splashScreenElapsedTime >= splashScreenDelay)
			{
				transitionOut(StateTypes.MAIN_MENU);
			}
		}
		
		super.update(gameTime);
	}

	@Override
	public void destroyState() 
	{
		drawables.clear();
	}

	@Override
	public void initializeState() 
	{

	}

	@Override
	public void draw(float deltaTime) 
	{
		super.draw(deltaTime);
	}
}
