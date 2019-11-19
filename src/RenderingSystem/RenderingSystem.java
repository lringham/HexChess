package RenderingSystem;
import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CCW;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glFrontFace;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STREAM_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetAttribLocation;
import static org.lwjgl.opengl.GL20.glGetShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.util.glu.GLU.gluErrorString;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import Font.Character;
import Font.Font;
import GUI.DrawableText;
import Game.Hex;
import Util.Color;
import Util.Mat4;
import Util.Point;
import Util.Screen;


public class RenderingSystem
{
	public static Color backgroundColor = new Color(.25f, .25f, 1f);
		
	private static Map<String, Texture> textures = new HashMap<String, Texture>();
	private static Map<DRAW_LAYER, Map<Texture, DrawLayer>> layers = new HashMap<DRAW_LAYER, Map<Texture, DrawLayer>>(); 
	private static Font font;

	static List<DrawableText> drawableTexts = new ArrayList<DrawableText>();
	
	private static final int BYTES_PER_PIXEL = 4;//3 for RGB, 4 for RGBA
	private static final String ART_ASSETS = "Assets\\Art\\";
	
	private static Mat4 projMatrix;
	
	private static int vbo;
	private static int vao;
	private static int ebo;	
	
	private static int vertShader;
	private static int fragShader;
	private static int shaderProgram;
	
	private static long drawCallCount = 0;
	
	//uniforms
	private static int projMatUniform;
	
	//attributes
	private static int positionAttr;
	private static int colorAttr;
	private static int textureAttr;
	
	public static final int BYTES_IN_VBO = 9000;
	public static final int BYTES_IN_EBO = 3000;
    public static final int BYTES_IN_FLOAT;
    public static final int BYTES_IN_INT;
    public static DrawableText drawCallText = null;
    
    public static enum DRAW_LAYER {BACKGROUND, FOREGROUND0, FOREGROUND1, FOREGROUND2, FOREGROUND3};
    static {
    	BYTES_IN_INT = Integer.SIZE / Byte.SIZE;
        BYTES_IN_FLOAT = Float.SIZE / Byte.SIZE;
    }
    
    static public Texture hexTexture;
    
	/**
	 * @param args
	 */
	@SuppressWarnings("deprecation")
	public static void initialize()
	{        
		loadTextures();
		font = new Font("outlinedFont.fnt");
        
		for(DRAW_LAYER layer : DRAW_LAYER.values())
		{
			Map<Texture, DrawLayer> textureMap = new HashMap<Texture, DrawLayer>();
			for(Texture texture : textures.values())
			{				
				textureMap.put(texture, new DrawLayer(new VertexBuffer(BYTES_IN_VBO), new ElementBuffer(BYTES_IN_EBO)));
				layers.put(layer, textureMap);
			}
		}
		
        //Set the openGL state
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glEnable(GL_CULL_FACE);
        glBlendFunc (GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);        
        glFrontFace(GL_CCW);
        glCullFace(GL_BACK);        
        
        projMatrix = Mat4.orthoProjColumnMajor(0f, Screen.getWidth(), 0f, Screen.getHeight(), 1f, -1f);
        
    		/**********************************************************************************
    		* Create the shader program
    		**********************************************************************************/               	
    		//Create Vertex and Fragment shaders        
    		vertShader = createShader(GL_VERTEX_SHADER, readShaderCode("Assets\\Shaders\\shader.vert"));
    		fragShader = createShader(GL_FRAGMENT_SHADER, readShaderCode("Assets\\Shaders\\shader.frag"));

    		if(glGetShader(vertShader, GL_COMPILE_STATUS) == GL_FALSE)		
    			Util.DebugLog.writeError("Vertex shader compilation failed.");
    		else
    			Util.DebugLog.writeError("Vertex shader compilation successful.");
    		Util.DebugLog.writeError(glGetShaderInfoLog(vertShader, 1024));
    		
    		if(glGetShader(fragShader, GL_COMPILE_STATUS) == GL_FALSE)		
    			Util.DebugLog.writeError("Fragment shader compilation failed.");
    		else
    			Util.DebugLog.writeError("Fragment shader compilation successful.");
    		Util.DebugLog.writeError(glGetShaderInfoLog(fragShader, 1024));
    		
    		//Create shaderProgram
    		shaderProgram = glCreateProgram();    		
    		glAttachShader(shaderProgram, vertShader);    		
    		glAttachShader(shaderProgram, fragShader);    		
                     
    		glLinkProgram(shaderProgram);    		
    		glValidateProgram(shaderProgram);
    		    		
    		//Use the program
    		glUseProgram(shaderProgram);
    		
    		//cleanup
    		glDetachShader(shaderProgram, vertShader);
    		glDetachShader(shaderProgram, fragShader);
    		glDeleteShader(vertShader);
    		glDeleteShader(fragShader);
    		
    		/**********************************************************************************
    		* Create the vertex buffer objects
    		**********************************************************************************/	
			/* GL_STATIC_DRAW: The vertex data will be uploaded once and drawn many times (e.g. the world).
			 * GL_DYNAMIC_DRAW: The vertex data will be changed from time to time, but drawn many times more than that.
			 * GL_STREAM_DRAW: The vertex data will change almost every time it's drawn (e.g. user interface).   */
    		
            setupVBOs();    		
            checkGLError();
            
            drawCallText = generateText("Draw Call Count:", new Point(Screen.getWidth() - 170, Screen.getHeight() - 40));
            drawCallText.setPosition(Screen.getWidth() - (drawCallText.getWidth()+10), Screen.getHeight() - 40);
            drawCallText.setLayer(DRAW_LAYER.FOREGROUND1);
    }
	
