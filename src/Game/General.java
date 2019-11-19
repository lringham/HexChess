package Game;

import RenderingSystem.RenderingSystem;

public class General extends GameToken
{

	public General()
	{		
		movementRange = 2;
		sprite = RenderingSystem.generateSprite("KingWhite");
		sprite.setLayer(RenderingSystem.DRAW_LAYER.FOREGROUND2);
		type = TOKEN_TYPE.GENERAL;
	}
}
