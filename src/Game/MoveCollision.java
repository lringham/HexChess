package Game;

import java.util.ArrayList;

public class MoveCollision
{
	private GameToken playerToken = null;
	private GameToken enemyToken = null;
	private Hex collisionHex = null;
	
	private Move playerMove;
	private Move enemyMove;
	
	public MoveCollision(GameToken playerToken, GameToken enemyToken, ArrayList<Hex> playerPath, ArrayList<Hex> enemyPath, Hex collisionHex)
	{
		this.playerToken = playerToken;
		this.enemyToken = enemyToken;
		playerMove = new Move(playerToken, playerPath);
		enemyMove = new Move(enemyToken, enemyPath);
		this.collisionHex = collisionHex;
	}
	
	
	public Hex getCollisionHex()
	{
		return collisionHex;
	}
	
	public GameToken getPlayerToken()
	{
		return playerToken;
	}

	public GameToken getEnemyToken()
	{
		return enemyToken;
	}
	
	public Move getPlayerMove()
	{
		return playerMove;
	}
	
	public Move getEnemyMove()
	{
		return enemyMove;
	}
}
