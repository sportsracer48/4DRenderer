package model;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public abstract class Model
{
	public ColorMaker color;
	
	public int vao;
	
	public int vb;
	public int nb;
	public int cb;
	public int ib;
	
	public int primType;
	
	private boolean hasBuffers=false;
	private boolean hasVao=false;
	
	public abstract float[] getVerts();
	public abstract float[] getNorms();
	public abstract int[] getIds();
	public abstract int getVertSize();
	public abstract int getPrimSize();
	public abstract Model3D getBase();
	
	public float[] getColors()
	{
		float[] colors=new float[getVerts().length/getVertSize()*4];
		
		for(int i=0;i<colors.length/4;i++)
		{
			colors[i*4+0]=color.getRed();
			colors[i*4+1]=color.getGreen();
			colors[i*4+2]=color.getBlue();
			colors[i*4+3]=color.getAlpha();
		}
		
		return colors;
	}
	
	public void genBuffers()
	{
		if(hasBuffers) return;
		
		vb=GL15.glGenBuffers();
		bufferData(vb,getVerts());
		
		nb=GL15.glGenBuffers();
		bufferData(nb,getNorms());
		
		cb=GL15.glGenBuffers();
		bufferData(cb,getColors());
		
		ib=GL15.glGenBuffers();
		indexBufferData(ib,getIds());
		
		hasBuffers=true;
	}
	
	public void genVao()
	{
		if(hasVao) return;
		genBuffers();
		vao=GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);
		addToCurrentVao();
		hasVao=true;
	}
	
	public void addToCurrentVao()
	{
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vb);
		GL20.glVertexAttribPointer(0, getVertSize(), GL11.GL_FLOAT, false, 0, 0);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, cb);
		GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, 0, 0);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, nb);
		GL20.glVertexAttribPointer(2, 4, GL11.GL_FLOAT, false, 0, 0);
		
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
	}
	
	public void render()
	{
		genVao();
		GL30.glBindVertexArray(vao);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER,ib);
		GL11.glDrawElements(primType, getIds().length, GL11.GL_UNSIGNED_INT, 0);
	}
	
	public static void bufferData(int id, float[] dataArray)
	{
		FloatBuffer data=BufferUtils.createFloatBuffer(dataArray.length);
		data.put(dataArray);
		data.flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,id);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
	}

	public static void indexBufferData(int id, int[] dataArray)
	{
		IntBuffer data=BufferUtils.createIntBuffer(dataArray.length);
		data.put(dataArray);
		data.flip();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER,id);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
	}
}
