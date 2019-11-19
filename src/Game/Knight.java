package Game;

import RenderingSystem.RenderingSystem;

public class Knight extends GameToken
{
	
	public Knight()
	{		
		movementRange = 2;
		sprite = RenderingSystem.generateSprite("KnightWhite");
		sprite.setLayer(RenderingSystem.DRAW_LAYER.FOREGROUND2);
		type = TOKEN_TYPE.KNIGHT;
	}

	@Override
	public boolean hasMovesLeft() {
		return move.finished();
	}
}
