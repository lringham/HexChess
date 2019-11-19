package Game;

import java.util.ArrayList;
import java.util.List;

import RenderingSystem.RenderingSystem;
import Sound.Sound;
import Sound.SoundSystem;
import Util.Color;
import Util.Point;
import Util.Screen;

public class GameBoard
{
	public enum STARTING_AREA
	{
		TOP,
		BOTTOM		
	}
	
	private ArrayList<ArrayList<Hex>> boardTiles = new ArrayList<ArrayList<Hex>>();
	private ArrayList<GameToken> tokens = new ArrayList<GameToken>();
	public ArrayList<Hex> highlightedHex = new ArrayList<Hex>();
	public final float SQRT_3 = (float) Math.sqrt(3);
	
	//board and hex properties
	public final int BOARD_WIDTH;
	public final int BOARD_HEIGHT;
	public final float BOARD_X_OFFSET;
	public final float BOARD_Y_OFFSET;
	public final float HEX_HEIGHT;
	public final float HEX_QUARTER_HEIGHT;
	public final float HEX_HALF_WIDTH;
	private Player player1;
	private Player player2;
	String message = "";
	int numOfHexagons = 0;
	private boolean drawHexCoordinates = true;

	public GameBoard(int boardWidth, int boardHeight, Player player1, Player player2)
	{						
		this.player1 = player1;
		this.player2 = player2;
		
		HEX_QUARTER_HEIGHT = (float)(Screen.getHeight()-50f) / (1f+(3f*(float)boardHeight));
		HEX_HEIGHT = HEX_QUARTER_HEIGHT * 4f; 
		HEX_HALF_WIDTH = SQRT_3 * HEX_QUARTER_HEIGHT; 	// sqrt(3) * quarterHeight. This is used for the verticies x offsets
		
		BOARD_WIDTH = boardWidth;
		BOARD_HEIGHT = boardHeight;
		
		//This fits the board to the center of the window
		BOARD_X_OFFSET = -HEX_HALF_WIDTH*BOARD_WIDTH + HEX_HALF_WIDTH + Screen.getWidth() / 2f;
		BOARD_Y_OFFSET = HEX_HEIGHT / 2f;
		
		int colCoordinate = 1;
		int rowCoordinate = 65;
		
		if(Game.playerDesignation.equals("player1"))
			rowCoordinate = 65;
		else if(Game.playerDesignation.equals("player2"))
			rowCoordinate = 71;
		else
			throw new RuntimeException("invalid player designation");
		
		
		//initialize the game board
		for (int i = 0; i < boardHeight; i++)
		{
			ArrayList<Hex> row = new ArrayList<Hex>();
			
			int rowWidth;
			if (i >= BOARD_HEIGHT / 2)
				rowWidth = boardWidth - (i - BOARD_HEIGHT / 2); //calculate bottom row widths
			else
				rowWidth = (boardWidth - BOARD_HEIGHT / 2) + i; // calculate top row widths

			//offset the row so the hexagons properly fit together
			float rowOffsetX = (boardWidth - rowWidth) * HEX_HALF_WIDTH + BOARD_X_OFFSET;
			float rowOffsetY = BOARD_Y_OFFSET;	
			
			//create the hexagons			
			for(int j=0; j < rowWidth; j++)
			{				
				Hex hex = RenderingSystem.generateHex("silverHex");		
				
				hex.setHeight((int) (HEX_HEIGHT+1f));
				hex.setPosition(new Point(rowOffsetX + j*2*HEX_HALF_WIDTH, rowOffsetY + i*3*HEX_QUARTER_HEIGHT));
				hex.setCoordinates(Character.toString((char) rowCoordinate) + colCoordinate);
				
				colCoordinate++;				
				row.add(hex);
			}
						
			colCoordinate = 1;
			
			if(Game.playerDesignation.equals("player1"))
				rowCoordinate++;
			else if(Game.playerDesignation.equals("player2"))
				rowCoordinate--;
			else
				throw new RuntimeException("invalid player designation");
			
			boardTiles.add(row);
		}
		
		assignNeighbours();
	}
			
	public void addToken(GameToken token, Point position)
	{
		getHexAt(position).setToken(token);
	}
	
