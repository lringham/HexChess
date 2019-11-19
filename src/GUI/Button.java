package GUI;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

import RenderingSystem.Animation;
import RenderingSystem.Drawable;
import RenderingSystem.Sprite;
import RenderingSystem.Vertex;
import RenderingSystem.RenderingSystem.DRAW_LAYER;
import Sound.Sound;
import Util.Color;
import Util.Point;
import Util.TimeInterval;

public class Button extends Drawable implements Clickable 
{
	enum ButtonSprite {UP, DOWN};
	ButtonSprite buttonSprite = ButtonSprite.UP;
	
	ArrayList<Sprite> sprites = new ArrayList<Sprite>();
	public TimeInterval clickRate = new TimeInterval(250);
	public Sprite upSprite;
	public Sprite downSprite;
	public Sprite activeSprite;
	public Sound clickSound = null;
	
	Button()
	{

	}
	
	public Button(Sprite upSprite,Sprite downSprite, Sound clickSound)
	{
		this.upSprite = upSprite;
		this.downSprite = downSprite;
		sprites.add(upSprite);
		sprites.add(downSprite);
		
		activeSprite = upSprite;
		
		this.clickSound = clickSound;
	}
	
	public Button(Sprite upSprite,Sprite downSprite)
	{
		this.upSprite = upSprite;
		this.downSprite = downSprite;
		sprites.add(upSprite);
		sprites.add(downSprite);
		
		activeSprite = upSprite;
	}
	
	public Button(Sprite upSprite, Sound clickSound)
	{
		this.upSprite = upSprite;
		activeSprite = upSprite;
		sprites.add(upSprite);
		
		this.clickSound = clickSound;
	}
	
	public Button(Sprite upSprite)
	{
		this.upSprite = upSprite;
		sprites.add(upSprite);
		
		activeSprite = upSprite;
	}
	
	@Override
	public boolean clicked(Point point) 
	{
		boolean isClicked = false;
		
		if(activeSprite.clicked(point))
		{
			isClicked = clickRate.elapsed();		
		}
		
		if(isClicked)
		{
			if(clickSound != null)				
				clickSound.play();
			
			if(isUp())
				swapSprites();
		}
		
		return isClicked;
	}
	
	public void setWidth(float value)
	{
		super.setWidth(value);
		
		for(Sprite sprite : sprites)
			sprite.setWidth(value);
		//downSprite.getScale().x = value;
	}
	
	public void setHeight(float value)
	{
		super.setHeight(value);
		for(Sprite sprite : sprites)
			sprite.setHeight(value);
	}

	public void setPosition(float x, float y)
	{
		super.setPosition(x, y);
		for(Sprite sprite : sprites)
			sprite.setPosition(x,y);		
	}
	
	public void setPosition(Point point)
	{
		super.setPosition(point);
		for(Sprite sprite : sprites)
			sprite.setPosition(point);
	}
	
	public void setColor(float r, float g, float b, float a)
	{
		super.setColor(r, g, b, a);
		
		for(Sprite sprite : sprites)
			sprite.setColor(r, g, b, a);
	}

	public void setColor(Color color)
	{
		super.setColor(color);
		
		for(Sprite sprite : sprites)
			sprite.setColor(color);
	}
	
	public void setVisible(boolean visible)
	{
		super.setVisible(visible);
		
		for(Sprite sprite : sprites)
			sprite.setVisible(visible);
	}
	
	public float getWidth()
	{
		return activeSprite.getWidth();
	}
	
	public float getHeight()
	{
		return activeSprite.getHeight();
	}
	
	private void swapSprites()
	{
		if(downSprite != null)
		{
			activeSprite = activeSprite == upSprite ? downSprite : upSprite;			
		}
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
		
		activeSprite.draw(deltaTime);
		
		if(!Mouse.isButtonDown(0) && isDown())
			swapSprites();		
	}

	public boolean isDown() 
	{
		return activeSprite == downSprite;
	}

	public boolean isUp() 
	{
		return !isDown();
	}
	
	@Override
	public void setRotationAroundCentre(float angle) {
		upSprite.setRotationAroundCentre(angle);
		downSprite.setRotationAroundCentre(angle);
	}
	
	@Override
	public List<Vertex> getVertices() {
		return activeSprite.getVertices();
	}
	
	@Override
	public void setLayer(DRAW_LAYER layer) 
	{
		super.setLayer(layer);
		upSprite.setLayer(layer);
		downSprite.setLayer(layer);
	}
}
