package Game;
import org.lwjgl.opengl.Display;

import GUI.DrawableText;
import GameStates.FindGameState;
import GameStates.GameState;
import GameStates.JoinGameState;
import GameStates.MainMenuState;
import GameStates.NewGameState;
import GameStates.PlayState;
import GameStates.SplashScreenState;
import Network.NetworkHandler;
import RenderingSystem.RenderingSystem;
import RenderingSystem.Sprite;
import Sound.SoundSystem;
import Util.Color;
import Util.GameTime;
import Util.Point;
import Util.Screen;


public class Game 
{	
	GameState currentState = null;
	public static boolean running = true;
	DrawableText fps = null;
	Sprite fadeScreen = null;
	NetworkHandler networkHandler = null;
	public static String playerDesignation = "";
	
	public Game()
	{		
		initialize();
	}
	
	/*initialize: Set the window title, init the rendering system and current state.	 
	 */
	public void initialize()
	{
		@SuppressWarnings("unused")
		Screen screen = new Screen(1280, 720, "Hex Chess");
		networkHandler = new NetworkHandler();
		RenderingSystem.initialize();
		SoundSystem.initialize();
		currentState = new SplashScreenState();		
		
    	fadeScreen = RenderingSystem.generateSprite("black");
    	fadeScreen.setColor(new Color(0.f, 0.f, 0.f, 1.f));    	    	
		fadeScreen.setWidth(Screen.getWidth());
		fadeScreen.setHeight(Screen.getHeight());
		fadeScreen.setPosition(new Point(0, fadeScreen.getHeight()));
		fadeScreen.setLayer(RenderingSystem.DRAW_LAYER.FOREGROUND3);
		
	}
	
	/*start: This method starts the game and runs the main while loop.	 
	 */
	public void start()
    {
		GameTime gameTime = new GameTime();	
		fps = RenderingSystem.generateText("FPS", new Point(Screen.getWidth() - 100, Screen.getHeight() - 10));
		fps.setLayer(RenderingSystem.DRAW_LAYER.FOREGROUND1);
		
		while (!Display.isCloseRequested() && running)
		{
			float deltaTime = gameTime.getDelta();
			fps.setText("FPS:" + Integer.valueOf((int) (1000f/deltaTime)));
			
			update(deltaTime);
			render(deltaTime);
		}	
		
		
		currentState.destroyState();
		networkHandler.destroy();
		SoundSystem.destroy();
		RenderingSystem.destroy();
		Util.DebugLog.destroy();
	}
	
	/*update: update the scene
	 */
	public void update(float deltaTime)
	{
		checkChangeState();
		currentState.update(deltaTime);
		
		fadeScreen.setColorAlpha(currentState.fadeAmount);
	}
	
	/*render: Render the scene.	 
	 */
	public void render(float deltaTime)
	{
		RenderingSystem.startDraw();
		
		currentState.draw(deltaTime);
	
		fps.draw(deltaTime);
		fadeScreen.draw(deltaTime);		
		RenderingSystem.endDraw();
	}
	
	/*checkChangeState: This method is used for polling the current state to determine if 
	 * the current state should be changed.
	 */
	public void checkChangeState()
	{
		if(currentState.pollStateChange())
		{
			switch(currentState.getTarget())
			{
				case SPLASH_SCREEN:
					currentState = new SplashScreenState();
					break;
				case MAIN_MENU:
					currentState = new MainMenuState(networkHandler);
					break;
				case NEW_GAME:
					currentState = new NewGameState(networkHandler);
					break;
				case FIND_GAME:
					currentState = new FindGameState(networkHandler);
					break;
				case JOIN_GAME:
					currentState = new JoinGameState(networkHandler);
					break;
				case PLAY:
					currentState = new PlayState(networkHandler);
					break;
			}
		}
	}
}