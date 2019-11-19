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

public class NewGameState extends GameState {

	DrawableText notificationText = null;
	DrawableText enterName = null;
	DrawableText enterPort = null;
	NetworkHandler networkHandler = null;
	Button button = new Button(RenderingSystem.generateSprite("startButton"),RenderingSystem.generateSprite("startButtonPress"));
	TextBox gameName = new TextBox(new Point(Screen.getWidth() * .5f, Screen.getHeight() - 150), 300);
	TextBox gamePort = new TextBox(new Point(Screen.getWidth() * .5f, Screen.getHeight() - 250), 300);
	TextBox selectedTextBox = gameName;
	boolean newGameSent = false;
	boolean newGameConfirmed = false;
	
	public NewGameState(NetworkHandler networkHandler)
	{		
		RenderingSystem.backgroundColor = new Color(0.5f,0.5f,.8f);	
		this.networkHandler = networkHandler;
		notificationText = RenderingSystem.generateText("New Game", new Point(0,Screen.getHeight()-50f));
		notificationText.setPosition((Screen.getWidth()/2f)-(notificationText.getWidth()/2f), notificationText.getY());
		enterName = RenderingSystem.generateText("Enter Game Name:", new Point(Screen.getWidth() * .30f,Screen.getHeight() - 150));
		enterPort = RenderingSystem.generateText("Enter Game Port:", new Point(Screen.getWidth() * .30f,Screen.getHeight() - 250));
		gameName.appendText("Game Name");		
		gameName.regexFilter = "[^a-zA-Z0-9 ]";
		
		gamePort.setCharLimit(5);
		gamePort.appendText("49152");
		gamePort.regexFilter = "[^0-9]";
		
		button.setPosition(Screen.getWidth()/2-(button.getWidth()/2), button.getHeight() + 20f);
		
		gameName.selected = true;
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
		notificationText.setPosition((Screen.getWidth()/2f)-(notificationText.getWidth()/2f), notificationText.getY());
		notificationText.draw(deltaTime);
		button.draw(deltaTime);
		
		gameName.draw(deltaTime);
		enterName.draw(deltaTime);
				
		gamePort.draw(deltaTime);
		enterPort.draw(deltaTime);
		
		super.draw(deltaTime);
	}

	public void update(float deltaTime)
	{
		String keyPressString = KeyboardWrapper.getPressed();
		selectedTextBox.appendText(keyPressString);
		
//		if(newGameSent && !newGameConfirmed)
//		{
//			title.setText("Waiting for server...");
//			for(String message : networkHandler.getServerMessages())
//			{
//				if(message.equals("server received game"));
//				{
//					newGameConfirmed = true;
//					title.setText("Waiting for opponent...");
//					Game.playerDesignation = "player1";
//					networkHandler.acceptConnection();					
//					transitionOut(GameState.StateTypes.PLAY);	
//				}
//			}
//		}	
		
		if(newGameSent  && !super.transitioning())
		{
			notificationText.setText("Starting Game...");
			try {	Thread.sleep((long) 300);	} catch (InterruptedException e) {	e.printStackTrace();	}		
			transitionOut(GameState.StateTypes.PLAY);	
			Game.Game.playerDesignation = "player1";
		}
		
		if(!newGameSent)
		{
			if(Keyboard.isKeyDown(Keyboard.KEY_RETURN) || (Mouse.isButtonDown(0) && button.clicked(new Point(Mouse.getX(), Mouse.getY()))))
			{
//				if(networkHandler.connectServer())
//				{					
//					networkHandler.bindListenerSocket();					
//					networkHandler.sendServerMessage("newGame:" + gameName.getText().replace(":", "::") + ":" + NetworkHandler.getIP() + ":" + NetworkHandler.getPort());					
//					newGameSent = true;
//				}
//				else
//					title.setText("Failed connecting to server...");
				
				if(gameName.getText().length() == 0)
				{
					notificationText.setText("Game name must be one or more letters");
				}
				else if(gamePort.getText().length() > 0)
				{
					int port = 0;
					port = Integer.valueOf(gamePort.getText());
					if(port > 65535 || port < 0)
						notificationText.setText("Port out of range. try [0-65535]");
					else
					{			
						if(networkHandler.acceptConnection(Integer.valueOf(gamePort.getText())))
						{
							newGameSent = true;
							notificationText.setText("Creating game");
						}
						else
							notificationText.setText("Unable to create game \""+gameName.getText()+"\" on port \""+port+"\"");
					}
				}
				else
					notificationText.setText("Invalid Port. try [0-65535]");
			}
			else if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
			{
				transitionOut(GameState.StateTypes.MAIN_MENU);
				Game.Game.playerDesignation = "";
				networkHandler.disconnectServer();
				networkHandler.disconnectOpponent();
			}
			else if(Mouse.isButtonDown(0) && gameName.clicked(new Point(Mouse.getX(), Mouse.getY())))
			{
				gameName.selected = true;
				gamePort.selected = false;
				selectedTextBox = gameName;
			}
			else if(Mouse.isButtonDown(0) && gamePort.clicked(new Point(Mouse.getX(), Mouse.getY())))
			{
				gameName.selected = false;
				gamePort.selected = true;
				selectedTextBox = gamePort;
			}
		}		
		super.update(deltaTime);		
	}
}
