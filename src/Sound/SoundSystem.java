package Sound;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

public class SoundSystem 
{
	static ArrayList<Sound> sounds =  new ArrayList<Sound>();
	static Map<String, IntBuffer> buffers = new HashMap<String, IntBuffer>();
	static String soundLocation = "Assets\\Sounds\\";

	public static void initialize()
	{
		try 
		{
			AL.create();
			AL10.alListener3f(AL10.AL_POSITION,0f,0f,0f);
		  	AL10.alListener3f(AL10.AL_VELOCITY,0f,0f,0f);
		  	//AL10.alListener(AL10.AL_ORIENTATION, BufferUtils.createFloatBuffer(6).put(new float[] { 0.0f, 0.0f, -1.0f,  0.0f, 1.0f, 0.0f }));
		}
		catch (LWJGLException e)
		{
			e.printStackTrace();
		}
	}
	
	public static Sound generateSound(String name)
	{
		Sound sound = loadSound(name);		
		sounds.add(sound);
		return sound;
	}
	
	public static void playSound(Sound sound)
	{
		AL10.alSourcePlay(sound.source.get(0));
	}
	
	public static void stopSound(Sound sound)
	{
		AL10.alSourceStop(sound.source.get(0));
	}
	
	public static void pauseSound(Sound sound)
	{
		AL10.alSourcePause(sound.source.get(0));
	}
		
	private static Sound loadSound(String fileName)
	{
		IntBuffer buffer = null;
		
		if(buffers.containsKey(fileName))
			buffer = buffers.get(fileName); 	
		else
		{
			buffer = BufferUtils.createIntBuffer(1);
			AL10.alGenBuffers(buffer);
			
			WaveData waveFile;
			try 
			{
				waveFile = WaveData.create(new BufferedInputStream(new FileInputStream(soundLocation + fileName)));		
				AL10.alBufferData(buffer.get(0), waveFile.format, waveFile.data, waveFile.samplerate);		
				waveFile.dispose();
			}
			catch (FileNotFoundException e) 
			{
				e.printStackTrace();
			}
			
			buffers.put(fileName, buffer);
		}
		
		// Bind the buffer with the source.
		IntBuffer source = BufferUtils.createIntBuffer(1);		
		AL10.alGenSources(source);
		AL10.alSourcei(source.get(0), AL10.AL_BUFFER, buffer.get(0));
		AL10.alSourcef(source.get(0), AL10.AL_PITCH, 1.0f);
		AL10.alSourcef(source.get(0), AL10.AL_GAIN, 1.0f);
		AL10.alSource3f(source.get(0), AL10.AL_POSITION, 0f, 0f, 0f);
		AL10.alSource3f(source.get(0), AL10.AL_VELOCITY, 0f, 0f, 0f);
		
		Sound sound = new Sound(buffer, source);		
		return sound;
	}
	
	public static void destroy()
	{
		killALData();
		AL.destroy();
	}
	/**
	 * void killALData()
	 *
	 *  We have allocated memory for our buffers and sources which needs
	 *  to be returned to the system. This function frees that memory.
	 */
	private static void killALData() 
	{
		for(Sound sound : sounds)
		{
			AL10.alDeleteSources(sound.source);
			AL10.alDeleteBuffers(sound.buffer);
		}
		sounds.clear();
	}
}