	public List<Hex> getHex()
	{
		List<Hex> allHex = new ArrayList<Hex>();
		
		for(ArrayList<Hex> row : boardTiles)
		{
			allHex.addAll(row);			
		}
		
		return allHex;
	}
	
	public Hex getHexAt(Point point)
	{
	    for(ArrayList<Hex> row : boardTiles)
	    {
		    for(Hex hex : row)
		    {    
		    	if(hex.clicked(point))
		    		return hex; 
		    }
	    }	
	    return null;
	}
	
	public ArrayList<Hex> getHexAround(Hex startingHex, int radius)
	{
		ArrayList<ArrayList<Hex>> neighbours = new ArrayList<ArrayList<Hex>>();
		neighbours.add(new ArrayList<Hex>());
		neighbours.add(new ArrayList<Hex>());
		neighbours.add(new ArrayList<Hex>());
		
		if(radius == 0)
			neighbours.get(0).add(startingHex);
		else
		{
			neighbours.get(0).add(startingHex);
			
			for(int i = 0; i < radius; i++)
			{
				//neighbours[0] represents the hex that are being discovered
				//neighbours[1] represents the outer most discovered hex
				//neighbours[2] represents the hex just inside neighbours[1]
				
				// Update seen neighbours that matter
				neighbours.get(2).clear();
				neighbours.get(2).addAll(neighbours.get(1));
				neighbours.get(1).clear();
				neighbours.get(1).addAll(neighbours.get(0));
				neighbours.get(0).clear();				
				
								
				for(Hex neighbour : neighbours.get(1))
				{
					for(Hex testNeighbour : neighbour.getNeighbours())
					{
						// Find all the neighbours that haven't been seen before
						if(!neighbours.get(1).contains(testNeighbour) && !neighbours.get(2).contains(testNeighbour))
							neighbours.get(0).add(testNeighbour);
					}
				}
			}
		}
		
		return neighbours.get(0);
	}
	
	public ArrayList<Hex> getHexInArea(Hex startingHex, int radius)
	{
		ArrayList<Hex> hexInArea = new ArrayList<Hex>();
		ArrayList<Hex> neighbourHex = new ArrayList<Hex>();
		ArrayList<Hex> tempHex = new ArrayList<Hex>();
	
		hexInArea.add(startingHex);
		neighbourHex.addAll(startingHex.getNeighbours());
		
		for(int i = 0; i < radius; i++)
		{			
			for(Hex hex : neighbourHex)
			{
				if(!hexInArea.contains(hex))
				{
					hexInArea.add(hex);
					tempHex.addAll(hex.getNeighbours());
				}
			}
			
			neighbourHex.clear();
			neighbourHex.addAll(tempHex);
			tempHex.clear();
		}
				
		return hexInArea;
	}
	
