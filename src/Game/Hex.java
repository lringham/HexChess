package Game;
import java.util.ArrayList;
import java.util.List;

import GUI.Clickable;
import RenderingSystem.Drawable;
import RenderingSystem.RenderingSystem;
import RenderingSystem.Texture;
import RenderingSystem.Vertex;
import Util.Color;
import Util.Line;
import Util.Point;

public class Hex extends Drawable implements Clickable
{
	private ArrayList<Hex> neighbours = new ArrayList<Hex>();
	private String coordinate = "NOT SET"; // coordinates ex: B5
	private List<Vertex> vertices = new ArrayList<Vertex>();
	private GameToken token1 = null;
	private GameToken token2 = null;
	
	public Hex(Texture texture)
	{			
		super.setTexture(texture);
		
		int HEX_HEIGHT = texture.getHeight();
		float HEX_QUARTER_HEIGHT = (float)HEX_HEIGHT / 4f;
		float HEX_WIDTH = (float) (Math.sqrt(3) * HEX_QUARTER_HEIGHT);
		float HEX_HALF_WIDTH = HEX_WIDTH / 2f;
		
		int[] vertElements = {	
				0,2,1,
				0,3,2,
				0,4,3,
				0,5,4	};

		super.setElements(vertElements);
		
	    Point hexPosition = super.getPosition();
		Point position = null;
		Point textureCoord;
		Color whiteColor = new Color(1f,1f,1f,1f);
		
		position = new Point(hexPosition.x, 					hexPosition.y + 2f*HEX_QUARTER_HEIGHT);//new Point(0f, .5f);
		textureCoord = new Point(.5f, 0f);
		addVertex(new Vertex(position, textureCoord, whiteColor)); 
		
		position = new Point(hexPosition.x + HEX_HALF_WIDTH,	hexPosition.y + HEX_QUARTER_HEIGHT);//new Point(.443f, .26f);
		textureCoord = new Point(.933f, .25f);
		addVertex(new Vertex(position, textureCoord, whiteColor));
		
		position = new Point(hexPosition.x + HEX_HALF_WIDTH,	hexPosition.y - HEX_QUARTER_HEIGHT);//new Point(.443f, -.26f);
		textureCoord = new Point(.933f, .75f);		
		addVertex(new Vertex(position, textureCoord, whiteColor));
		
		position = new Point(hexPosition.x, 					hexPosition.y - 2f*HEX_QUARTER_HEIGHT);//new Point(0f, -.5f);
		textureCoord = new Point(.5f, 1f);
		addVertex(new Vertex(position, textureCoord, whiteColor));
		
		position = new Point(hexPosition.x - HEX_HALF_WIDTH,	hexPosition.y - HEX_QUARTER_HEIGHT);//new Point(-.443f, -.26f);
		textureCoord = new Point(.066f, .75f);		
		addVertex(new Vertex(position, textureCoord, whiteColor));
		
		position = new Point(hexPosition.x - HEX_HALF_WIDTH,	hexPosition.y + HEX_QUARTER_HEIGHT);//new Point(-.443f,  .26f);
		textureCoord = new Point(.067f, .25f);
		addVertex(new Vertex(position, textureCoord, whiteColor));
				

	}

	public Line[] getEdges()
	{
		Line[] edges = new Line[6];
		edges[0] = new Line(vertices.get(0).getPosition(), vertices.get(1).getPosition());
		edges[1] = new Line(vertices.get(1).getPosition(), vertices.get(2).getPosition());
		edges[2] = new Line(vertices.get(2).getPosition(), vertices.get(3).getPosition());
		edges[3] = new Line(vertices.get(3).getPosition(), vertices.get(4).getPosition());
		edges[4] = new Line(vertices.get(4).getPosition(), vertices.get(5).getPosition());
		edges[5] = new Line(vertices.get(5).getPosition(), vertices.get(0).getPosition());
		return edges;
	}
	
	public GameToken getToken(Player player)
	{
		if(player.containsToken(token1))
			return token1;
		else if(player.containsToken(token2))
			return token2;
		else
			return null;
	}
	
	public void setToken(GameToken token)
	{
		Point position = super.getPosition();
		if(token1 == null || token2 == null)
		{		
			token.sprite.setPosition(position.x-token.sprite.getWidth()/2f, position.y+token.sprite.getHeight()/2f);
			token.currentHex = this;
		}
		
		if(token1 == null && token2 == null)
		{
			this.token1 = token;
		}
		else if(token1 == null || token2 == null)
		{
			if(token1 == null)
				token1 = token;
			
			if(token2 == null)
				token2 = token;
						
			token1.sprite.move(-token1.sprite.getWidth()/2f,0);
			token2.sprite.move(token2.sprite.getWidth()/2f,0);			
		}
	
	}
	
	public void clearTokens()
	{
		token1 = null;
		token2 = null;
	}

