package Sound;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class Sound 
{
	protected float volume = 1f;
	protected boolean loop = false;
	public IntBuffer buffer = BufferUtils.createIntBuffer(1);
	public IntBuffer source = BufferUtils.createIntBuffer(1);
	
	public Sound(IntBuffer buffer, IntBuffer source)
	{
		this.buffer = buffer;
		this.source = source;
	}
	
	public void play()
	{
		SoundSystem.playSound(this);
	}
	
	public void stop()
	{
		SoundSystem.stopSound(this);
	}
	
	public void pause()
	{
		SoundSystem.pauseSound(this);	
	}
	
	public void setLooping(boolean value)
	{
		loop = value;
	}
	
	public boolean getLooping()
	{
		return loop;
	}
}
