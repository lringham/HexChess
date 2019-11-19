package RenderingSystem;


import java.util.ArrayList;
import java.util.List;

import Util.Color;
import Util.Point;
import Util.Vector2;
import Util.Vector3;


public abstract class Drawable
{	
	protected int[] elements;
	
	private float rotation = 0;
	private Point rotationPoint = new Point(0,0);
	
	private Point position = new Point();
	private Vector3 scale = new Vector3(1f,1f,1f);
	private Texture texture = null;
	private boolean visible = true;	
	private Color color = new Color();
	protected List<Animation> animations = new ArrayList<Animation>();
	private RenderingSystem.DRAW_LAYER layer = RenderingSystem.DRAW_LAYER.FOREGROUND0;


	public abstract void draw(float deltaTime);
	public abstract List<Vertex> getVertices();
	
	public void addAnimation(Animation animation)
	{
		animations.add(animation);
	}
	
	public Texture getTexture()
	{
		return texture;
	}

	public void setPosition(Point point)
	{
		position.x = point.x;
		position.y = point.y;
	}

	public void setPosition(float x, float y)
	{
		position.x = x;
		position.y = y;
	}
	
	public void move(Vector2 translation)
	{
		position.x += translation.x;
		position.y += translation.y;
	}
	
	public void move(float x, float y)
	{
		position.x += x;
		position.y += y;
	}
	
	public void setPosition(float x, float y, float z)
	{
		position.x = x;
		position.y = y;
		position.z = z;
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public Point getPosition()
	{
		return position;
	}
	
	public float getX()
	{
		return position.x;
	}

	public float getY()
	{
		return position.y;
	}
	
	public float getZ()
	{
		return position.z;
	}
	
	public float getWidth()
	{
		return scale.x;
	}
	
	public float getHeight()
	{
		return scale.y;
	}
	
	public Vector3 getScale()
	{
		return scale;
	}

	public boolean getVisible()
	{
		return visible;
	}
	
	public void setVisible(boolean visible)
	{
		this.visible = visible;
	}
	
	public void setWidth(float value)
	{
		scale.x = value;
	}
	
	public void setHeight(float value)
	{
		scale.y = value;
	}

	public void setScale(Vector3 scale)
	{
		this.scale = scale;
	}
	
	public void setColor(Color color)
	{
		this.color.red = color.red;
		this.color.green = color.green;
		this.color.blue = color.blue;
	}
	
	public void setColorAlpha(float alpha)
	{
		color.alpha = alpha;		
	}
	
	public void setElements(int[] elements) 
	{
		this.elements = elements;
	}
	
	public void setColor(float r, float g, float b, float a)
	{
		color.red = r;
		color.green = g;
		color.blue = b;
		color.alpha = a;
	}	
	
	public int[] getElements() 
	{
		return elements;
	}

	public void setTexture(Texture texture) 
	{
		this.texture = texture;		
	}
	
	public Texture getTexture(Texture texture) 
	{
		return texture;		
	}
	
	public void setXPosition(float x)
	{
		position.x = x;		
	}
	
	public void setYPosition(float y)
	{
		position.y = y;		
	}
	
	public void rotate(float angle, Point rotationPoint)
	{
		this.rotation = angle;
		this.rotationPoint = rotationPoint;
	}
		
	public float getRotation() 
	{
		return rotation;
	}
	
	public Point getRotationPoint() 
	{
		return rotationPoint;
	}
	
	public void setRotation(float angle, Point rotationPoint) 
	{
		rotation = angle;
		setRotationPoint(rotationPoint);		
	}
	
	public void setRotationAroundCentre(float angle) 
	{
		rotation = angle;
		setRotationPoint(new Point(getX()+(getWidth()/2f), getY()-(getHeight()/2f)));
	}
	
	public void setRotation(float angle) 
	{
		rotation = angle;
	}	
	
	public void setRotationPoint(Point rotationPoint) 
	{
		this.rotationPoint = rotationPoint;
	}
	
	public RenderingSystem.DRAW_LAYER getLayer() 
	{
		return layer;
	}
	
	public void setLayer(RenderingSystem.DRAW_LAYER layer) 
	{
		this.layer = layer;
	}
}
