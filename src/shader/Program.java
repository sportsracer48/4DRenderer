package shader;

import org.lwjgl.opengl.GL20;

public class Program
{
	int id;
	public Program()
	{
		id=GL20.glCreateProgram();
	}
	
	public void addShader(Shader shader)
	{
		GL20.glAttachShader(id, shader.id);
	}
	
	public void bindAttribLocation(int attrib, String location)
	{
		GL20.glBindAttribLocation(id, attrib, location);
	}
	
	public void link()
	{
		GL20.glLinkProgram(id);
		System.out.println(GL20.glGetProgramInfoLog(id, 2048));
	}
	
	public int getUniformLocation(String name)
	{
		return GL20.glGetUniformLocation(id, name);
	}
	
	public Uniform getUniform(String name)
	{
		return new Uniform(this,name);
	}
	
	public void bind()
	{
		GL20.glUseProgram(id);
	}
}
