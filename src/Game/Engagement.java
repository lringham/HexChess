package Game;

import GUI.Clickable;
import RenderingSystem.RenderingSystem;
import RenderingSystem.Sprite;
import Util.Point;
import Util.TimeInterval;

public class Engagement implements Clickable 
{
	public Sprite sprite;
	public GameToken token1;
	public GameToken token2;
	public Hex hex1;
	public Hex hex2;
	public TimeInterval timer = new TimeInterval(500);
	
	public Engagement(GameToken token, GameToken enemyToken, Sprite arrow) 
	{
		token1 = token;
		token2 = enemyToken;
		hex1 = token.currentHex;
		hex2 = enemyToken.currentHex;
		sprite = arrow;
		sprite.setLayer(RenderingSystem.DRAW_LAYER.FOREGROUND1);
	}

	public Engagement() 
	{

	}

	@Override
	public boolean clicked(Point point)
	{
		return sprite.clicked(point);
	}

	public boolean containsToken(GameToken token) 
	{
		if(token1 != token && token2 != token)
			return false;
		return true;
	}

	public boolean containsHex(Hex hex) 
	{
		if(hex1 != hex && hex2 != hex)
			return false;
		return true;
	}

	public GameToken getToken1() 
	{
		return token1;
	}	
	
	public GameToken getToken2() 
	{
		return token2;
	}	
}
