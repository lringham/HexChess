package RenderingSystem;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.BufferUtils;

public class VertexBuffer 
{
	public int maxSizeBytes = 0;
	public ArrayList<ArrayList<Float>> floatBuffers = new ArrayList<ArrayList<Float>>();
	public ArrayList<Float> floatBuffer = new ArrayList<Float>();
	public FloatBuffer buffer;
	
	public VertexBuffer(int max)
	{		
		maxSizeBytes = max;
		buffer = BufferUtils.createFloatBuffer(1);
		floatBuffers.add(floatBuffer);
	}	

	void put(Float f)
	{
		floatBuffer.add(f);
	}

	public void put(Float[] verts) 
	{
		floatBuffer.addAll(Arrays.asList(verts));
	}
	
	public void flip()
	{
		float[] floats = new float[floatBuffer.size()];
		
		for(int i=0;  i<floatBuffer.size(); i++)
			floats[i] = floatBuffer.get(i);
		
		buffer = BufferUtils.createFloatBuffer(floats.length);
		buffer.put(floats, 0, floats.length);
		buffer.flip();
	}

	public void clear()
	{
		for(ArrayList<Float> buff : floatBuffers)
			buff.clear();
		
		floatBuffers.clear();
		buffer.clear();
		
		ArrayList<Float> newBuff = new ArrayList<Float>();
		floatBuffers.add(newBuff);
		floatBuffer = newBuff;
	}

	public void newBuffer()
	{
		ArrayList<Float> newBuff = new ArrayList<Float>();
		floatBuffers.add(newBuff);
		floatBuffer = newBuff;
	}
	
	public void setBuffer(int i) 
	{
		buffer.clear();
		floatBuffer = floatBuffers.get(i);
	}
		
	public int getSize()
	{
		return floatBuffer.size();
	}
	
	public int getSizeBytes()
	{
		return floatBuffer.size() * RenderingSystem.BYTES_IN_FLOAT;
	}
	
	public int getBufferCount()
	{
		return floatBuffers.size();
	}
}
