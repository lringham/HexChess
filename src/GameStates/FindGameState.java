package GameStates;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import GUI.Button;
import GUI.Console;
import GUI.DrawableText;
import Game.Game;
import Network.NetworkHandler;
import RenderingSystem.RenderingSystem;
import Util.Point;
import Util.Screen;



public class FindGameState extends GameState
{		
	private DrawableText notificationText = null;

	private NetworkHandler networkHandler = null;
	private Button button = new Button(RenderingSystem.generateSprite("joinGameButton"),RenderingSystem.generateSprite("joinGameButtonPress"));
	private Button scrollUpButton = new Button(RenderingSystem.generateSprite("arrow"),RenderingSystem.generateSprite("arrow"));
	private Button scrollDownButton = new Button(RenderingSystem.generateSprite("arrow"),RenderingSystem.generateSprite("arrow"));

	private boolean findGamesSent = false;
	private boolean gamesReceived = false;
	private boolean joiningGame = false;
	private Console console = new Console();
	
	public FindGameState(NetworkHandler networkHandler)
	{	
		setBackground(RenderingSystem.generateSprite("lightWood"));
		
		console.clear();
		this.networkHandler = networkHandler;
		notificationText = RenderingSystem.generateText("Find Game", new Point(0,Screen.getHeight()-50f));
		notificationText.setPosition((Screen.getWidth()/2f)-(notificationText.getWidth()/2f), notificationText.getY());	
		button.setPosition(Screen.getWidth()/2-(button.getWidth()/2), button.getHeight() + 20f);
		transitionIn();
				
		console.setPosition(50, Screen.getHeight()-100);
		console.setHeight(Screen.getHeight()-200);
		console.setWidth(Screen.getWidth()-100);
		console.setLayer(RenderingSystem.DRAW_LAYER.FOREGROUND0);		
		
		scrollDownButton.downSprite.setColor(1,0,0,1);
		scrollDownButton.setPosition(console.getPosition().x + console.getWidth() - scrollDownButton.getWidth(), console.getPosition().y - console.getHeight() + scrollDownButton.getHeight());
		scrollDownButton.setRotationAroundCentre(180f);
		scrollDownButton.setLayer(RenderingSystem.DRAW_LAYER.FOREGROUND1);
		
		scrollUpButton.downSprite.setColor(1,0,0,1);
		scrollUpButton.setPosition(console.getPosition().x + console.getWidth() - scrollUpButton.getWidth(), console.getPosition().y);
		scrollUpButton.setLayer(RenderingSystem.DRAW_LAYER.FOREGROUND1);
	}
	
	@Override
	public void initializeState() {

	}

	@Override
	public void destroyState() {

	}
	
	public void draw(float deltaTime)
	{
		notificationText.setPosition((Screen.getWidth()/2f)-(notificationText.getWidth()/2f), notificationText.getY());
		notificationText.draw(deltaTime);
		button.draw(deltaTime);
		console.draw(deltaTime);
		scrollDownButton.draw(deltaTime);
		scrollUpButton.draw(deltaTime);

		super.draw(deltaTime);
	}
int a = 0;
	public void update(float deltaTime)
	{		
		if(findGamesSent && !gamesReceived)
		{
			for(String message : networkHandler.getServerMessages())
			{
				if(message.contains("games:"))
				{					
					parseGames(message);
					gamesReceived = true;
					findGamesSent = false;
					gamesReceived = false;
					
					if(console.lineCount() > 0)
						notificationText.setText("Games Found");
					else
						notificationText.setText("No Games Found");
				}
			}
		}	
		
		if(Keyboard.isKeyDown(Keyboard.KEY_A))
			console.insertLine(RenderingSystem.generateText("Hello World"+a++));
		
		if(!findGamesSent)
		{
			if(Keyboard.isKeyDown(Keyboard.KEY_RETURN) || (Mouse.isButtonDown(0) && button.clicked(new Point(Mouse.getX(), Mouse.getY()))))
			{
				console.clear();				
				if(networkHandler.connectServer())
				{
					notificationText.setText("Requesting Games from Server...");
					networkHandler.sendServerMessage("findGames");					
					findGamesSent = true;
				}
				else
					notificationText.setText("Failed connecting to server...");
			}
			else if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
			{
				transitionOut(GameState.StateTypes.MAIN_MENU);
				networkHandler.disconnectServer();
				networkHandler.disconnectOpponent();
			}				
		}
		
		String gameSelected = "";
		if(Mouse.isButtonDown(0))
		{
			Point mousePos = new Point(Mouse.getX(), Mouse.getY());
			if(!(gameSelected = console.getStringClicked(mousePos)).equals("") && !joiningGame)
			{
				String[] connParams = gameSelected.split(",");
			
				networkHandler.joinGame(connParams[1], Integer.valueOf(connParams[2]));
				Game.playerDesignation = "player2";
				transitionOut(GameState.StateTypes.PLAY);
				joiningGame = true;
				
			}
			
			if(scrollDownButton.clicked(mousePos))
				console.scrollDown();
			if(scrollUpButton.clicked(mousePos))
				console.scrollUp();
		}		
		super.update(deltaTime);		
	}

	private void parseGames(String message) 
	{
		message = message.replace("games:", "");
		String[] Games = message.split("#");
		
		for(String game : Games)
		{
			if(!game.equals(""))			
				console.insertLine(game);			
		}
		
		Util.DebugLog.writeError("Parsing:" + message);	
	}
}