	private static void loadTextures() 
	{
		File folder = new File(ART_ASSETS);
		File[] listOfFiles = folder.listFiles();
		if(listOfFiles == null)
		{
			Util.DebugLog.writeError("Can't find assets folder");
			throw new RuntimeException("Can't find assets folder");
		}
		else
		{
		    for (int i = 0; i < listOfFiles.length; i++) 
		    {
				if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith(".png"))  
					loadTexture(listOfFiles[i].getName());		      
		    }
		}
	}

	private static String readShaderCode(String shaderFile)
	{
		BufferedReader reader;
		String line = "";
		StringBuilder shader = new StringBuilder();
		
		try
		{
			reader = new BufferedReader(new FileReader(shaderFile));
			while((line = reader.readLine()) != null)
				shader.append(line).append("\n");
			reader.close();
		}
		catch(IOException e)
		{
			Util.DebugLog.writeError("Shader"+""+" not loaded\n"+e.getMessage());
		}
		
		return shader.toString(); 
	}
	
	public static void startDraw()
	{
		glClearColor(backgroundColor.red, backgroundColor.green, backgroundColor.blue, backgroundColor.alpha);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);	
				
		for(Map<Texture, DrawLayer> layerMap : layers.values())
		{
			for(DrawLayer layer: layerMap.values())
			{
				layer.vertBuff.clear();
				layer.elemBuff.clear();
			}
		}
	}
	
	public static void draw(Drawable drawable)
	{
		if(drawable.getVisible())
		{
			ElementBuffer elemBuff = layers.get(drawable.getLayer()).get(drawable.getTexture()).elemBuff;
			VertexBuffer vertBuff = layers.get(drawable.getLayer()).get(drawable.getTexture()).vertBuff;			
						
			int vertByteCount = drawable.getVertices().size() * Vertex.SIZE_IN_BYTES;
			int elemByteCount = drawable.getElements().length * BYTES_IN_INT;
			
			//If the current buffer byte limit is reached start a new one.
			if((vertBuff.getSizeBytes() + vertByteCount > BYTES_IN_VBO) || (elemBuff.getSizeBytes() + elemByteCount > BYTES_IN_EBO))
			{
				vertBuff.newBuffer();
				elemBuff.newBuffer();
			}			
			int elementIndexOffset = vertBuff.getSize() / Vertex.SIZE;
			
			for(Vertex vertex : drawable.getVertices())			
				vertBuff.put(vertex.asFloats());

			for(int i : drawable.getElements())
				elemBuff.put(i+elementIndexOffset);				
		}
	}
		
	public static void draw(DrawableText text)
	{
		Point cursorPos = new Point();
		cursorPos.x = text.getPosition().x;
		cursorPos.y = text.getPosition().y;
		
		String string = text.toString();
		Font font = text.getFont();
		
		for(int i=0; i < string.length(); i++)
		{
			Character character = font.getCharacter((int)string.charAt(i));
			character.setLayer(text.getLayer());
			character.setColor(text.getColor());
			
			character.setPosition(
					(cursorPos.x + character.getXOffset()),
					(cursorPos.y - character.getYOffset())
				);
			
			draw(character);
			cursorPos.x += character.getXAdvance();			
		}	
	}
	
	public static void endDraw()
	{
		glUseProgram(shaderProgram);		
		glBindVertexArray(vao);
		
		draw(drawCallText);
		for(DRAW_LAYER layerType : DRAW_LAYER.values())
		{
			Map<Texture, DrawLayer> layerMap = layers.get(layerType);			
			for(Texture texture : layerMap.keySet())
			{	
				VertexBuffer vertexBuffer = layerMap.get(texture).vertBuff;
				ElementBuffer elementBuffer = layerMap.get(texture).elemBuff;
						
				for(int i=0; i<vertexBuffer.getBufferCount(); i++)
				{
					vertexBuffer.setBuffer(i);
					elementBuffer.setBuffer(i);
					
					vertexBuffer.flip();
					elementBuffer.flip();

					if(vertexBuffer.getSize() > 0)
					{
						glBufferSubData(GL_ARRAY_BUFFER, 0, vertexBuffer.buffer);
						glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, 0, elementBuffer.buffer);
						
						glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
						glDrawElements(GL_TRIANGLES, elementBuffer.getSize(), GL_UNSIGNED_INT, 0);
						drawCallCount++;
					}
				}
			}
		}
		
		drawCallText.setText("Draw Calls:"+drawCallCount);	
		drawCallText.setPosition(Screen.getWidth() - (drawCallText.getWidth()+10),Screen.getHeight() - 40);
		drawCallCount = 0;
		
		checkGLError();		
		glUseProgram(0);			
	    Display.update();
	}
		
	static int createShader(int glShaderType, String sourceCode)
	{
		int shaderID = glCreateShader(glShaderType);
		glShaderSource(shaderID, sourceCode);
		glCompileShader(shaderID);
		
		return shaderID;
	}
	
	public static AnimatedSprite generateAnimatedSprite(String textureName)
	{
		//AnimatedSprite sprite = null;
		//drawables.add(sprite);
		throw new RuntimeException("Unimplimented Method: generateAnimatedSprite");
	}
	
	public static Sprite generateSprite(String textureName)
	{
		Sprite sprite = new Sprite(getTexture(textureName));			        		
		sprite.setPosition(0f, sprite.getHeight());
		sprite.setLayer(RenderingSystem.DRAW_LAYER.FOREGROUND1);
		return sprite;			
	}
	
	public static Hex generateHex(String textureName)
	{	   		    
		Hex hex = new Hex(getTexture(textureName));				
		hex.setLayer(RenderingSystem.DRAW_LAYER.FOREGROUND0);
		return hex;			
	}
	
	public static DrawableText generateText(String text)
	{
		DrawableText drawableText = new DrawableText(font, text);
		drawableTexts.add(drawableText);
		drawableText.setLayer(DRAW_LAYER.FOREGROUND2); 
		return drawableText;
	}
	
	public static DrawableText generateText(String text, Point position)
	{
		DrawableText drawableText = new DrawableText(font, text, position);
		drawableTexts.add(drawableText);
		drawableText.setLayer(DRAW_LAYER.FOREGROUND2); 
		return drawableText;
	}
	
	public static DrawableText generateText(String textString, Point position, Color color)
	{
		DrawableText tempText = new DrawableText(font, textString, position, color);
		drawableTexts.add(tempText);
		return tempText;
	}
	
	public static Texture getTexture(String textureName)
	{
		if(textures.containsKey(textureName))
			return textures.get(textureName);
		else
		{
			Util.DebugLog.writeError("Texture does not exist: "+textureName);
			return textures.get("default");
		}
	}
	
	private static Texture loadTexture(String imageName)
	{		
		if (textures.containsKey(imageName)) //check if the texture already exists			
			return textures.get(imageName);
		else
		{
			Texture texture = null;
			BufferedImage image = null; 
			
			try
			{
				image = ImageIO.read(new File(ART_ASSETS+imageName)); //read the image
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			int[] pixels = new int[image.getWidth() * image.getHeight()];
			image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
			ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * BYTES_PER_PIXEL);

			for (int y = 0; y < image.getHeight(); y++)
			{
				for (int x = 0; x < image.getWidth(); x++)
				{					
					//PIXEL = [AAAA AAAA RRRR RRRR GGGG GGGG BBBB BBBB]
					//		   31                                    0
					
					int pixel = pixels[y * image.getWidth() + x];
					buffer.put((byte) ((pixel >> 16) & 0xFF));    // Red component, the third 2 bytes
					buffer.put((byte) ((pixel >> 8) & 0xFF));     // Green component, the second 2 bytes
					buffer.put((byte) (pixel & 0xFF));            // Blue component, the first 2 bytes
					buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA, the last 2 bytes
				}
			}
			
			buffer.flip(); //Prepair the buffer for reading instead of writing  

			int textureID = GL11.glGenTextures(); //Generate texture ID
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID); //Bind texture ID

			//Setup wrap mode
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S,
					GL12.GL_CLAMP_TO_EDGE);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T,
					GL12.GL_CLAMP_TO_EDGE);

			//Setup texture scaling filtering
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			GL30.glGenerateMipmap(GL_TEXTURE_2D);
			
			//Send texel data to OpenGL
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8,
					image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA,
					GL11.GL_UNSIGNED_BYTE, buffer);
						
			texture = new Texture(textureID, imageName.replace(".png", ""), image.getWidth(), image.getHeight());
			textures.put(imageName.replace(".png", ""), texture);
			
			//Return the texture so we can bind to it later
			return texture;
		}
	}	
		
	public static void destroy()
	{
		glDeleteBuffers(vbo);
		glDeleteBuffers(ebo);
		glDeleteBuffers(vao);
		glDeleteProgram(shaderProgram);		
		glDeleteVertexArrays(vao);
		Display.destroy();	
	}
		
	@SuppressWarnings("unused")
	private static FloatBuffer asFloatBuffer(float[] verts)
	{
		FloatBuffer buff = BufferUtils.createFloatBuffer(verts.length);
		for (Float f : verts)
			buff.put(f);
		buff.flip();
		
		return buff;
	}
		
	private static FloatBuffer asFloatBuffer(Mat4 verts)
	{
		FloatBuffer buff = BufferUtils.createFloatBuffer(16);
		for (Float f : verts.getElements())
			buff.put(f);
		buff.flip();
		
		return buff;
	}
	
	@SuppressWarnings("unused")
	private static IntBuffer asIntBuffer(int[] allElements)
	{
		IntBuffer buff = BufferUtils.createIntBuffer(allElements.length);
		for (int i : allElements)
			buff.put(i);
		buff.flip();
		
		return buff;
	}

	private static  void setupVBOs()
	{
		//Create a vao to remember the links between attributes and the VBO
		vao = glGenVertexArrays();    		
		glBindVertexArray(vao);
		
		//Create a vbo to hold the vertex information
		vbo = glGenBuffers(); 		  
		glBindBuffer(GL_ARRAY_BUFFER, vbo);			

		//Create a ebo to hold the element information
		ebo = glGenBuffers(); 		  
		glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo);		
		
		//get the attribute locations
		positionAttr = glGetAttribLocation(shaderProgram, "position");            
		colorAttr = glGetAttribLocation(shaderProgram, "color");
		textureAttr = glGetAttribLocation(shaderProgram, "textureCoord"); 

		//get uniform locations
		projMatUniform = glGetUniformLocation(shaderProgram, "proj");
		glUniformMatrix4(projMatUniform, false, asFloatBuffer(projMatrix));
		
		//tell openGL how our attributes are formatted and ordered.
		glEnableVertexAttribArray(positionAttr);
		glEnableVertexAttribArray(colorAttr);
		glEnableVertexAttribArray(textureAttr);
		    		
		boolean normalize = false;	    		

		glVertexAttribPointer(positionAttr, 3, GL11.GL_FLOAT, normalize, Vertex.SIZE_IN_BYTES, 0);    		
		glVertexAttribPointer(colorAttr, 4, GL11.GL_FLOAT, normalize, Vertex.SIZE_IN_BYTES, BYTES_IN_FLOAT*3);
		glVertexAttribPointer(textureAttr, 2, GL11.GL_FLOAT, normalize, Vertex.SIZE_IN_BYTES, BYTES_IN_FLOAT*7);
		
		//Create a vbo for vertex information
		glBufferData(GL_ARRAY_BUFFER, BYTES_IN_VBO, GL_STREAM_DRAW);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, BYTES_IN_EBO, GL_STREAM_DRAW);
	}

	public static float[] concat(float[] f1, float[]f2)
	{
		float[] floats = new float[f1.length + f2.length]; 
		int i=0;
		
		for(float f : f1)
			floats[i++] = f;
		
		for(float f : f2)
			floats[i++] = f;
		
		return floats;
	}
	
	public static int[] concat(int[] i1, int[] i2)
	{
		int[] ints = new int[i1.length + i2.length]; 
		int i=0;
		
		for(int ele : i1)
			ints[i++] = ele;
		
		for(int ele : i2)
			ints[i++] = ele;
		
		return ints;
	}
	
	public static short[] concat(short[] b1, short[]  b2)
	{
		short[] shorts = new short[b1.length + b2.length]; 
		int i=0;
		
		for(short ele : b1)
			shorts[i++] = ele;
		
		for(short ele : b2)
			shorts[i++] = ele;
		
		return shorts;
	}
	
	public static byte[] concat(byte[] b1, byte[]  b2)
	{
		byte[] bytes = new byte[b1.length + b2.length]; 
		int i=0;
		
		for(byte ele : b1)
			bytes[i++] = ele;
		
		for(byte ele : b2)
			bytes[i++] = ele;
		
		return bytes;
	}
	
	/*public void loadTexture(String FileName)
	{
		//Generate a texture object
		GLuint textureID;
		glGenTextures(1, &textureID);
		glBindTexture(GL_TEXTURE_2D, textureID);
		
		//Set texture filtering methods S,T are aliases for X,Y of the texture
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		*/
						/*GL_CLAMP_TO_EDGE: The coordinate will simply be clamped between 0 and 1.
						* GL_CLAMP_TO_BORDER: The coordinates that fall outside the range will be given a specified border color.
						* GL_REPEAT: The integer part of the coordinate will be ignored and a repeating pattern is formed.
						* GL_MIRRORED_REPEAT: The texture will also be repeated, but it will be mirrored when the integer part of the coordinate is odd.*/
		/*
		//Enable mipmaps
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);							// When MAGnifying the image (no bigger mipmap available), use LINEAR filtering
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR); 	// When MINifying the image, use a LINEAR blend of two mipmaps, each filtered LINEARLY too
		glGenerateMipmap(GL_TEXTURE_2D);
		*/
						/*GL_NEAREST_MIPMAP_NEAREST: Uses the mipmap that most closely matches the size of the pixel being textured and samples with nearest neighbour interpolation.
						* GL_LINEAR_MIPMAP_NEAREST: Samples the closest mipmap with linear interpolation.
						* GL_NEAREST_MIPMAP_LINEAR: Uses the two mipmaps that most closely match the size of the pixel being textured and samples with nearest neighbour interpolation.
						* GL_LINEAR_MIPMAP_LINEAR: Samples closest two mipmaps with linear interpolation.*/
		/*
		//Load the texture image
		glTexImage2D(GL_TEXTURE_2D, levelOfDetail, GL_RGB, width, height, 0, GL_RGB, GL_FLOAT, pixels); //0 (or 1) is for enabling a border		
		textures.add(line, textureID);		
	}	*/
	
	private static void checkGLError()
	{
		int error;
		while((error = glGetError()) != GL_NO_ERROR)
			Util.DebugLog.writeError("OpenGL Error: " + gluErrorString(error));
	}
	
	
	public static int[] generateQuadElements(int start, int quadCount)
	{
		int[] elements = new int[quadCount*6];
			
		int quadIndex = 0;
		
			for(int i = 0; i < (quadCount*6 - 5); i += 6)
			{
				elements[i]   = start+quadIndex;
				elements[i+1] = start+quadIndex+1;
				elements[i+2] = start+quadIndex+2;
				
				elements[i+3] = start+quadIndex;
				elements[i+4] = start+quadIndex+2;
				elements[i+5] = start+quadIndex+3;
				
				quadIndex += 4;
			}
			
			return elements; 
	}
}