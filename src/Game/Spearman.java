package Game;

import RenderingSystem.RenderingSystem;

public class Spearman extends GameToken
{

	public Spearman()
	{		
		movementRange = 1;
		sprite = RenderingSystem.generateSprite("InfantryWhite");
		sprite.setLayer(RenderingSystem.DRAW_LAYER.FOREGROUND2);
		type = TOKEN_TYPE.SPEARMAN;
	}

	@Override
	public boolean hasMovesLeft() {
		return move.finished();
	}
}
