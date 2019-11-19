package RenderingSystem;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.BufferUtils;

public class ElementBuffer 
{
	public IntBuffer buffer;
	public ArrayList<ArrayList<Integer>> intBuffers = new ArrayList<ArrayList<Integer>>();
	public ArrayList<Integer> intBuffer = new ArrayList<Integer>();
	public int maxSizeBytes = 0;
	
	public ElementBuffer(int max)
	{
		maxSizeBytes = max;
		buffer = BufferUtils.createIntBuffer(1);
		intBuffers.add(intBuffer);
	}	
	
	public void put(int i)
	{
		intBuffer.add(i);	
	}
	
	public void put(Integer[] elements) 
	{
		intBuffer.addAll(Arrays.asList(elements));
	}
	
	public void flip()
	{
		int[] ints = new int[intBuffer.size()];
		
		for(int i=0; i<intBuffer.size(); i++)
			ints[i] = intBuffer.get(i).intValue();
			
		buffer = BufferUtils.createIntBuffer(ints.length);
		buffer.put(ints, 0, ints.length);
		buffer.flip();
	}

	public void clear()
	{
		for(ArrayList<Integer> buff : intBuffers)
			buff.clear();
		
		intBuffers.clear();
		buffer.clear();
		
		ArrayList<Integer> newBuff = new ArrayList<Integer>();
		intBuffers.add(newBuff);
		intBuffer = newBuff;
	}
	
	public void newBuffer()
	{
		ArrayList<Integer> newBuff = new ArrayList<Integer>();
		intBuffers.add(newBuff);
		intBuffer = newBuff;
	}

	public int getSize()
	{
		return intBuffer.size();
	}
	
	public int getSizeBytes()
	{
		return intBuffer.size() * RenderingSystem.BYTES_IN_INT;
	}
	
	public int getBufferCount()
	{
		return intBuffers.size();
	}

	public void setBuffer(int i) 
	{
		intBuffer = intBuffers.get(i);
	}
}
