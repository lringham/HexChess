package Network;

import java.util.List;

import Game.Hex;
import Game.Move;

public class TurnMessage implements Message
{
	private String text = "";
	public TurnMessage(List<Move> moves)
	{
		for(Move move : moves)
		{
			text += "move:";	
			for(Hex hex : move.getPath())			
				text += hex.getCoordinate()+",";
			
			if(text.contains(",")) 
			{
				text = text.substring(0, text.length()-1);
			}
		}
	}

	@Override
	public String getText() 
	{
		return text;
	}
}
