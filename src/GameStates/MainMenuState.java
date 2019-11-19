package GameStates;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import GUI.Button;
import Game.Game;
import Network.NetworkHandler;
import RenderingSystem.RenderingSystem;
import Sound.Sound;
import Sound.SoundSystem;
import Util.Color;
import Util.Point;
import Util.Screen;
import Util.TimeInterval;


public class MainMenuState extends GameState
{
	Button newGameButton;
	Button findGameButton;
	Button settingsButton;
	Button quitButton;
	TimeInterval time = new TimeInterval(500, true);
	Sound buttonSound;
	NetworkHandler networkHandler;
	String player = "player1";
	Point point = new Point(1,0);
	
	public MainMenuState(NetworkHandler networkHandler)
	{		
		this.networkHandler = networkHandler;
		buttonSound = SoundSystem.generateSound("button.wav");
				
		//setBackground(RenderingSystem.generateSprite("lightWood"));
		RenderingSystem.backgroundColor = new Color(0.5f,0.5f,.8f);
		
		newGameButton = new Button(RenderingSystem.generateSprite("newGameButton"), RenderingSystem.generateSprite("newGameButtonPress"), buttonSound);
		findGameButton = new Button(RenderingSystem.generateSprite("joinGameButton"), RenderingSystem.generateSprite("joinGameButtonPress"), buttonSound);
		//settingsButton = new Button(RenderingSystem.generateSprite("settingsButton"), RenderingSystem.generateSprite("settingsButtonPress"), buttonSound);
		quitButton = new Button(RenderingSystem.generateSprite("quitButton"), RenderingSystem.generateSprite("quitButtonPress"), buttonSound);
		
		drawables.add(newGameButton);
		drawables.add(findGameButton);
		drawables.add(settingsButton);
		drawables.add(quitButton);

		newGameButton.setPosition(new Point(
				(Screen.getWidth() * .5f)-(newGameButton.getWidth()/2f),
				550));
		findGameButton.setPosition(new Point(
				(Screen.getWidth() * .5f)-(newGameButton.getWidth()/2f),
				400));
		quitButton.setPosition(new Point(
				(Screen.getWidth() * .5f)-(quitButton.getWidth()/2f),
				250));
		//quitButton.setPosition(new Point(
		//		(Screen.getWidth() * .5f)-(quitButton.getWidth()/2f),
		//		250));

		super.transitionIn();
	}
	
	@Override
	public void update(float gameTime)
	{		
		if(!transitioningOut() && !transitioningIn())
		{					
			while (Keyboard.next())
			{
				if (Keyboard.getEventKeyState())
				{
//					int key = Keyboard.getEventKey();
//					if (key == Keyboard.KEY_SPACE)
//						console.insertLine("hello "+a++);
//					else if(key == Keyboard.KEY_UP)
//						console.scrollUp();
//					else if(key == Keyboard.KEY_DOWN)
//						console.scrollDown();
//					else if(key == Keyboard.KEY_C)
//						console.clear();
				}
			}
			
			if(Mouse.isButtonDown(0))
			{
				if(newGameButton.clicked(new Point(Mouse.getX(), Mouse.getY())))
					super.transitionOut(GameState.StateTypes.NEW_GAME);
				if(findGameButton.clicked(new Point(Mouse.getX(), Mouse.getY())))
					super.transitionOut(GameState.StateTypes.JOIN_GAME);
//				if(settingsButton.clicked(new Point(Mouse.getX(), Mouse.getY())))					
//					Screen.setFullscreen(true);
				if(quitButton.clicked(new Point(Mouse.getX(), Mouse.getY())) && Game.running)
				{					
					try 
					{
						Thread.sleep(450);
					} 
					catch (InterruptedException e) 
					{
						Util.DebugLog.writeError("quit button delay interrupted");
					}
					Game.running = false;
				}
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
		newGameButton.draw(deltaTime);
		findGameButton.draw(deltaTime);
		//settingsButton.draw(deltaTime);
		quitButton.draw(deltaTime);
	}
}
