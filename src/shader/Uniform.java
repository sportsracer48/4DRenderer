package shader;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;

public class Uniform
{
	int id;
	static FloatBuffer matBuffer=BufferUtils.createFloatBuffer(16);
	public Uniform(Program program, String name)
	{
		id=program.getUniformLocation(name);
	}
	
	public void setf(float f)
	{
		GL20.glUniform1f(id,f);
	}
	
	public void seti(int i)
	{
		GL20.glUniform1i(id,i);
	}
	
	public void set4f(float x, float y, float z, float w)
	{
		GL20.glUniform4f(id,x,y,z,w);
	}
	
	public void set4M(FloatBuffer mat)
	{
		GL20.glUniformMatrix4(id, false, mat);
	}
	
	public void set4M(Matrix4f m)
	{
		matBuffer.clear();
		m.store(matBuffer);
		matBuffer.flip();
		set4M(matBuffer);
	}
	
	public void set4M()
	{
		set4M(matBuffer);
	}
}
