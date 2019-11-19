package GameStates;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import GUI.Button;
import GUI.Console;
import GUI.DrawableText;
import Game.Engagement;
import Game.GameBoard;
import Game.GameToken;
import Game.GameToken.TOKEN_TYPE;
import Game.General;
import Game.Hex;
import Game.Infantry;
import Game.Knight;
import Game.Move;
import Game.MoveCollision;
import Game.Player;
import Game.Spearman;
import Network.InitBoardMessage;
import Network.NetworkHandler;
import Network.TurnMessage;
import RenderingSystem.MoveAnimation;
import RenderingSystem.RenderingSystem;
import RenderingSystem.RenderingSystem.DRAW_LAYER;
import Sound.Sound;
import Sound.SoundSystem;
import Util.Color;
import Util.Line;
import Util.Point;
import Util.Screen;
import Util.TimeInterval;
import Util.Vector2;


public class PlayState extends GameState
{
	private GameBoard board;
	private GameToken selectedToken = null;
	private Hex selectedHex = null;
		
	private Button infantryButton;		
	private Button spearmanButton;
	private Button knightButton;
	private Button generalButton;
	private Button startButton;
	private Button submitButton;
	private Button scrollUpButton = new Button(RenderingSystem.generateSprite("arrow"),RenderingSystem.generateSprite("arrow"));
	private Button scrollDownButton = new Button(RenderingSystem.generateSprite("arrow"),RenderingSystem.generateSprite("arrow"));

	enum GAME_PHASE
	{
		TOKEN_PLACEMENT,		
		MOVE_SELECTION,
		RESOLVE_MOVEMENTS,
		GAME_OVER		
	}

	private Player player1 = new Player(GameBoard.STARTING_AREA.BOTTOM);
	private Player player2 = new Player(GameBoard.STARTING_AREA.BOTTOM);
	private Player enemy = player2;
	private Player player = player1;
	
	private DrawableText notificationText = RenderingSystem.generateText("", new Point(Screen.getWidth()/2f, Screen.getHeight()-10f),new Color(1f,1f,1f));
	private TimeInterval textDelay = new TimeInterval(3000);
	private GAME_PHASE gamePhase = GAME_PHASE.TOKEN_PLACEMENT;	
	private Move currentMove = null;
	
	private Sound tokenPlacement = SoundSystem.generateSound("tokenPlacement.wav");
	private TimeInterval keyboardPressedDelay = new TimeInterval(200);
	private ArrayList<Engagement> engagements = new ArrayList<Engagement>();
	private ArrayList<DrawableText> hexCoordinates = new ArrayList<DrawableText>();

	private NetworkHandler networkHandler;
	private int moveSpeed = 1000;
	private boolean gameOverSeen = false;
	private boolean opponentConnected = false;
	
	private Console previousMoves = new Console();
	private int turnCount = 0;
	
