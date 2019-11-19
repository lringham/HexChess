package Network;

import java.util.List;

import Game.GameToken;

public class InitBoardMessage implements Message{

	String text = "tokenPlacement:";
	public InitBoardMessage(List<GameToken> tokens)
	{
		for(GameToken token : tokens)
			text += token.getType().toString() + "," + token.currentHex.getCoordinate()+"#";
		
		if(text.contains("#")) 
		{
			text = text.substring(0, text.length()-1);
		}
	}
	
	@Override	
	public String getText() 
	{
		return text;
	}
}