	public ArrayList<Hex> getValidHexTargets(GameToken token)
	{	
		ArrayList<Hex> validHex = new ArrayList<Hex>();
		ArrayList<Hex> invalidHex = new ArrayList<Hex>();
		validHex.addAll(getHexInArea(token.startingHex, token.movementRange));
		
		Player player = player1.containsToken(token) ? player1 : player2;
		Player enemy = player == player1 ? player2 : player1;


		
		//if you aren't a general and you're engaged by the enemy's general then you cannot move.
		for(GameToken enemyGeneral : enemy.getTokens(GameToken.TOKEN_TYPE.GENERAL))
		{
			if(getEngagedTokens(token).contains(enemyGeneral) && token.type != GameToken.TOKEN_TYPE.GENERAL)
			{
				validHex.clear();
				validHex.add(enemyGeneral.startingHex);
			}
		}
		
		//Remove other hex tiles with allies 
		for(Hex hex : validHex)
		{
			if(hex.hasToken() && !hex.containsToken(token) && hex.getToken(player) != null)			
				invalidHex.add(hex);			
		}
		
		//Remove hex that include a path
		for(Move move : player.getMoves())
		{
			if(move.getToken() != token)
			{
				for(Hex hex : move.getPath())
				{
					if(validHex.contains(hex))
						invalidHex.add(hex);				
				}
			}
		}
				
		//Handle engagements
		for(Hex hex : getEngagedHex(token))
		{
			if(hex.getToken(enemy) != null)
			{
				if(!canTake(token, hex.getToken(enemy)))
				{
					if(!invalidHex.contains(hex))
						invalidHex.add(hex);
				}
				for(Hex adjHex : hex.getNeighbours())
				{
					if(validHex.contains(adjHex) && !invalidHex.contains(adjHex))
						invalidHex.add(adjHex);
				}
			}
		}
		
		
		//Remove hex that contain nonkillable enemies
		for(Hex hex : validHex)
		{
			if(hex.getToken(enemy) != null)
			{
				if(!canTake(token, hex.getToken(enemy)))
					invalidHex.add(hex);
			}
		}
		 
		//remove all invalid hex from validHex
		for(Hex hex : invalidHex)
		{
			if(validHex.contains(hex))
				validHex.remove(hex);
		}		
		
		//remove all the hex that are not in range after other invalid hex are found
		ArrayList<Hex> testHex = new ArrayList<Hex>();
		ArrayList<Hex> neighbours = new ArrayList<Hex>();
		ArrayList<Hex> finalHex = new ArrayList<Hex>();
		
		finalHex.add(token.startingHex);		
		testHex.addAll(token.startingHex.getNeighbours());
		
		for(int i=0;i<token.movementRange;i++)
		{
			for(Hex hex : testHex)
			{
				if(validHex.contains(hex) && !finalHex.contains(hex))
				{
					finalHex.add(hex);
					if(!(hex.getToken(enemy)!=null && canTake(token, hex.getToken(enemy)))) //stop tokens from moving through take-able tokens	
						neighbours.addAll(hex.getNeighbours());
				}
			}
			
			testHex.clear();
			testHex.addAll(neighbours);
			neighbours.clear();
		}
		return finalHex;
	}
	
	public ArrayList<Hex> getInvalidHexTargets(GameToken token)
	{					
		ArrayList<Hex> validHex = new ArrayList<Hex>();	
		
		if(token != null)
		{			
			ArrayList<Hex> invalidHex = getValidHexTargets(token);
			ArrayList<Hex> testHex = new ArrayList<Hex>();
			ArrayList<Hex> neighbours = new ArrayList<Hex>();
			
			validHex.add(token.startingHex);		
			testHex.addAll(token.startingHex.getNeighbours());
			
			for(int i=0;i<token.movementRange;i++)
			{
				for(Hex hex : testHex)
				{
					if(!invalidHex.contains(hex) && !validHex.contains(hex))
					{
						validHex.add(hex);
						neighbours.addAll(hex.getNeighbours());
					}
				}
				
				testHex.clear();
				testHex.addAll(neighbours);
				neighbours.clear();
			}
		}
		return validHex;
	}

	private boolean canTake(GameToken token1, GameToken token2) 
	{
		/**
		 *  If a token is engaged with only one opposing token
		 *  and that opposing token is engaged by multiple tokens
		 *  the single token can capture the opposing token.
		 */
		
		if(getEngagedTokens(token1).size() <= 1 && (getEngagedTokens(token2).size() >= 2 || (getEngagedTokens(token2).size() == 1 && !getEngagedTokens(token2).contains(token1))))		
			return true;
		else
			return false;
	}

	public void unhighlightHex()
	{
		for(Hex hex : highlightedHex)
			hex.setColor(1f,1f,1f,1f);
		highlightedHex.clear();
	}
	
	public void highlightArea(STARTING_AREA area)
	{
		ArrayList<Hex> tiles = new ArrayList<Hex>();
		
		if(area == STARTING_AREA.BOTTOM)
		{
			tiles.addAll(boardTiles.get(0));
			tiles.addAll(boardTiles.get(1));
		}
		else if(area == STARTING_AREA.TOP)
		{
			tiles.addAll(boardTiles.get(boardTiles.size()-1));
			tiles.addAll(boardTiles.get(boardTiles.size()-2));			
		}
		
		for(Hex hex : tiles)
		{
			highlightHex(hex, new Color(.5f,.5f,.5f));
		}
	}

