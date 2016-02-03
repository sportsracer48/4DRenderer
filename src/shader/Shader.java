package shader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.lwjgl.opengl.GL20;

public class Shader
{
	int id;
	public Shader(String file, int type)
	{
		id=loadShader(file,type);
	}
	
	public static int loadShader(String file, int type)
	{
		String source=null;
		try
		{
			source = readFileAsString(file);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		int shader=GL20.glCreateShader(type);
		GL20.glShaderSource(shader, source);
		GL20.glCompileShader(shader);

		System.out.println(GL20.glGetShaderInfoLog(shader, 1024));
		return shader;
	}
	
	private static String readFileAsString(String filename) throws Exception 
	{
		StringBuilder source = new StringBuilder();

		FileInputStream in = new FileInputStream(filename);

		Exception exception = null;

		BufferedReader reader;
		try{
			reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));

			Exception innerExc= null;
			try {
				String line;
				while((line = reader.readLine()) != null)
					source.append(line).append('\n');
			}
			catch(Exception exc) {
				exception = exc;
			}
			finally {
				try {
					reader.close();
				}
				catch(Exception exc) {
					if(innerExc == null)
						innerExc = exc;
					else
						exc.printStackTrace();
				}
			}

			if(innerExc != null)
				throw innerExc;
		}
		catch(Exception exc) {
			exception = exc;
		}
		finally {
			try {
				in.close();
			}
			catch(Exception exc) {
				if(exception == null)
					exception = exc;
				else
					exc.printStackTrace();
			}

			if(exception != null)
				throw exception;
		}

		return source.toString();
	}
}