	public PlayState(NetworkHandler networkHandler)
	{
		if(Game.Game.playerDesignation.equals("player2"))
		{
			player = player2;			
			enemy = player1;
		}
		else
		{
			player = player1;
			enemy = player2;
		}
		
		this.networkHandler = networkHandler;
		//setBackground(RenderingSystem.generateSprite("lightWood"));
		RenderingSystem.backgroundColor = new Color(0.5f,0.5f,.8f);		
		board = new GameBoard(7,7, player1, player2);	

		previousMoves.setPosition(15f, Screen.getHeight() - 15f);
		previousMoves.setWidth(200f);
		previousMoves.setHeight(600f);
		previousMoves.setColor(0f,0f,0f,.5f);
		previousMoves.setLayer(RenderingSystem.DRAW_LAYER.FOREGROUND0);
		for(Hex hex : board.getHex())
		{
			DrawableText text = RenderingSystem.generateText(hex.getCoordinate(), new Point(hex.getX()-21,hex.getY()+19));
			text.setLayer(RenderingSystem.DRAW_LAYER.FOREGROUND1);
			text.setColor(new Color(.3f,.3f,.3f));
			hexCoordinates.add(text);
		}
		
		infantryButton = new Button(RenderingSystem.generateSprite("infantry_token"));		infantryButton.setColor(0f, 0f, 1f, 1f);
		spearmanButton = new Button(RenderingSystem.generateSprite("InfantryWhite"));		spearmanButton.setColor(0f, 0f, 1f, 1f);		
		knightButton = new Button(RenderingSystem.generateSprite("KnightWhite"));			knightButton.setColor(0f, 0f, 1f, 1f);
		generalButton = new Button(RenderingSystem.generateSprite("KingWhite"));			generalButton.setColor(0f, 0f, 1f, 1f);
		startButton = new Button(RenderingSystem.generateSprite("submitButton"), RenderingSystem.generateSprite("submitButtonPress"));
		submitButton = new Button(RenderingSystem.generateSprite("submitButton"), RenderingSystem.generateSprite("submitButtonPress"));
		
		infantryButton.upSprite.setPosition(new Point(10,Screen.getHeight()-20));
		spearmanButton.upSprite.setPosition(new Point(10,Screen.getHeight()-20));
		knightButton.upSprite.setPosition(new Point(110,Screen.getHeight()-20));
		generalButton.upSprite.setPosition(new Point(210,Screen.getHeight()-20));
		startButton.setWidth(startButton.getWidth()*.75f);	startButton.setHeight(startButton.getHeight()*.75f); startButton.setPosition(new Point(15f, startButton.getHeight()+15f));
		submitButton.setWidth(submitButton.getWidth()*.75f);	submitButton.setHeight(submitButton.getHeight()*.75f); submitButton.setPosition(new Point(15f, submitButton.getHeight()+15f));
		submitButton.setVisible(false);
		
		drawables.add(infantryButton);
		drawables.add(spearmanButton);
		drawables.add(knightButton);
		drawables.add(generalButton);
		drawables.add(startButton);
		drawables.add(submitButton);
		
		notificationText.setLayer(DRAW_LAYER.FOREGROUND2);
		notificationText.setText("Waiting for opponent!");
		
		scrollDownButton.downSprite.setColor(1,0,0,1);
		scrollDownButton.setPosition(previousMoves.getPosition().x + previousMoves.getWidth() - scrollDownButton.getWidth(), previousMoves.getPosition().y - previousMoves.getHeight() + scrollDownButton.getHeight());
		scrollDownButton.setRotationAroundCentre(180f);
		scrollDownButton.setLayer(RenderingSystem.DRAW_LAYER.FOREGROUND1);
		
		scrollUpButton.downSprite.setColor(1,0,0,1);
		scrollUpButton.setPosition(previousMoves.getPosition().x + previousMoves.getWidth() - scrollUpButton.getWidth(), previousMoves.getPosition().y);
		scrollUpButton.setLayer(RenderingSystem.DRAW_LAYER.FOREGROUND1);
		
		board.highlightArea(player.STARTING_AREA);
		determineVisibleButtons();
		super.transitionIn();		
	}
		
	private void addTurnNumber() 
	{
		DrawableText turnText = RenderingSystem.generateText("Turn "+turnCount ++);
		turnText.setColor(new Color(1f,1f,1f));
		previousMoves.insertLine(turnText);		
	}

