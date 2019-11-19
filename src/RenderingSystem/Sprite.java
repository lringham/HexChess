package RenderingSystem;
import java.util.ArrayList;
import java.util.List;

import GUI.Clickable;
import Util.Color;
import Util.Point;
import Util.Vector3;


public class Sprite extends Drawable implements Clickable
{
	private List<Vertex> vertices = new ArrayList<Vertex>();
	
	public Sprite(Texture texture)
	{
		Point vertPosition;
		Point textureCoord;
		Color whiteColor;
		
		vertPosition = new Point(0f, 0f);
		textureCoord = new Point(0f, 0f);
		whiteColor = new Color(1f,1f,1f,1f);
		addVertex(new Vertex(vertPosition, textureCoord, whiteColor)); 
		
		vertPosition = new Point(0f, -1f);
		textureCoord = new Point(0f, 1f);
		whiteColor = new Color(1f,1f,1f,1f);
		addVertex(new Vertex(vertPosition, textureCoord, whiteColor));
		
		vertPosition = new Point(1f, -1f);
		textureCoord = new Point(1f, 1f);
		whiteColor = new Color(1f,1f,1f,1f);
		addVertex(new Vertex(vertPosition, textureCoord, whiteColor));
		
		vertPosition = new Point(1f, 0f);
		textureCoord = new Point(1f, 0f);
		whiteColor = new Color(1f,1f,1f,1f);
		addVertex(new Vertex(vertPosition, textureCoord, whiteColor));
				
		int[] vertElements = 
			{	0,1,2,
				0,2,3	};		
		
		setElements(vertElements);
						
		super.setTexture(texture);
		setWidth(texture.getWidth());
		setHeight(texture.getHeight());
	}

	public boolean clicked(Point point)
	{
		Point position = super.getPosition();
		if(point.x >= position.x && point.x <= position.x + getWidth())
			if(point.y <= position.y && point.y >= position.y - getHeight())
				return true;
		return false;
	}
		
	@Override
	public void draw(float deltaTime) 
	{
		Animation animation = null;
		if(super.animations.size() > 0)
		{
			animation = animations.get(0);
			animation.update(deltaTime);
			
			//remove completed animations
			if(animation.isCompleted())
				animations.remove(animation);
		}		
		
		RenderingSystem.draw(this);
	}

	@Override
	public List<Vertex> getVertices() 
	{
		Point position = new Point();
		position.x = super.getPosition().x;
		position.y = super.getPosition().y;

		Vector3 scale = super.getScale();
		Color color = getColor();
		//float rotation = super.getRotation();
		
		Vertex v0 = vertices.get(0);
		Vertex v1 = vertices.get(1);
		Vertex v2 = vertices.get(2);
		Vertex v3 = vertices.get(3);
				
		position.sub(getRotationPoint());
		
		v0.setPosition(position.x,			position.y);			v0.setColor(color);
		v1.setPosition(position.x,			position.y-scale.y);	v1.setColor(color);
		v2.setPosition(position.x+scale.x,	position.y-scale.y);	v2.setColor(color);
		v3.setPosition(position.x+scale.x,	position.y);			v3.setColor(color);
		
		v0.getPosition().rotate(super.getRotation());
		v1.getPosition().rotate(super.getRotation());
		v2.getPosition().rotate(super.getRotation());
		v3.getPosition().rotate(super.getRotation());

		v0.getPosition().add(getRotationPoint());
		v1.getPosition().add(getRotationPoint());
		v2.getPosition().add(getRotationPoint());
		v3.getPosition().add(getRotationPoint());
				
		return vertices;
	}

	public void addVertex(Vertex vertex) 
	{
		vertices.add(vertex);
	}
	
	public void removeVertex(Vertex vertex) 
	{
		vertices.remove(vertex);
	}
}
