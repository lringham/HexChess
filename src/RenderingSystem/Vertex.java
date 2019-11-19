package RenderingSystem;
import Util.Color;
import Util.Point;


public class Vertex
{	
	private static int floatsInPosition = 3;		//x,y,z
	private static int floatsInColor = 4;			//r,g,b,a
	private static int floatsInTextureCoords = 2;	//u,v
		
	private Point position = null;
	private Color color = null;
	private Point textureCoordinates = null;
	Float[] floats = new Float[FLOAT_COUNT];
	
	private static int FLOAT_COUNT = floatsInPosition + floatsInColor + floatsInTextureCoords;
	public static final int BYTES_IN_FLOAT = Float.SIZE / Byte.SIZE;
	public static final int SIZE_IN_BYTES = FLOAT_COUNT * BYTES_IN_FLOAT;
	public static final int SIZE = FLOAT_COUNT;
	
	public Vertex(Point position, Point textureCoordinates, Color color)
	{
		this.position = position;
		this.color = color;
		this.textureCoordinates = textureCoordinates;
		floats = new Float[FLOAT_COUNT];
	}
	
	public Vertex()
	{
	}
		
	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public void setPosition(float x, float y) 
	{
		position.x = x;
		position.y = y;
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) 
	{
		this.color.red = color.red;
		this.color.green = color.green;
		this.color.blue = color.blue;
		this.color.alpha = color.alpha;
	}

	public Point getTextureCoordinates() {
		return textureCoordinates;
	}

	public void setTextureCoordinates(Point textureCoordinates) {
		this.textureCoordinates = textureCoordinates;
	}

	public Float[] asFloats() 
	{		
		floats[0] = position.x;
		floats[1] = position.y;
		floats[2] = position.z;

		floats[3] = color.red;
		floats[4] = color.green;
		floats[5] = color.blue;
		floats[6] = color.alpha;

		floats[7] = textureCoordinates.x;
		floats[8] = textureCoordinates.y;
		
		return floats;
	}

	public void setColor(float red, float green, float blue, float alpha) 
	{
		color.red = red;
		color.green = green;
		color.blue = blue;
		color.alpha = alpha;		
	}
}
