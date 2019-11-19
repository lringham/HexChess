package GUI;

import java.util.ArrayList;
import java.util.List;

import RenderingSystem.Drawable;
import RenderingSystem.RenderingSystem;
import RenderingSystem.Sprite;
import RenderingSystem.Vertex;
import RenderingSystem.RenderingSystem.DRAW_LAYER;
import Util.Color;
import Util.Point;

public class Console extends Drawable
{
	ArrayList<DrawableText> lines = new ArrayList<DrawableText>();
    private int lineIndex = 0;
    Sprite background = null;
    float textXOffset = 5f;
    float textYOffset = 5f;
    
    public Console()
    {
    	lineIndex  = -1;
    	background = RenderingSystem.generateSprite("black");
    	background.setWidth(200f);
    	background.setHeight(200f);
    	background.setColorAlpha(.5f);
    }
    
	@Override
	public void draw(float deltaTime) 
	{
		topDownDraw(deltaTime);
	}

	private void topDownDraw(float deltaTime)
	{
		background.draw(deltaTime);				
		
		if(lines.size() > 0)
		{
			float textX = background.getPosition().x + textXOffset;
			float textY = background.getPosition().y - textYOffset;
					
			int linesHeight = 0;
			int endIndex = lineIndex;
			int i = lineIndex;
			
			while(linesHeight < background.getHeight() && i < lines.size())
			{
				linesHeight += lines.get(i).getHeight();
				
				if(linesHeight < background.getHeight())
					endIndex = i;
				
				i++;
			}		
			
			//draw the lineLimit amount of texts starting from the bottom going up		
			for(int j = lineIndex; j <= endIndex && j < lines.size(); j++)
			{
				DrawableText line = lines.get(j);
	
				line.setPosition(textX, textY);
				line.draw(deltaTime);
				
				textY -= line.getHeight();
			}
		}
	}
	
	
	public boolean consoleFull()
	{
		if(lineIndex >= 0)
		{
			int linesHeight = 0;
			int i = lineIndex;
			
			while(linesHeight < background.getHeight() && i < lines.size())
			{
				linesHeight += lines.get(i++).getHeight();
			}	
			
			return linesHeight >= background.getHeight();
		}
		else 
			return false;
	}
	
	@Override
	public List<Vertex> getVertices() {
		return null;
	}

    public void insertLine(String string)
    {
    	DrawableText line = RenderingSystem.generateText(string, background.getPosition());
     	line.setLayer(super.getLayer());
     	
    	lines.add(line);    	
    	if(consoleFull() || lineIndex == -1)
    		lineIndex++;
    }


	public void insertLine(DrawableText line) 
	{
		lines.add(line);
		if(consoleFull() || lineIndex == -1)
			lineIndex++;
	}
    
    public void scrollDown()
    {
        if (lineIndex < lines.size()-1)
        	lineIndex++;
    }

    public void scrollUp()
    {
        if (lineIndex > 0)
        	lineIndex--;
    }

    public void clear()
    {
        lines.clear();
        lineIndex = -1;
    }

    public String getStringClicked(Point point)
    {
    	DrawableText line = getTextClicked(point);
		return line != null ? line.getText() : "";
    }

    public DrawableText getTextClicked(Point point)
    {		int startIndex = 0;
		int linesHeight = 0;
		for(int i=lineIndex-1; i>0; i--)
		{
			linesHeight += lines.get(startIndex+i).getHeight();
			
			if(linesHeight >= background.getHeight())
			{
				startIndex = i;
				i = 0;
			}
		}
		
		//draw the lineLimit amount of texts starting from the bottom going up
		int i=0;
		while(startIndex+i < lines.size())
		{
			DrawableText line = lines.get(startIndex+i);

			if(point.x >= background.getPosition().x && point.x <= line.getWidth()+background.getPosition().x)
					if(point.y <= line.getPosition().y && point.y >= line.getPosition().y - line.getHeight())
					{
						Util.DebugLog.writeError(line.getText());
							return line;
					}
			i++;
		}				
		return null;
    }
    
    public ArrayList<DrawableText> getLines()
    {
    	return lines;
    }

	public int lineCount() 
	{
		return lines.size();
	}
	
    @Override
    public void setVisible(boolean visible) {
    	super.setVisible(visible);
    	background.setVisible(visible);
    }    
    
    @Override
    public void setWidth(float value) {
    	super.setWidth(value);    	
    	background.setWidth(value);
    }
    
    @Override
    public void setHeight(float value) {
    	super.setHeight(value);
    	background.setHeight(value);
    }
    
    @Override
    public void setPosition(float x, float y) {
    	super.setPosition(x, y);
    	background.setPosition(x, y);
    }
    
    @Override
    public void setPosition(Point point) {
    	super.setPosition(point);
    	background.setPosition(point);
    }
    
    @Override
    public void setColor(float r, float g, float b, float a) {
    	super.setColor(r, g, b, a);
    	background.setColor(r, g, b, a);
    }
    
    @Override
    public void setColor(Color color) {
    	super.setColor(color);
    	background.setColor(color);
    }
    
    @Override
    public void setColorAlpha(float alpha) {
    	super.setColorAlpha(alpha);
    	background.setColorAlpha(alpha);
    }
    
    @Override
    public void setLayer(DRAW_LAYER layer) 
    {
    	super.setLayer(layer);
    	background.setLayer(layer);
    	for(DrawableText text : lines)
    		text.setLayer(layer);
    }
}