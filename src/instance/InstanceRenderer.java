package instance;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL33;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import main.Entity;
import model.Model;

public class InstanceRenderer
{
	Model m;
	int numInstances;
	
	int mb;
	int pb;
	int vao;
	
	ArrayList<Matrix4f> modelMatricies=new ArrayList<Matrix4f>();
	ArrayList<Vector4f> modelPositions=new ArrayList<Vector4f>();
	
	public InstanceRenderer(Model m)
	{
		this.m=m;
	}
	
	public void addInstance(Matrix4f model, Vector4f modelPos)
	{
		modelMatricies.add(model);
		modelPositions.add(modelPos);
		numInstances++;
	}
	
	public void addInstance(Entity e, int part)
	{
		addInstance(e.getModelMatrix(part),e.getPostition(part));
	}
	
	public void addBaseInstance(Entity e)
	{
		addInstance(e.getBaseMatrix(),e.getBasePosition());
	}
	
	public void finish()
	{
		vao=GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);
		
		m.genBuffers();
		
		mb=GL15.glGenBuffers();
		pb=GL15.glGenBuffers();
		
		FloatBuffer mData=BufferUtils.createFloatBuffer(16*numInstances);

		for(int i=0;i<numInstances;i++)
		{
			modelMatricies.get(i).store(mData);
		}
		mData.flip();
		
		bufferData(mb,mData);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, m.vb);
		GL20.glVertexAttribPointer(0, m.getVertSize(), GL11.GL_FLOAT, false, 0, 0);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, m.cb);
		GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, 0, 0);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, m.nb);
		GL20.glVertexAttribPointer(2, 4, GL11.GL_FLOAT, false, 0, 0);
		
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, mb);
		GL20.glVertexAttribPointer(3, 4, GL11.GL_FLOAT, false, 64, 0);
		GL20.glEnableVertexAttribArray(3);
		GL33.glVertexAttribDivisor(3, 1);
		
		GL20.glVertexAttribPointer(4, 4, GL11.GL_FLOAT, false, 64, 16);
		GL20.glEnableVertexAttribArray(4);
		GL33.glVertexAttribDivisor(4, 1);
		
		GL20.glVertexAttribPointer(5, 4, GL11.GL_FLOAT, false, 64, 32);
		GL20.glEnableVertexAttribArray(5);
		GL33.glVertexAttribDivisor(5, 1);
		
		GL20.glVertexAttribPointer(6, 4, GL11.GL_FLOAT, false, 64, 48);
		GL20.glEnableVertexAttribArray(6);
		GL33.glVertexAttribDivisor(6, 1);

		
		FloatBuffer pData=BufferUtils.createFloatBuffer(4*numInstances);
		for(int i=0;i<numInstances;i++)
		{
			modelPositions.get(i).store(pData);
		}
		pData.flip();
		bufferData(pb,pData);
		
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, pb);
		GL20.glVertexAttribPointer(7, 4, GL11.GL_FLOAT, false, 0, 0);
		GL20.glEnableVertexAttribArray(7);
		GL33.glVertexAttribDivisor(7, 1);
	}
	
	public void render()
	{
		GL30.glBindVertexArray(vao);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER,m.ib);
		GL31.glDrawElementsInstanced(m.primType, m.getIds().length, GL11.GL_UNSIGNED_INT, 0, numInstances);
	}
	
	public static void bufferData(int id, FloatBuffer data)
	{
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,id);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
	}
}
