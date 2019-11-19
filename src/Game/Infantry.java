package Game;

import RenderingSystem.RenderingSystem;



public class Infantry extends GameToken
{

	public Infantry()
	{		
		sprite = RenderingSystem.generateSprite("InfantryWhite");
		sprite.setLayer(RenderingSystem.DRAW_LAYER.FOREGROUND2);
		movementRange = 1;
		type = TOKEN_TYPE.INFANTRY;
	}

	@Override
	public boolean hasMovesLeft() {
		return move.finished();
	}
}
