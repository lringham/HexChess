package Font;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import RenderingSystem.RenderingSystem;
import RenderingSystem.Texture;
import Util.Point;
import Util.Vector3;

public class Font
{
	protected static Map<Integer, Character> characters = new HashMap<Integer, Character>();	
	protected String fontLocation = "Assets\\Art\\fonts\\";
	private String textureName;
	private Vector3 textureSize;
	private String face;
	private Texture texture;
	
	public Font(String font)
	{
		loadFont(font);
	}
	
	public void loadFont(String filename)
	{
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(fontLocation + filename));
			String line;
			
			while((line = reader.readLine()) != null)
				parseLine(line);
			
			reader.close();
		}
		catch(IOException e)
		{
			Util.DebugLog.writeError("Font not loaded: \n"+e.getMessage());
		}
	}

	public int getNumCharacters()
	{
		return characters.size();
	}
	
	public Collection<Character> getCharacters()
	{
		return characters.values();
	}
	
	public Character getCharacter(int character)
	{
		return characters.get(character);
	}
	
	public Character getCharacter(char ch)
	{
		return characters.get(ch);
	}
		
	public float[] toFloatList()
	{	
		ArrayList<Float> floatArray = new ArrayList<Float>();
		for(Character ch : getCharacters())
		{		
			float x = ch.texturePosition.x;
			float y = ch.texturePosition.y;

			float sizeX = ch.textureScale.x;
			float sizeY = ch.textureScale.y;
			
			floatArray.add(-.5f); 							//X
			floatArray.add(-.5f); 							//Y			
			floatArray.add(0f);								//Z
			floatArray.add(x);								//U
			floatArray.add(y + sizeY);						//V
			
			floatArray.add(-.5f);				 			//X
			floatArray.add(.5f ); 							//Y	
			floatArray.add(0f);								//Z
			floatArray.add(x);								//U
			floatArray.add(y);								//V		
			
			floatArray.add(.5f); 							//X
			floatArray.add(.5f); 							//Y	
			floatArray.add(0f);								//Z
			floatArray.add(x + sizeX);						//U
			floatArray.add(y);								//V	
			
			floatArray.add(.5f); 							//X
			floatArray.add(-.5f);				 			//Y	
			floatArray.add(0f);								//Z
			floatArray.add(x + sizeX);						//U
			floatArray.add(y + sizeY);						//V	
		}
		
		
		float[] floatList = new float[floatArray.size()];
		int i = 0;

		for (Float f : floatArray) 
		{
			floatList[i++] = (f != null ? f : Float.NaN);
		}
		
		return floatList;		
	}
	
	private void parseLine(String line)
	{
		String[] lineSegments = line.split("\\s+");
		
		switch(lineSegments[0])
		{
			case "info":
				face = lineSegments[1].split("\"")[1];
				break;
			case "common":
				textureSize = new Vector3(Float.valueOf(lineSegments[3].split("=")[1]), Float.valueOf(lineSegments[4].split("=")[1]), 1f);
				break;
			case "page":
				textureName = lineSegments[2].split("\"")[1];
				texture = RenderingSystem.getTexture(textureName.replace(".png", ""));
				break;
			case "chars":
				 characters = new HashMap<Integer,Character>(Integer.valueOf(lineSegments[1].split("=")[1]));
				break;
			case "char":
				
				int ID = Integer.valueOf(lineSegments[1].split("=")[1]);
				Point position 	= new Point(Float.valueOf(lineSegments[2].split("=")[1]), Float.valueOf(lineSegments[3].split("=")[1]));
				Vector3 size 	= new Vector3(Float.valueOf(lineSegments[4].split("=")[1]), Float.valueOf(lineSegments[5].split("=")[1]), 1f);
				Point offset 	= new Point(Float.valueOf(lineSegments[6].split("=")[1]), Float.valueOf(lineSegments[7].split("=")[1]));
				float xAdvance 	= Float.valueOf(lineSegments[8].split("=")[1]);				
								
				position.x = position.x / textureSize.x;
				position.y = position.y / textureSize.y;
				
				Character newChar = new Character(ID, position, size, texture, offset, xAdvance);
				characters.put(ID, newChar);
		}
	}

	public String getFace()
	{		
		return face;
	}

	public String getTextureName()
	{
		return textureName;
	}
}
