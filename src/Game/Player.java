package Game;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Player
{
	public final GameBoard.STARTING_AREA STARTING_AREA;	
	public ArrayList<Move> moves = new ArrayList<Move>(2);
	private Map<GameToken.TOKEN_TYPE, Integer> tokenMax = new HashMap<GameToken.TOKEN_TYPE, Integer>();
	private Map<GameToken.TOKEN_TYPE, ArrayList<GameToken>> gameTokens = new HashMap<GameToken.TOKEN_TYPE, ArrayList<GameToken>>();
	private int maxMoves = 2;
	private boolean ready = false;
	
	public Player(GameBoard.STARTING_AREA startingArea)
	{		
		STARTING_AREA = startingArea;
		
		tokenMax.put(GameToken.TOKEN_TYPE.INFANTRY, 0);
		tokenMax.put(GameToken.TOKEN_TYPE.SPEARMAN, 4);
		tokenMax.put(GameToken.TOKEN_TYPE.KNIGHT, 2);
		tokenMax.put(GameToken.TOKEN_TYPE.GENERAL, 1);
		
		gameTokens.clear();
		for (GameToken.TOKEN_TYPE type : GameToken.TOKEN_TYPE.values()) 		
			gameTokens.put(type, new ArrayList<GameToken>());
		
	}
	
	/**
	 * @param token to be added.
	 * @return Boolean: true if successful, false if token max reached or token contained already.
	 */	
	public boolean addToken(GameToken token)
	{		
		if(gameTokens.get(token.type).size() < tokenMax.get(token.type) && !getTokens(token.type).contains(token))
		{
			gameTokens.get(token.type).add(token);
			return true;
		}
		else
			return false;
	}
	
	public boolean getReady()
	{
		return ready;		
	}
	
	public void setReady(boolean ready)
	{
		this.ready = ready;
	}
	
	/**
	 * @param token to be removed.
	 * @return Boolean: true if successful, false if token doesn't exist.
	 */
	public boolean removeToken(GameToken token)
	{
		if(gameTokens.get(token.type).size() > 0)
		{
			gameTokens.get(token.type).remove(token);
			return true;
		}
		else
			return false;
	}
	
	public int getNumTokens()
	{
		return getTokens().size();		
	}
	
	public int getNumTokens(GameToken.TOKEN_TYPE type)	
	{
		return gameTokens.get(type).size();
	}
	
	public ArrayList<GameToken> getTokens(GameToken.TOKEN_TYPE type)	
	{
		return gameTokens.get(type);
	}
	
	public ArrayList<GameToken> getTokens()	
	{
		ArrayList<GameToken> allTokens = new ArrayList<GameToken>();

		for (GameToken.TOKEN_TYPE type : GameToken.TOKEN_TYPE.values()) 		
			allTokens.addAll(gameTokens.get(type));
				
		return allTokens;
	}
		
	public int getTokenMax(GameToken.TOKEN_TYPE type)
	{
		return tokenMax.get(type);
	}

	public boolean maxReached(GameToken.TOKEN_TYPE type)
	{
		return getNumTokens(type) >= getTokenMax(type);
	}
	
	public boolean allTokensPlaced()
	{
		boolean placed = true;
		
		for (GameToken.TOKEN_TYPE type : GameToken.TOKEN_TYPE.values()) 		
			placed = placed && maxReached(type);
		
		return placed;
	}
	
	public boolean containsToken(GameToken token)
	{
		if(token == null)
			return false;
		else
			return getTokens(token.type).contains(token);	
	}

	public void clearMoves() 
	{
//		for(Move move : moves)
//		{
//			move.sourceHex.getToken().sprite.delete();
//			move.sourceHex.clearToken();
//		}
		moves.clear();		
	}

	public void addMove(Move move)
	{
		if(hasMovesRemaining())
			moves.add(move);
	}
	
	public int getNumAvailMoves() 
	{
		if(maxMoves > getTokens().size())
			return getTokens().size() - moves.size();
		else
			return maxMoves - moves.size();		
	}
	
	public boolean hasMovesRemaining()
	{
		return getNumAvailMoves() > 0;
	}
	
	public boolean hasMovedToken(GameToken selectedToken)
	{
		for(Move move  : moves)
		{
			if(move.getToken() == selectedToken)
				return true;
		}
		return false;
	}

	public Move getMove(GameToken selectedToken) 
	{
		for(Move move  : moves)
		{
			if(move.getToken() == selectedToken)
				return move;
		}
		return null;
	}

	public void removeMove(GameToken selectedToken) 
	{
		moves.remove(getMove(selectedToken));	
	}

	public ArrayList<Move> getMoves() 
	{
		return moves;
	}

	public void removeMove(Move move) 
	{
		if(move != null)
			moves.remove(move);	
	}

	public boolean containsMove(Move currentMove) 
	{
		return moves.contains(currentMove);
	}

	public boolean canMoveToken() 
	{
		return false;
	}
}