	@Override
	public void update(float gameTime)
	{		
		super.update(gameTime);		
		if(!transitionedOut())
		{
			if(networkHandler.isOpponentConnected() && !opponentConnected)
			{
				opponentConnected = true;
				notificationText.setText("Opponent joined!");
			}
			else if(networkHandler.isOpponentConnected() && player.getReady())
			{			
				for(String message : networkHandler.getOpponentMessages())
				{
					System.out.println(message);
					if(!enemy.getReady() && message.contains("tokenPlacement"))
					{
						notificationText.setText("Opponent has place their tokens!");
						try {
							initEnemyTokens(message);
							player.clearMoves();
						} catch (Exception e) {
							e.printStackTrace();
						}
						enemy.setReady(true);
					}
					else if(!enemy.getReady() && message.contains("move:"))
					{											
						try {
							moveEnemyTokens(message);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						enemy.setReady(true);
					}
				}
			}
						
			if(opponentConnected && !networkHandler.isOpponentConnected())
			{
				notificationText.setText("Opponent has disconnected!");
			}
			
			Point mousePos = new Point(Mouse.getX(), Mouse.getY());
			
			//Make sure the game is not transition in our out before allowing the player to do things
			if(!transitioningIn() && !transitioningOut())
			{
				board.unhighlightHex();
				while (Keyboard.next())
				{
					if (Keyboard.getEventKeyState())
					{
						if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE)
							transitionOut(GameState.StateTypes.MAIN_MENU);//gamePhase = GAME_PHASE.GAME_OVER;
						if (Keyboard.getEventKey() == Keyboard.KEY_F1 && keyboardPressedDelay.elapsed())
							board.setDrawHexCoordinates(!board.getDrawHexCoordinates());
					}
				}
				
				//Highlight all the moves currently in play.
				for(Move move: player.getMoves())
				{
					for(Hex hex : move.getPath())
						board.highlightHex(hex, new Color(.5f,0f,0f));									
				}
				if(currentMove != null)
				{
					for(Hex hex : currentMove.getPath())
						board.highlightHex(hex, new Color(.5f,0f,0f));													
				}

				//calculate the engagements
				Player enemy = player == player1 ? player2 : player1;
				engagements.clear();
				for(GameToken token : player.getTokens())
				{
					for(GameToken enemyToken : enemy.getTokens())
					{
						if(token.currentHex.getNeighbours().contains(enemyToken.currentHex))
						{
							Engagement engagement = new Engagement(token, enemyToken, RenderingSystem.generateSprite("arrow"));
							engagements.add(engagement);	
																				
							Line line = new Line(token.currentHex.getPosition(), enemyToken.currentHex.getPosition());
							Vector2 up = new Vector2(0f,1f);
							Vector2 dir = new Vector2(Point.sub(enemyToken.currentHex.getPosition(), token.currentHex.getPosition()));
							
							dir.toUnit();
							
							engagement.sprite.setPosition(line.getMidPoint().x - engagement.sprite.getWidth()/2f, line.getMidPoint().y + engagement.sprite.getHeight()/2f);
							engagement.sprite.setRotationAroundCentre((float)dir.angleBetweenDeg(up));				
						}
					}
				}			
			
				//If the player has a token selected for placement, make the token follow the cursor
				if(selectedToken != null)
					selectedToken.sprite.setPosition(mousePos.x-selectedToken.sprite.getWidth()/2f, mousePos.y+selectedToken.sprite.getHeight()/2f);
						
				switch(gamePhase)
				{
				case TOKEN_PLACEMENT:
					
					if(networkHandler.isOpponentConnected() && networkHandler.isServerConnected())
						networkHandler.disconnectServer();
					
					board.highlightArea(player.STARTING_AREA);
					determineVisibleButtons();
					handleMouseClicks();
					
					if(enemy.getReady() && player.getReady())
					{
						gamePhase = GAME_PHASE.MOVE_SELECTION;
						startButton.upSprite.setVisible(false);
						infantryButton.upSprite.setVisible(false);		
						spearmanButton.upSprite.setVisible(false);
						knightButton.upSprite.setVisible(false);
						generalButton.upSprite.setVisible(false);							
						submitButton.upSprite.setVisible(true);
						submitButton.clickRate.resetInterval();
						
						for(GameToken token : player1.getTokens())
							token.startingHex = token.currentHex;
						for(GameToken token : player2.getTokens())
							token.startingHex = token.currentHex;

						player.setReady(false);
						enemy.setReady(false);
						
						notificationText.setText("Please place all of your tokens");
					}
					
					break;
				case MOVE_SELECTION:
					
					if(enemy.getReady() && player.getReady())
					{
						player.setReady(false);
						enemy.setReady(false);					
						resolveMoves();

						if((player1.getTokens(TOKEN_TYPE.GENERAL).size() == 0 && player2.getTokens(TOKEN_TYPE.GENERAL).size() == 0) || (player1.getTokens().size() == 1 && player2.getTokens().size() == 1)) //draw if: both have 1 token, both general's are dead
						{
							Util.DebugLog.writeError("DRAW!");
							notificationText.setText("DRAW!");
							gamePhase = GAME_PHASE.GAME_OVER;
						}	
						else if(!board.canMoveToken(player) && !board.canMoveToken(enemy)) //draw if: no one can move
						{
							Util.DebugLog.writeError("DRAW!");
							notificationText.setText("DRAW!");
							gamePhase = GAME_PHASE.GAME_OVER;
						}
						else if(enemy.getTokens(TOKEN_TYPE.GENERAL).size() == 0 || (enemy.getTokens().size() == 1) || !board.canMoveToken(enemy)) //you win if: enemy's general is dead, enemy only has 1 token, enemy can't make any moves
						{
							Util.DebugLog.writeError("YOU WIN!");
							notificationText.setText("YOU WIN!");
							gamePhase = GAME_PHASE.GAME_OVER;
						}
						else if(player.getTokens(TOKEN_TYPE.GENERAL).size() == 0 || (player.getTokens().size() == 1) || !board.canMoveToken(player)) //you lose if: your general is dead, you only have 1 token, you can't make any moves
						{
							Util.DebugLog.writeError("YOU LOSE!");
							notificationText.setText("YOU LOSE!");
							gamePhase = GAME_PHASE.GAME_OVER;
						}
					}
					
					board.highlightHexWithTokens(player.getTokens(),new Color(.5f,.5f,.5f));
					
					if(selectedToken != null && !player.hasMovedToken(selectedToken))
						board.highlightValidHexTargets(selectedToken, new Color(.5f,.5f,.5f));
					else if(player.hasMovedToken(selectedToken))
						board.highlightValidHexTargets(selectedToken, new Color(.5f,.5f,.5f));
					
					handleMouseClicks();
					break;
				case RESOLVE_MOVEMENTS:
					gamePhase = GAME_PHASE.MOVE_SELECTION;
					break;
				case GAME_OVER:
					if(!gameOverSeen)
					{
						Util.DebugLog.writeError("GameOver");
						gameOverSeen = true;
						submitButton.setVisible(false);
					}
					break;		
				}
			}
		}		
	}
		
