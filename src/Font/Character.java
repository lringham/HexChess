package Font;

import java.util.ArrayList;
import java.util.List;

import RenderingSystem.Drawable;
import RenderingSystem.RenderingSystem;
import RenderingSystem.Texture;
import RenderingSystem.Vertex;
import Util.Color;
import Util.Point;
import Util.Vector3;

public class Character extends Drawable
{	
	protected int ID;
	protected Point offset = new Point();
	protected float xAdvance;
	List<Vertex> vertices = new ArrayList<Vertex>();
	public Point texturePosition = new Point();
	public Vector3 textureScale = new Vector3();	
	
	Character(int ID, Point texturePosition, Vector3 scale, Texture texture, Point offset, float xAdvance)
	{
		this.texturePosition = texturePosition;
		this.textureScale.x = scale.x / texture.getWidth();
		this.textureScale.y = scale.y / texture.getHeight();
		super.setScale(scale);		
		this.ID = ID;
		this.offset.x = offset.x;
		this.offset.y = offset.y;
		this.xAdvance = xAdvance;
		
		Point vertPosition;
		Point textureCoord;
		Color whiteColor;
		
		vertPosition = new Point(0f, 0f);
		textureCoord = new Point(texturePosition.x, texturePosition.y);
		whiteColor = new Color(1f,1f,1f,1f);
		addVertex(new Vertex(vertPosition, textureCoord, whiteColor)); 
		
		vertPosition = new Point(0f, -1f);
		textureCoord = new Point(texturePosition.x, texturePosition.y+textureScale.y);
		whiteColor = new Color(1f,1f,1f,1f);
		addVertex(new Vertex(vertPosition, textureCoord, whiteColor));
		
		vertPosition = new Point(1f, -1f);
		textureCoord = new Point(texturePosition.x+textureScale.x, texturePosition.y+textureScale.y);
		whiteColor = new Color(1f,1f,1f,1f);
		addVertex(new Vertex(vertPosition, textureCoord, whiteColor));
		
		vertPosition = new Point(1f, 0f);
		textureCoord = new Point(texturePosition.x+textureScale.x, texturePosition.y);
		whiteColor = new Color(1f,1f,1f,1f);
		addVertex(new Vertex(vertPosition, textureCoord, whiteColor));
				
		int[] vertElements = 
			{	0,1,2,
				0,2,3	};		
		
		setElements(vertElements);
						
		super.setTexture(texture);
	}
	
	private void addVertex(Vertex vertex) 
	{
		vertices.add(vertex);	
	}

	public int getID()
	{
		return ID;
	}
			
	public float getTexWidth()
	{
		return textureScale.x;
	}

	public float getTexHeight()
	{
		return textureScale.y;
	}

	public Point getOffset()
	{
		return offset;
	}
	
	public float getXOffset()
	{
		return offset.x;
	}

	public float getYOffset()
	{
		return offset.y;
	}

	public float getXAdvance()
	{
		return xAdvance;
	}

	@Override
	public void draw(float dt) 
	{
		RenderingSystem.draw(this);
	}

	@Override
	public List<Vertex> getVertices() 
	{
		Point position = super.getPosition();
		Vector3 scale = super.getScale();
		Color color = getColor();
		//float rotation = super.getRotation();
		
		Vertex v0 = vertices.get(0);
		Vertex v1 = vertices.get(1);
		Vertex v2 = vertices.get(2);
		Vertex v3 = vertices.get(3);
		
		
		v0.setPosition(position.x,			position.y);   		v0.setColor(color);
		v1.setPosition(position.x, 		position.y-scale.y);	v1.setColor(color);
		v2.setPosition(position.x+scale.x, position.y-scale.y);	v2.setColor(color);
		v3.setPosition(position.x+scale.x, position.y);			v3.setColor(color);
		
		return vertices;
	}
	
}
