package RenderingSystem;


public class Texture
{
	private final int textureID;
	private final String name;
	private final int width;
	private final int height;
	
	public Texture(int textureID, String name, int width, int height)
	{
		this.textureID = textureID;
		this.name = name;
		this.width = width;
		this.height = height;		
	}

	public int getTextureID() 
	{
		return textureID;
	}

	public String getName() 
	{
		return name;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight() 
	{
		return height;
	}
}
