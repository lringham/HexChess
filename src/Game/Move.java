package Game;

import java.util.ArrayList;

public class Move 
{
	private Hex sourceHex = null;
	private int MAX_PATH_LENGTH;
	private GameToken token;
	private ArrayList<Hex> path;
	private MoveCollision[] pathCollisions;
	
	@SuppressWarnings("unchecked")
	public Move(GameToken piece, ArrayList<Hex> path)
	{
		this.sourceHex = path.get(0);
		this.token = piece;
		this.path = (ArrayList<Hex>) path.clone();		
		MAX_PATH_LENGTH = piece.movementRange+1;
		pathCollisions = new MoveCollision[MAX_PATH_LENGTH];
	}
	
	public Move(Hex sourceHex, GameToken piece)
	{
		this.sourceHex = sourceHex;
		this.token = piece;
		path = new ArrayList<Hex>();
		path.add(sourceHex);
		MAX_PATH_LENGTH = piece.movementRange+1;
		pathCollisions = new MoveCollision[MAX_PATH_LENGTH];
	}

	public Move(String[] hexPathCoords, Player player, GameBoard board) throws Exception 
	{
		path = new ArrayList<Hex>();
		for(String hexCoord : hexPathCoords)
		{
			Hex hex = board.getHexAt(hexCoord);
			if(sourceHex == null)
			{
				sourceHex = hex;			
				token = sourceHex.getToken(player);
				sourceHex.clearToken(player);
				
				if(token == null)
					throw new Exception("invalid move: "+hexCoord);
				MAX_PATH_LENGTH = token.movementRange+1;
			}
			path.add(hex);	
		}
		peekPath().setToken(token);
		pathCollisions = new MoveCollision[MAX_PATH_LENGTH];
	}
	
	public void setCollisionAt(MoveCollision collision, Hex hex)
	{
		int collisionIndex = getIndexOf(hex);
		if(collisionIndex != -1)
			pathCollisions[collisionIndex] = collision;
		else
			throw new RuntimeException("Hex not in path");
	}
	
	public MoveCollision getCollisionAt(Hex hex)
	{
		int collisionIndex = getIndexOf(hex);
		if(collisionIndex != -1)
			return pathCollisions[collisionIndex];
		else
			throw new RuntimeException("Hex not in path");
	}
	
	public Hex getFirstCollisionHex()
	{
		for(MoveCollision collision : pathCollisions)
			if(collision != null)
				return collision.getCollisionHex();		
		
		return null;
	}
	
	public Hex getValidLastHex()
	{
		Hex lastHex = null;
		boolean collisionNotFound = true;
		for(int i=0;i<path.size() && collisionNotFound;i++)		
		{
			MoveCollision collision = pathCollisions[i];
			if(collision != null)
			{
				lastHex = path.get(i-1);
				collisionNotFound = false;
			}
		}
		
		if(lastHex == null)		
			lastHex = peekPath();				
		
		return lastHex;
	}
	
	public Hex peekPath()
	{
		return path.get(path.size()-1);
	}
	
	public Hex getHex(int i)
	{
		return path.get(i);
	}
	
	public void pushPath(Hex hex)
	{
		if(path.contains(hex))
		{
			path = new ArrayList<Hex>(path.subList(0, path.indexOf(hex)+1));
		}
		else if(path.size() > 1 && path.get(0).getNeighbours().contains(hex))
		{
			Hex start = path.get(0);
			path = new ArrayList<Hex>(2);
			path.add(start);
			path.add(hex);	
		}
		else if(path.size() > 2 && path.get(1).getNeighbours().contains(hex))
		{
			Hex start = path.get(0);
			Hex mid = path.get(1);
			path = new ArrayList<Hex>(2);
			path.add(start);			
			path.add(mid);
			path.add(hex);	
		}
		else if(pathLength() < MAX_PATH_LENGTH && !path.contains(hex) && peekPath().getNeighbours().contains(hex))
		{
			path.add(hex);
			
		}
	}
	public ArrayList<Hex> getPath()
	{	
		return path;
	}

	public int pathLength()
	{	
		return path.size();
	}
	
	public boolean finished() 
	{
		return pathLength() == MAX_PATH_LENGTH;
	}

	public Hex popPath()
	{
		Hex lastHex = path.get(path.size()-1);
		path.remove(path.size()-1);
		pathCollisions[pathCollisions.length-1] = null;
		return lastHex;		
	}
	
	public GameToken getToken()
	{
		return token;
	}
	
	public boolean pathEndsWith(Hex endHex)
	{
		return (endHex == peekPath());
	}

	public Hex getSourceHex() 
	{
		return sourceHex;
	}

	public void setEnd(Hex newHex) 
	{
		ArrayList<Hex> newPath = new ArrayList<Hex>();
		
		for(int i=0; i<pathCollisions.length;i++)
			pathCollisions[i] = null;
		
		boolean endSeen = false;
		
		for(Hex hex : path)
		{			
			if(endSeen)
				continue;
			
			if(hex == newHex)
			{
				endSeen = true;
				newPath.add(hex);
				continue;
			}			
			newPath.add(hex);
		}
		
		path = newPath;
	}

	public int getIndexOf(Hex hex) 
	{
		if(path.contains(hex))
			return path.indexOf(hex);
		else
			return -1;
	}

	public MoveCollision getClosestCollision() 
	{
		for(int i = 0; i < pathCollisions.length; i++)
		{
			if(pathCollisions[i] != null)
			return pathCollisions[i];	
		}
		return null;
	}
}
