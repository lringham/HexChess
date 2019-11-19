package GUI;

import java.util.List;

import Font.Character;
import Font.Font;
import RenderingSystem.Drawable;
import RenderingSystem.RenderingSystem;
import RenderingSystem.Vertex;
import Util.Color;
import Util.Point;

public class DrawableText extends Drawable
{
	private String text = "";
	private Font font = null;
	
	public DrawableText(Font font, String text)
	{
		this.font = font;
		this.text = text;
		super.setColor(new Color(1,1,1,1));
	}
	
	public DrawableText(Font font, String text, Point position)
	{
		this.font = font;
		this.text = text;
		super.setPosition(position.x,position.y,position.z);
		super.setColor(new Color(1,1,1,1));
	}
	
	public DrawableText(Font font, String text, Point position, Color color)
	{
		this.font = font;
		this.text = text;
		super.setPosition(position);
		super.setColor(color);
	}
	
	@Override
	public void draw(float deltaTime)
	{
		RenderingSystem.draw(this);
	}

	public float getWidth()
	{		
		float width = 0;
		
		for(char i : text.toCharArray())
		{
			Character ch = font.getCharacter((int)i);
			width += ch.getXAdvance();
		}
		
		return width;
	}
	
	public float getHeight()
	{		
		float height = 0;
		
		for(char i : text.toCharArray())
		{
			Character ch = font.getCharacter((int)i);
			height = Math.max(ch.getHeight(), height);
		}
		
		return height;
	}	
	
	@Override
	public List<Vertex> getVertices() 
	{
		return null;
	}

	public void setText(String value) 
	{
		text = value;
	}
	
	public String getText() 
	{
		return text;
	}
	
	public String toString() 
	{
		return text;
	}

	public void append(String value) 
	{
		text += value;		
	}

	public Font getFont() 
	{
		return font;
	}
}
