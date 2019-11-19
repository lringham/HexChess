package RenderingSystem;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class ShaderLoader
{
	public static String shaderLocation = "shaders\\";
	
	
	public static int createShaderProgram()
	{
		//Initialize openGL and load the shaders
		int shaderProgram = GL20.glCreateProgram();
		int vertexShader = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		int fragmentShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		BufferedReader reader = null;
		String line = "";
		
		StringBuilder vertexShaderSource = new StringBuilder();
		StringBuilder fragmentShaderSource = new StringBuilder();
		
		Util.DebugLog.writeError("Loading Shaders");
		try
		{
			reader = new BufferedReader(new FileReader(shaderLocation + "shader.vert"));
			while((line = reader.readLine()) != null)
				vertexShaderSource.append(line).append("\n");
			reader.close();
		}
		catch(IOException e)
		{
			Util.DebugLog.writeError("Vertex Shader" + " not loaded\n"+e.getMessage());
		}
	
		try
		{
			reader = new BufferedReader(new FileReader(shaderLocation + "shader.frag"));
			line = "";
			while((line = reader.readLine()) != null)
				fragmentShaderSource.append(line).append("\n");
			reader.close();
		}
		catch(IOException e)
		{
			Util.DebugLog.writeError("Fragment Shader"+" not loaded\n"+e.getMessage());
		}		
		Util.DebugLog.writeError("Loading Shaders Finished");
		Util.DebugLog.writeError("\nCompiling Shaders");
		
		GL20.glShaderSource(vertexShader,vertexShaderSource);
		GL20.glCompileShader(vertexShader);
		if(GL20.glGetShaderi(vertexShader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
			Util.DebugLog.writeError("ERROR vertexShader "+GL11.glGetError());
		
		GL20.glShaderSource(fragmentShader,fragmentShaderSource);
		GL20.glCompileShader(fragmentShader);		
		if(GL20.glGetShaderi(fragmentShader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
			Util.DebugLog.writeError("ERROR fragmentShader "+GL11.glGetError());	
		
		Util.DebugLog.writeError("Compiling Shaders Finished");
		Util.DebugLog.writeError("\nAttaching Shaders");
		GL20.glAttachShader(shaderProgram, fragmentShader);
		GL20.glAttachShader(shaderProgram, vertexShader);
		GL20.glLinkProgram(shaderProgram);
		GL20.glValidateProgram(shaderProgram);
		Util.DebugLog.writeError("Attaching Shaders Finished");		
		
		return shaderProgram;
	}
}