	private void resolveMoves() 
	{
		//check for intersection of token paths				
		for(Move playerMove : player.getMoves())
		{
			for(Hex playerHex : playerMove.getPath())
			{
				for(Move enemyMove : enemy.getMoves())
				{	
					for(Hex enemyHex : enemyMove.getPath())
					{
						if(playerHex == enemyHex && (enemyHex != enemyMove.getHex(0) && playerHex != playerMove.getHex(0)))
						{
							Hex collisionHex = playerHex;
							
							MoveCollision prevPlayerCollision = playerMove.getClosestCollision();
							MoveCollision prevEnemyCollision = enemyMove.getClosestCollision();
							GameToken playerToken = playerMove.getToken();
							GameToken enemyToken = enemyMove.getToken();
							MoveCollision newCollision = new MoveCollision(playerToken, enemyToken, playerMove.getPath(), enemyMove.getPath(), collisionHex);
							
							if(prevPlayerCollision != null && playerMove.getIndexOf(collisionHex) < playerMove.getIndexOf(prevPlayerCollision.getCollisionHex()))
							{
								enemyMove.setCollisionAt(newCollision, collisionHex);
								playerMove.setCollisionAt(null, prevPlayerCollision.getCollisionHex());
								prevPlayerCollision.getEnemyToken().move.setCollisionAt(null, prevPlayerCollision.getCollisionHex());
							}
							else if(prevPlayerCollision == null)
								enemyMove.setCollisionAt(newCollision, collisionHex);
							
							if(prevEnemyCollision != null && enemyMove.getIndexOf(collisionHex) < enemyMove.getIndexOf(prevEnemyCollision.getCollisionHex()))
							{
								playerMove.setCollisionAt(newCollision, collisionHex);
								enemyMove.setCollisionAt(null, prevEnemyCollision.getCollisionHex());
								prevEnemyCollision.getPlayerToken().move.setCollisionAt(null, prevEnemyCollision.getCollisionHex());
							}								
							else if(prevEnemyCollision == null)
								playerMove.setCollisionAt(newCollision, collisionHex);
						}						
					}
				}
			}
		}
		
		for(Move playerMove : player.getMoves())
		{
			playerMove.getToken().currentHex.removeToken(playerMove.getToken());
			playerMove.getToken().clearHex();			
			
			Hex endHex = playerMove.getValidLastHex();
			Hex collisionHex = playerMove.getFirstCollisionHex();
			GameToken token = playerMove.getToken();
			
			board.placeToken(playerMove.getValidLastHex(), playerMove.getToken());
			playerMove.setEnd(playerMove.getValidLastHex());
			
			//Animate the tokens
			float animationSpeed = collisionHex == null ? moveSpeed / (float)playerMove.getPath().size()-1f : moveSpeed / playerMove.getPath().size();  
			for(int i=0; (i+1) < playerMove.getPath().size(); i++)			
				token.sprite.addAnimation(new MoveAnimation(token.sprite, animationSpeed, playerMove.getPath().get(i).getPosition(token), playerMove.getPath().get(i+1).getPosition(token)));
			if(collisionHex != null)
			{
				token.sprite.addAnimation(new MoveAnimation(token.sprite, animationSpeed, endHex.getPosition(token), new Point(collisionHex.getPosition().x-token.sprite.getWidth()/2f,collisionHex.getPosition().y+token.sprite.getHeight()/2f)));
				token.sprite.addAnimation(new MoveAnimation(token.sprite, animationSpeed, new Point(collisionHex.getPosition().x-token.sprite.getWidth()/2f,collisionHex.getPosition().y+token.sprite.getHeight()/2f), endHex.getPosition(token)));
			}
		}
		
		for(Move enemyMove : enemy.getMoves())
		{			
			enemyMove.getToken().currentHex.removeToken(enemyMove.getToken());
			enemyMove.getToken().clearHex();			
			
			Hex endHex = enemyMove.getValidLastHex();
			Hex collisionHex = enemyMove.getFirstCollisionHex();
			GameToken token = enemyMove.getToken();
			
			board.placeToken(endHex, enemyMove.getToken());
			enemyMove.setEnd(endHex);
						
			//Animate the tokens
			float animationSpeed = collisionHex == null ? moveSpeed / (float)enemyMove.getPath().size()-1f : moveSpeed / enemyMove.getPath().size();  
			for(int i=0; (i+1) < enemyMove.getPath().size(); i++)
			{
				token.sprite.addAnimation(new MoveAnimation(token.sprite, animationSpeed, enemyMove.getPath().get(i).getPosition(token), enemyMove.getPath().get(i+1).getPosition(token)));
			}
			
			if(collisionHex != null)
			{
				token.sprite.addAnimation(new MoveAnimation(token.sprite, animationSpeed, endHex.getPosition(token), new Point(collisionHex.getPosition().x-token.sprite.getWidth()/2f,collisionHex.getPosition().y+token.sprite.getHeight()/2f)));
				token.sprite.addAnimation(new MoveAnimation(token.sprite, animationSpeed, new Point(collisionHex.getPosition().x-token.sprite.getWidth()/2f,collisionHex.getPosition().y+token.sprite.getHeight()/2f), endHex.getPosition(token)));
			}
		}
		
		addTurnNumber();				
		for(Move move : player.getMoves())
			addMoveText(move, new Color(.3f,.3f,1f));					
		for(Move move : enemy.getMoves())
			addMoveText(move, new Color(1f,.3f,.3f));
		
		// resolve tokens taking each other
		// If a hex has two tokens then the token that moved onto that hex should be the attacker
		// thus we delete the victim token.
		top: for(GameToken playerToken : player.getTokens())
		{
			if(playerToken.currentHex.hasTwoTokens())
			{
				GameToken enemyToken = playerToken.currentHex.getToken(enemy);
				GameToken deadToken = null;
				if(player.hasMovedToken(playerToken))
				{
					deadToken = enemyToken;
					deadToken.currentHex.removeToken(deadToken);	
					enemy.removeToken(deadToken);

					Util.DebugLog.writeError("enemy token deleted: "+deadToken.getType());
				}
				else if(enemy.hasMovedToken(enemyToken))
				{
					deadToken = playerToken;
					deadToken.currentHex.removeToken(deadToken);	
					player.removeToken(deadToken);		
					
					Util.DebugLog.writeError("player token deleted: "+deadToken.getType());
				}		
				else
					throw new RuntimeException("wtf");

				continue top;				
			}
		}
		
		//Set the tokens starting positions
		for(GameToken token : player1.getTokens())
			token.startingHex = token.currentHex;
		for(GameToken token : player2.getTokens())
			token.startingHex = token.currentHex;
		
		engagements.clear();
		player.clearMoves();
		enemy.clearMoves();
	}