	public void highlightHex(Point location, Color color)
	{					
		for(ArrayList<Hex> row : boardTiles)
		{
			for(Hex hex : row)
			{
				if(hex.clicked(location))
					highlightHex(hex, color);					
			}
		}
	}
	
	public void highlightHex(Hex hex, Color color)
	{				
		if(!highlightedHex.contains(hex))
		{
			highlightedHex.add(hex);
			hex.setColor(color);
		}
	}
	
	public void highlightHexAround(Hex startingHex,int radius, Color color)
	{			
		ArrayList<Hex> neighbours = new ArrayList<Hex>();
		
		for(int i=1;i<=radius;i++)
			neighbours.addAll(getHexAround(startingHex, i));
		
		for(Hex hex : neighbours)
			highlightHex(hex, color);		
	}
	
	public void highlightValidHexTargets(GameToken token, Color color)
	{
		ArrayList<Hex> validHex = getValidHexTargets(token); 

		for(Hex hex : validHex)
			highlightHex(hex, color);
	}
	
	public void highlightHexWithTokens(ArrayList<GameToken> tokens, Color color)
	{					
		for(GameToken token : tokens)
		{
			highlightHex(token.currentHex, color);
		}
	}
	
	public void assignNeighbours()
	{
		for(int i=0; i < boardTiles.size(); i++)
		{
			ArrayList<Hex> row = boardTiles.get(i); 
			for(int j=0; j<row.size(); j++)
			{
				Hex hex = row.get(j);
				
				int belowIndex = Math.max(i-1,0);
				int aboveIndex = Math.min(i+1,boardTiles.size()-1);
				
				hex.addNeighbour(row.get(Math.min(j+1, row.size()-1)));
				hex.addNeighbour(row.get(Math.max(j-1, 0)));
				
				if(i < BOARD_HEIGHT/2)
				{
					hex.addNeighbour(boardTiles.get(aboveIndex).get(Math.min(j+1,boardTiles.get(aboveIndex).size()-1)));
					hex.addNeighbour(boardTiles.get(aboveIndex).get(Math.min(j,boardTiles.get(aboveIndex).size()-1)));
										
					hex.addNeighbour(boardTiles.get(belowIndex).get(Math.min(j,boardTiles.get(belowIndex).size()-1)));
					hex.addNeighbour(boardTiles.get(belowIndex).get(Math.max(j-1,0)));	
				}
				else if(i == BOARD_HEIGHT/2)
				{
					hex.addNeighbour(boardTiles.get(aboveIndex).get(Math.max(j-1,0)));
					hex.addNeighbour(boardTiles.get(aboveIndex).get(Math.min(j,boardTiles.get(aboveIndex).size()-1)));

					hex.addNeighbour(boardTiles.get(belowIndex).get(Math.min(j,boardTiles.get(belowIndex).size()-1)));
					hex.addNeighbour(boardTiles.get(belowIndex).get(Math.max(j-1,0)));	
				}
				else
				{
					hex.addNeighbour(boardTiles.get(aboveIndex).get(Math.max(j-1,0)));
					hex.addNeighbour(boardTiles.get(aboveIndex).get(Math.min(j,boardTiles.get(aboveIndex).size()-1)));

					hex.addNeighbour(boardTiles.get(belowIndex).get(Math.min(j,boardTiles.get(belowIndex).size()-1)));
					hex.addNeighbour(boardTiles.get(belowIndex).get(Math.min(j+1,boardTiles.get(belowIndex).size()-1)));	
				}
				//Util.DebugLog.writeError(hex.getCoordinate() + " " + hex.neighbours.size());
			}
			
		}
		
		
		
//		for(Hex hex : getHex())
//		{
//			Util.DebugLog.writeError(hex.getCoordinate()+" x:" + hex.getPosition().x +" y:"+ hex.getPosition().y);
//			for(Line edge : hex.getEdges())
//			{
//			   	Hex testHex = null;		   	
//			   	
//			   	Point midPoint = edge.getMidPoint();
//			   	Point position = hex.getPosition();
//			  	Point testPoint = Point.sub(position, midPoint);
//			  	
//			  	testPoint.mul(2f);
//			  	testPoint = Point.add(testPoint, hex.getPosition());
//			  	Util.DebugLog.writeError("\tx:" + testPoint.x +" y:"+ testPoint.y);
//				if((testHex = getHexAt(testPoint)) != null) 
//				{           
//					hex.neighbours.add(testHex);
//				} 
//		  	}
//		}
	}

