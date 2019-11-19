package GameStates;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import GUI.Button;
import GUI.DrawableText;
import GUI.TextBox;
import Input.KeyboardWrapper;
import Network.NetworkHandler;
import RenderingSystem.RenderingSystem;
import Util.Color;
import Util.Point;
import Util.Screen;
import Util.TimeInterval;

public class JoinGameState extends GameState
{
	private DrawableText notificationText = null;
	private NetworkHandler networkHandler = null;
	private Button button = new Button(RenderingSystem.generateSprite("joinGameButton"),RenderingSystem.generateSprite("joinGameButtonPress"));
	private boolean joiningGame = false;
	private int dotCount = 0;
	private int dotMax = 5;
	
	DrawableText enterIP = null;
	DrawableText enterPort = null;
	
	TextBox gameIP = new TextBox(new Point(Screen.getWidth() * .5f, Screen.getHeight() - 150), 300);
	TextBox gamePort = new TextBox(new Point(Screen.getWidth() * .5f, Screen.getHeight() - 250), 300);
	TextBox selectedTextBox = gameIP;

	TimeInterval dotDelay = new TimeInterval(100);
	
	public JoinGameState(NetworkHandler networkHandler)
	{				
		//setBackground(RenderingSystem.generateSprite("lightWood"));
		RenderingSystem.backgroundColor = new Color(0.5f,0.5f,.8f);	
		enterIP = RenderingSystem.generateText("Enter Game IP:", new Point(Screen.getWidth() * .30f,Screen.getHeight() - 150));
		enterPort = RenderingSystem.generateText("Enter Game Port:", new Point(Screen.getWidth() * .30f,Screen.getHeight() - 250));
		gameIP.appendText("localhost");		
		gameIP.regexFilter = "[^a-zA-Z0-9.]";
		gameIP.selected = true;
		gameIP.setCharLimit(15);
		
		gamePort.appendText("49152");
		gamePort.setCharLimit(5);
		gamePort.regexFilter = "[^0-9]";
		
		this.networkHandler = networkHandler;
		notificationText = RenderingSystem.generateText("Join Game", new Point(0,Screen.getHeight()-50f));
		notificationText.setPosition((Screen.getWidth()/2f)-(notificationText.getWidth()/2f), notificationText.getY());	
		button.setPosition(Screen.getWidth()/2-(button.getWidth()/2), button.getHeight() + 20f);
		
		transitionIn();
	}
	
	@Override
	public void initializeState() {

	}

	@Override
	public void destroyState() {

	}
	
	public void draw(float deltaTime)
	{		
		notificationText.draw(deltaTime);
		button.draw(deltaTime);

		enterIP.draw(deltaTime);
		enterPort.draw(deltaTime);
		
		gameIP.draw(deltaTime);
		gamePort.draw(deltaTime);
				
		super.draw(deltaTime);
	}
	
	public void update(float deltaTime)
	{		
		notificationText.setPosition((Screen.getWidth()/2f)-(notificationText.getWidth()/2f), notificationText.getY());
		
		String keyPressString = KeyboardWrapper.getPressed();
		selectedTextBox.appendText(keyPressString);
		
		Point mousePos = new Point(Mouse.getX(), Mouse.getY());	
		if(!joiningGame && ((Mouse.isButtonDown(0) && button.clicked(mousePos)) || Keyboard.isKeyDown(Keyboard.KEY_RETURN)))
		{	
			int port = 0;
			if(gamePort.getText().length() > 0)
			{
				port = Integer.valueOf(gamePort.getText());
				if(port > 65535 || port < 0)
					notificationText.setText("Port out of range. try [0-65535]");
				else
				{	
					networkHandler.joinGame(gameIP.getText(), Integer.valueOf(gamePort.getText()));
					joiningGame = true;
					notificationText.setText("Joining game...");					
				}
			}
			else
				notificationText.setText("Invalid Port. try [0-65535]");
		}		
		else if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
		{
			transitionOut(GameState.StateTypes.MAIN_MENU);
			Game.Game.playerDesignation = "";
			networkHandler.disconnectOpponent();
		}
		else if(Mouse.isButtonDown(0) && gameIP.clicked(new Point(Mouse.getX(), Mouse.getY())))
		{
			gameIP.selected = true;
			gamePort.selected = false;
			selectedTextBox = gameIP;
		}
		else if(Mouse.isButtonDown(0) && gamePort.clicked(new Point(Mouse.getX(), Mouse.getY())))
		{
			gameIP.selected = false;
			gamePort.selected = true;
			selectedTextBox = gamePort;
		}
		
		if(networkHandler.isOpponentConnecting())
		{
			notificationText.setText("Joining game");
			notificationText.setPosition((Screen.getWidth()/2f)-(notificationText.getWidth()/2f), notificationText.getY());
			
			for(int i=0; i<dotCount; i++)
				notificationText.append(".");
			
			if(dotDelay.elapsed())
				dotCount = (dotCount+1) % dotMax;
							
		}
		else if(joiningGame && !networkHandler.isOpponentConnecting() && !networkHandler.isOpponentConnected())
		{
			notificationText.setText("Failed to join game");	
			joiningGame = false;
			networkHandler.disconnectOpponent();
		}
		if(networkHandler.isOpponentConnected() && joiningGame)
		{
			transitionOut(GameState.StateTypes.PLAY);
			Game.Game.playerDesignation = "player2";
		}
		
		super.update(deltaTime);		
	}
}
