package GUI;

import java.util.List;

import RenderingSystem.Drawable;
import RenderingSystem.RenderingSystem;
import RenderingSystem.Sprite;
import RenderingSystem.Vertex;
import Util.Point;
import Util.Vector2;

public class TextBox extends Drawable implements Clickable 
{
	Point position = null;
	DrawableText currentText = null;
	Sprite background = RenderingSystem.generateSprite("black");
	Vector2 textOffset = new Vector2(3f, 3f);
	public String regexFilter = "";
	boolean boxFilled = false;
	public boolean selected = false;
	private int charLimit = 30;
	
	public TextBox(Point position, int width)
	{
		this.position = position;
		currentText = RenderingSystem.generateText("",new Point(position.x+textOffset.x,position.y-textOffset.y));
		currentText.setColor(1f, 1f, 1f, 1f);
		background.setPosition(position);
		background.setColor(0f, 0f, 0f, .25f);
		background.setWidth(width);
		background.setHeight(40f);
		background.setLayer(RenderingSystem.DRAW_LAYER.FOREGROUND1);
		currentText.setLayer(RenderingSystem.DRAW_LAYER.FOREGROUND2);
	}
	
	@Override
	public boolean clicked(Point point) 
	{	
		return background.clicked(point);
	}

	public void setText(String text)
	{
		currentText.setText(text);
	}
			
	public void appendText(String text)
	{
		if(!text.equals(""))
		{
			if(text.equals("DEL"))
			{
				currentText.setText(currentText.toString().substring(0, Math.max(0, currentText.toString().length()-1)));
				if(currentText.getWidth() < background.getWidth())
					boxFilled = false;
				
			}
			else
			{
				text = text.replaceAll(regexFilter, "");			
				if(!(text.equals("\n") || text.equals("END") || text.equals("ESC") || boxFilled))
				{
					currentText.append(text);
					if(currentText.getWidth() >= background.getWidth())
					{
						boxFilled = true;
						currentText.setText(currentText.toString().substring(0, Math.max(0, currentText.toString().length()-1)));
					}
				}
			}
		}
		
		if(currentText.getText().length() > charLimit)
			currentText.setText(currentText.getText().substring(0, charLimit));	
	}
	
	@Override
	public void draw(float deltaTime) 
	{
		if(selected)
			background.setColorAlpha(.8f);
		else
			background.setColorAlpha(.25f);
		
		background.draw(deltaTime);
		currentText.draw(deltaTime);
	}

	@Override
	public List<Vertex> getVertices() 
	{
		return null;
	}

	public String getText() 
	{
		return currentText.toString();
	}
	
	public int getCharLimit()
	{
		return charLimit;
	}
	
	public void setCharLimit(int charLimit)
	{
		this.charLimit = charLimit;
	}
}