	public boolean validateMove(Move move)
	{		
		ArrayList<Hex> validHex = getValidHexTargets(move.getToken());
		
		for(Hex hex : move.getPath())
		{
			if(!validHex.contains(hex))
				return false;
		}
		return true;
	}

	public Hex getOppositeHex(Hex hex1, Hex hex2)
	{
		if(!hex1.getNeighbours().contains(hex2))
			return null;
		
		return getHexAt(
				Point.add(
						Point.sub(hex1.getPosition(), hex2.getPosition()),
						hex1.getPosition()));
	}
	

	public void placeToken(String hexCoordinate, GameToken token) 
	{		
		placeToken(getHexAt(hexCoordinate), token);
	}
	
	public Hex getHexAt(String coordinate)
	{
		int col = Integer.valueOf(coordinate.toCharArray()[1])-49;
		int row = -1;
		
		if(Game.playerDesignation.equals("player1"))		
			row = Integer.valueOf(coordinate.toCharArray()[0]) - 65;		
		else if(Game.playerDesignation.equals("player2"))	
			row = 71 - Integer.valueOf(coordinate.toCharArray()[0]);				
		else
			throw new RuntimeException("player designation not set");
		
		return boardTiles.get(row).get(col);
	}
	
	public void placeToken(Point point, GameToken token) 
	{
		placeToken(getHexAt(point), token);
	}
	
	
	public void placeToken(Hex hex, GameToken token) 
	{	
		token.clearHex();
		hex.setToken(token);
		
		if(!tokens.contains(token))
			tokens.add(token);
	}
	
	public boolean hexInArea(Hex hex, STARTING_AREA area)
	{
		ArrayList<Hex> possibleTiles = new ArrayList<Hex>();
		
		if(area == STARTING_AREA.BOTTOM)
		{
			possibleTiles.addAll(boardTiles.get(0));
			possibleTiles.addAll(boardTiles.get(1));
		}
		else if(area == STARTING_AREA.TOP)
		{
			possibleTiles.addAll(boardTiles.get(boardTiles.size()-1));
			possibleTiles.addAll(boardTiles.get(boardTiles.size()-2));			
		}
		
		return possibleTiles.contains(hex);
	}

	public void destroy() 
	{				
		for(ArrayList<Hex> array : boardTiles)
			array.clear();
		boardTiles.clear();

		highlightedHex.clear();
	}

	public void draw(float deltaTime) 
	{
		for(Hex hex : getHex())
			hex.draw(deltaTime);		
	}

	public void setDrawHexCoordinates(boolean value)
	{		
		drawHexCoordinates = value;
	}
	
	public boolean getDrawHexCoordinates()
	{		
		return drawHexCoordinates;
	}

	public ArrayList<GameToken> getEngagedTokens(GameToken token)
	{
		Player enemy = player1.containsToken(token) ? player2 : player1;
		
		ArrayList<GameToken> engagedTokens = new ArrayList<GameToken>();
		for(Hex hex : token.startingHex.getNeighbours())
		{
			for(GameToken enemyToken : enemy.getTokens())
			{
				if(enemyToken.startingHex == hex && !engagedTokens.contains(enemyToken))
					engagedTokens.add(enemyToken);
			}			
		}

		return engagedTokens;
	}
	
	public ArrayList<Hex> getEngagedHex(GameToken token)
	{
		Player enemy = player1.containsToken(token) ? player2 : player1;
		
		ArrayList<Hex> engagedHex = new ArrayList<Hex>();
		for(Hex hex : token.startingHex.getNeighbours())
		{
			for(GameToken enemyToken : enemy.getTokens())
			{
				if(enemyToken.startingHex == hex && !engagedHex.contains(enemyToken))
					engagedHex.add(hex);
			}			
		}
		
		return engagedHex;
	}

	public boolean canMoveToken(Player player) 
	{
		for(GameToken token : player.getTokens())			
		{
			if(getValidHexTargets(token).size() > 1)   
				return true;
		}		
		return false;
	}
}