	private void initEnemyTokens(String message) throws Exception 
	{
		if(message.contains("tokenPlacement:"))
		{
			message = message.replace("tokenPlacement:", "");
									
			if(message.contains("#"))
			{
				String[] tokenPairs = message.split("#");
				for(String tokenPair : tokenPairs)
				{
					String[] parsed = tokenPair.split(",");
					
					String tokenType = parsed[0];
					String hexCoordinate = parsed[1];
					GameToken token = null;
					
					switch(tokenType)
					{
					case "INFANTRY":
						token = new Infantry();
						break;
					case "SPEARMAN":
						token = new Spearman();
						break;
					case "KNIGHT":
						token = new Knight();
						break;
					case "GENERAL":
						token = new General();
						break;
					default:
						Util.DebugLog.writeError("Invalid tokenType:"+tokenType);
					}
					token.sprite.setColor(1f, 0f, 0f, 1f);
					board.placeToken(hexCoordinate, token);
					
					if(!enemy.addToken(token))
						Util.DebugLog.writeError("can't add another token: "+message);
				}				
			}
			else if(message.contains(","))
			{
				String[] parsed = message.split(",");
				
				String tokenType = parsed[0];
				String hexCoordinate = parsed[1];
				GameToken token = null;
				
				switch(tokenType)
				{
				case "INFANTRY":
					token = new Infantry();					
					break;
				case "SPEARMAN":
					token = new Spearman();
					break;
				case "KNIGHT":
					token = new Knight();
					break;
				case "GENERAL":
					token = new General();
					break;
				default:
					Util.DebugLog.writeError("Invalid tokenType:"+tokenType);
				}
				token.sprite.setColor(1f, 0f, 0f, 1f);
				board.placeToken(hexCoordinate, token);
				
				if(!enemy.addToken(token))
					Util.DebugLog.writeError("can't add another token: "+message); 
			}
		}
		else
			throw new Exception("Invalid token placement string: " + message);		
	}

