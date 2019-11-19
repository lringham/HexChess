package Game;

import RenderingSystem.Drawable;

public abstract class GameToken
{
	public int movementRange = 0;
	public Hex currentHex = null;
	public Hex startingHex = null;
	public Engagement engagement = null;
	public Drawable sprite;
	public Move move = null;
	protected TOKEN_TYPE type;
	
	public TOKEN_TYPE getType() 
	{
		return type;
	}

	public enum TOKEN_TYPE
	{
		INFANTRY,
		SPEARMAN,
		KNIGHT,		
		GENERAL
	}

	public boolean hasMovesLeft() 
	{
		return move.finished();
	}

	public Boolean isEngagedTo(GameToken token)
	{
		if(engagement != null && engagement.containsToken(token))
			return true;
		else 
			return false;
			
	}

	public void clearHex() 
	{
		currentHex = null;
	}

	public void setCurrentHex(Hex currentHex) 
	{
		this.currentHex.removeToken(this);
		this.currentHex = currentHex;		
	}

	public void setStartingHex(Hex startingHex) 
	{
		this.startingHex.removeToken(this);
		this.startingHex = startingHex;
	}
	
	public boolean hasMoved()
	{
		return (currentHex != startingHex);
	}
	
	@Override
	public String toString() 
	{
		return type.toString() + " " + sprite.getPosition();
	}
}