	public void clearToken(Player activePlayer)
	{
		if(activePlayer.containsToken(token1))
			token1 = null;
		else if(activePlayer.containsToken(token2))
			token2 = null;
		
		if(token1 != null)
			token1.sprite.move(token1.sprite.getWidth()/2f,0);
		else if(token2 != null)
			token2.sprite.move(-token2.sprite.getWidth()/2f,0);
	}
		
	public Point getPosition()
	{		
		return super.getPosition();
	}
	
	public boolean clicked(Point point)
	{      
	  updateHexVerts();
	  int edgesToTheLeft = 0;
	  Point hitPoint = null;	  
	     
	  //FIXME: account for special cases... such as clicking vertices, ect..	  
	  for(Line edge : getEdges())
	  {
		//get the point on the edge corresponding to the y of the point in question
	     if((hitPoint = edge.getPointAtY(point.y)) != null) 
	     {           
	       if(hitPoint.x < point.x)	//if the edge point is to the left of the point in question,  increment 
	          edgesToTheLeft++; 
	     } 
	  }
	      
	  //if there are an odd number of edges to the left of the point in question
	  //then the point is inside the hexagon	  
	  if(edgesToTheLeft % 2 == 1)  
	    return true;
	  else   
	    return false;
	}

	private void updateHexVerts()
	{
		Point position = super.getPosition();		
		float HEX_QUARTER_HEIGHT = (float) super.getHeight() / 4f;
		float HEX_HALF_WIDTH = (float) (Math.sqrt(3) * HEX_QUARTER_HEIGHT); 	// sqrt(3) * quarterHeight. This is used for the vertices x offsets
		super.setWidth(HEX_HALF_WIDTH*2f);
		   
	    vertices.get(0).setPosition(new Point(position.x, 					position.y + 2f*HEX_QUARTER_HEIGHT));
	    vertices.get(1).setPosition(new Point(position.x + HEX_HALF_WIDTH,	position.y + HEX_QUARTER_HEIGHT));
	    vertices.get(2).setPosition(new Point(position.x + HEX_HALF_WIDTH,	position.y - HEX_QUARTER_HEIGHT));    
	    vertices.get(3).setPosition(new Point(position.x, 					position.y - 2f*HEX_QUARTER_HEIGHT));
	    vertices.get(4).setPosition(new Point(position.x - HEX_HALF_WIDTH,	position.y - HEX_QUARTER_HEIGHT));
	    vertices.get(5).setPosition(new Point(position.x - HEX_HALF_WIDTH,	position.y + HEX_QUARTER_HEIGHT));
	}

	public boolean hasToken() 
	{		
		return (token1 != null || token2 != null);
	}

	public boolean containsToken(GameToken token) 
	{
		return (token1 == token || token2 == token);
	}

	@Override
	public void draw(float dt)
	{
		RenderingSystem.draw(this);
	}

	public void addVertex(Vertex vertex) 
	{
		vertices.add(vertex);
	}
	
	public void removeVertex(Vertex vertex) 
	{
		vertices.remove(vertex);
	}
	
	@Override
	public List<Vertex> getVertices()
	{
		updateHexVerts();
		return vertices;
	}	
	
	public void setColor(Color color)
	{
		for(Vertex vert : vertices)
			vert.setColor(color);
	}
	
	public void setColor(float red, float green, float blue, float alpha)
	{
		for(Vertex vert : vertices)
			vert.setColor(red, green, blue, alpha);
	}

	public String getCoordinate() 
	{
		return coordinate;
	}

	public void setCoordinates(String coordinate) 
	{
		this.coordinate = coordinate;
	}

	public void addNeighbour(Hex hex) 
	{
		if(!neighbours.contains(hex) && this != hex)
		{			
			neighbours.add(hex);
			//hex.addNeighbour(this);
		}
	}

	public ArrayList<Hex> getNeighbours() {
		return neighbours;
	}

	public void removeToken(GameToken gameToken) 
	{
		if(token1 == gameToken)
			token1 = null;
		if(token2 == gameToken)
			token2 = null;

		if(token1 != null)
			token1.sprite.move(token1.sprite.getWidth()/2f,0);
		if(token2 != null)
			token2.sprite.move(-token2.sprite.getWidth()/2f,0);
	}

	public boolean hasTwoTokens() 
	{
		return (token1 != null && token2 != null);
	}

	public Point getPosition(GameToken token) 
	{
		Point position = getPosition();
		
		//offset the token based if it shares the hex with another token.
//		if((token1 == token && token2 != null) || (token2 != null && token2 != token))
//			return new Point(position.x-2*(token.sprite.getWidth()/2f), position.y+token.sprite.getHeight()/2f);
//		if((token2 == token && token1 != null)|| (token1 != null && token1 != token))
//			return new Point(position.x, position.y+token.sprite.getHeight()/2f);

		return new Point(position.x-token.sprite.getWidth()/2f, position.y+token.sprite.getHeight()/2f);
	}
}