	private void moveEnemyTokens(String message) throws Exception 
	{
		if(message.contains("move:"))
		{
			String[] moveStrings = message.split("move:");
			
			for(int i=1; i<moveStrings.length; i++)
			{				
				String[] moveCoordinates = moveStrings[i].split(","); 	
				Move move = new Move(moveCoordinates, enemy, board);		
				enemy.addMove(move);
			}		
			
			enemy.setReady(true);
		}
		else
			throw new Exception("Invalid move string: " + message);		
	}
	
	private void addMoveText(Move move, Color color) 
	{ 
		String moveString = move.getToken().getType().name().substring(0, 1) + " ";
		ArrayList<Hex> path = move.getPath();
		
		for(int i=0; i<path.size(); i++)		
			moveString += ">"+path.get(i).getCoordinate();
		
		DrawableText moveText = RenderingSystem.generateText(moveString);
		moveText.setColor(color);
		previousMoves.insertLine(moveText);		
	}

	@SuppressWarnings("unused")
	private Engagement getEngagement(GameToken token, GameToken enemyToken)
	{
		for(Engagement  engagement : engagements)
		{
			if(engagement.containsToken(token) && engagement.containsToken(enemyToken) && token != enemyToken)			
				if(engagement.containsHex(token.currentHex) && engagement.containsHex(enemyToken.currentHex) && token.currentHex != enemyToken.currentHex)
					return engagement;			
		}
		return null;
	}

	public void handleMouseClicks()
	{
		Point mousePos = new Point(Mouse.getX(), Mouse.getY()); 
		boolean leftMousePressed = 	Mouse.isButtonDown(0);
		boolean buttonPressed = false;
		Hex currentHex = null;
		
		if(!player.getReady() && leftMousePressed && !(buttonPressed = updateButtons(mousePos)) && ((currentHex = board.getHexAt(mousePos)) != null))
		{
			board.highlightHex(currentHex, new Color(0.3f,0.3f,1.f));									
			if(selectedToken == null && currentHex.hasToken() && currentHex.getToken(player) != null)
			{
				selectedHex = currentHex;
				selectedToken = currentHex.getToken(player);
				currentHex.clearToken(player);

				if(gamePhase == PlayState.GAME_PHASE.MOVE_SELECTION && currentMove == null && !player.hasMovedToken(selectedToken))
				{						
					currentMove = new Move(selectedHex, selectedToken);
					selectedToken.move = currentMove;
				}
				else if(gamePhase == PlayState.GAME_PHASE.MOVE_SELECTION && currentMove == null && player.hasMovedToken(selectedToken))					
					currentMove = player.getMove(selectedToken);										
			}
			
			if(currentMove != null && currentMove.peekPath() != currentHex)
				currentMove.pushPath(currentHex);			
		}
		else
		{
			currentHex = board.getHexAt(mousePos);
		}
		
		switch(gamePhase)
		{
		case TOKEN_PLACEMENT:
			if(leftMousePressed)		
				break;
			
			//Add a token if there is a token selected and released over a hex and that token type 
			if(currentHex != null && selectedToken != null && board.hexInArea(currentHex,player.STARTING_AREA))		
			{
				if(!currentHex.hasToken()) //add token to empty hex 	
				{
					player.addToken(selectedToken);
					board.placeToken(currentHex, selectedToken);
				}
				else if(selectedHex != null) //swap tokens on two different hex
				{
					player.addToken(selectedToken);
					GameToken currentToken = currentHex.getToken(player);
					
					selectedHex.clearTokens();
					currentHex.clearTokens();
					
					board.placeToken(selectedHex, currentToken);
					board.placeToken(currentHex, selectedToken);
				}		
				else //overwrite a token
				{
					player.addToken(selectedToken);										
					player.removeToken(currentHex.getToken(player));
					currentHex.clearTokens();					
					board.placeToken(currentHex, selectedToken);
				}
				SoundSystem.playSound(tokenPlacement);
				selectedToken = null;
				selectedHex = null;
			}
			else if(selectedToken != null) //token was placed invalidly so it is deleted
			{
				player.removeToken(selectedToken);
				selectedToken = null;
				selectedHex = null;
			}
			
			break;
		case MOVE_SELECTION:
			if(leftMousePressed || buttonPressed) // mouse released			
				break;
		
			if(isCurrentMovePossible(currentHex)) // a move is possible					
			{				
				if(!player.hasMovedToken(selectedToken)) //token never moved before
				{
					if(currentHex != selectedHex && !player.hasMovedToken(selectedToken))
						player.addMove(currentMove);										
					currentHex.setToken(selectedToken);				
				}
				else if(player.getMove(selectedToken).getSourceHex() == currentHex) //token moved to its orig position
				{
					Move move = player.getMove(selectedToken); 
					move.getSourceHex().setToken(selectedToken);
					player.removeMove(move);
				}
				else //moved hex is moved again
				{					
					currentHex.setToken(selectedToken);						
				}
				SoundSystem.playSound(tokenPlacement);
			}
			else if(selectedToken != null) //move not possible, reset token
			{
				selectedToken.startingHex.setToken(selectedToken);
				player.removeMove(selectedToken.move);
				selectedToken.move = null;
			}
			selectedToken = null;
			selectedHex = null;	
			currentMove = null;
			
			break;
		case RESOLVE_MOVEMENTS:
			gamePhase = GAME_PHASE.MOVE_SELECTION;
			break;
		case GAME_OVER:
			break;
		
		}
	}

	private void determineVisibleButtons()
	{
			if(player.maxReached(GameToken.TOKEN_TYPE.GENERAL))
				generalButton.upSprite.setVisible(false);
			else
				generalButton.upSprite.setVisible(true);
				
			if(player.maxReached(GameToken.TOKEN_TYPE.INFANTRY))
				infantryButton.upSprite.setVisible(false);
			else
				infantryButton.upSprite.setVisible(true);
				
			if(player.maxReached(GameToken.TOKEN_TYPE.KNIGHT))
				knightButton.upSprite.setVisible(false);
			else
				knightButton.upSprite.setVisible(true);
			
			if(player.maxReached(GameToken.TOKEN_TYPE.SPEARMAN))		
				spearmanButton.upSprite.setVisible(false);
			else
				spearmanButton.upSprite.setVisible(true);										
	}
	
	public boolean updateButtons(Point mousePos)
	{		
		boolean buttonPressed = false;
		boolean startButtonClicked = startButton.clicked(mousePos);
		switch(gamePhase)
		{
			case TOKEN_PLACEMENT:
				if(startButtonClicked && networkHandler.isOpponentConnected() && player.allTokensPlaced())
				{
					player.setReady(true);
					networkHandler.sendOpponentMessage(new InitBoardMessage(player.getTokens()));
					textDelay.resetInterval();
					notificationText.setText("Sending Token Positions");
					buttonPressed = true;
				}
				else if(startButtonClicked)
				{
					textDelay.resetInterval();
							
				}
				
				if(infantryButton.clicked(mousePos) && selectedToken == null && infantryButton.upSprite.getVisible() == true)
				{
					selectedToken = new Infantry();					
					buttonPressed = true;
				}
				else if(spearmanButton.clicked(mousePos) && selectedToken == null && spearmanButton.upSprite.getVisible() == true)
				{
					selectedToken = new Spearman();
					buttonPressed = true;
				}
				else if(knightButton.clicked(mousePos) && selectedToken == null && knightButton.upSprite.getVisible() == true)
				{ 
					selectedToken = new Knight();					
					buttonPressed = true;
				}
				else if(generalButton.clicked(mousePos) && selectedToken == null && generalButton.upSprite.getVisible() == true)
				{
					selectedToken = new General();
					buttonPressed = true;
				}
				//position the new token properly
				if(buttonPressed && selectedToken != null)
				{
					selectedToken.sprite.setPosition(mousePos.x - selectedToken.sprite.getWidth()/2f, mousePos.y + selectedToken.sprite.getHeight()/2f);
					selectedToken.sprite.setColor(0f,0f,1f,1f);
				}
				
				break;
			case MOVE_SELECTION:					
				if(scrollDownButton.clicked(mousePos))
					previousMoves.scrollDown();
				else if(scrollUpButton.clicked(mousePos))
					previousMoves.scrollUp();				
				else if(!player.getReady() && submitButton.clicked(mousePos) && player.getMoves().size() > 0)
				{
					sendMoves();
					buttonPressed = true;
				}
				else if(player.getMoves().size() == 0)
				{
					notificationText.setText("Place all your tokens");
				}
				
				break;
			case RESOLVE_MOVEMENTS:
				break;
			case GAME_OVER:
				break;
		}			
		return buttonPressed;		
	}	
	
	public boolean isCurrentMovePossible(Hex currentHex)
	{	
		// a token has been selected a source and destination hex have been indicated
		if(selectedToken != null && currentHex != null && selectedHex != null) 
		{
			//token has not been moved before, the player can still make moves, token is in range
			if(board.validateMove(currentMove) && (player.hasMovesRemaining() || player.containsMove(currentMove)) && currentMove.pathEndsWith(currentHex)) 
				return true;
		}

		return false;
	}
		
	public void sendMoves()
	{
		player.setReady(true);
		networkHandler.sendOpponentMessage(new TurnMessage(player.getMoves()));
		
		textDelay.resetInterval();
		notificationText.setText("Moves sent");		
	
		currentMove = null;
		
		for(GameToken token : player.getTokens())
			token.startingHex = token.currentHex; 
	}

	public boolean isGameOver()
	{
		if(player1.getTokens(TOKEN_TYPE.GENERAL).size() == 0 || player2.getTokens(TOKEN_TYPE.GENERAL).size() == 0)		
			return true;		
		else
			return false;
	}
	
	
	@Override
	public void destroyState() 
	{
		if(networkHandler.isOpponentConnected())
			networkHandler.disconnectOpponent();
		
		if(networkHandler.isServerConnected())
		{
			networkHandler.sendServerMessage("exit");
			networkHandler.disconnectServer();
		}
		
		Game.Game.playerDesignation = "";
		
		board.destroy();
		engagements.clear();
		drawables.clear();
	}

	@Override
	public void initializeState() {
		
	}

	@Override
	public void draw(float deltaTime) 
	{
		super.draw(deltaTime);
		notificationText.draw(deltaTime);
		if(!transitionedOut())
		{
			notificationText.setPosition((Screen.getWidth()/2f) - (notificationText.getWidth()/2f), notificationText.getY());
			if(board.getDrawHexCoordinates())
			{			
				for(DrawableText text : hexCoordinates)
				{
					if(board.getHexAt(text.toString()).getToken(player) == null && board.getHexAt(text.toString()).getToken(enemy) == null)
						text.draw(deltaTime);
				}
			}
			
			infantryButton.draw(deltaTime);		
			spearmanButton.draw(deltaTime);
			knightButton.draw(deltaTime);
			generalButton.draw(deltaTime);
			startButton.draw(deltaTime);
			submitButton.draw(deltaTime);
			board.draw(deltaTime);
			
			if(gamePhase == GAME_PHASE.MOVE_SELECTION)
			{
				previousMoves.draw(deltaTime);
				scrollDownButton.draw(deltaTime);
				scrollUpButton.draw(deltaTime);
			}
			
			for(GameToken token : player1.getTokens())
				token.sprite.draw(deltaTime);
			for(GameToken token : player2.getTokens())
				token.sprite.draw(deltaTime);
					
			if(selectedToken != null)
				selectedToken.sprite.draw(deltaTime);	
			
			for(Engagement engagement : engagements)
			{
				engagement.sprite.draw(deltaTime);
			}
		}
	}
}
