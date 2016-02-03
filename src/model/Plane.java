package model;

import org.lwjgl.opengl.GL11;

public class Plane extends Model
{
	static float[] verts={
		-1.4f,0,-1.4f,//0
		 1.4f,0,-1.4f,//1
		 1.4f,0, 1.4f,//2
		-1.4f,0, 1.4f,//3
	};
	
	static int[] triangles={
		0,1,2,
		0,2,3,
		1,0,2,
		2,0,3
	};
	
	public Plane()
	{
		color=new ColorMaker(0,0,0,0,1,0);
		color.setAlpha(0.4f);
		primType=GL11.GL_TRIANGLES;
	}
	
	public float[] getVerts()
	{
		return verts;
	}

	public int[] getIds()
	{
		return triangles;
	}

	public int getVertSize()
	{
		return 3;
	}

	public int getPrimSize()
	{
		return 2;
	}
	
	public float[] getNorms()
	{
		return new float[verts.length/3*4];
	}

	public Model3D getBase()
	{
		return null;
	}
}
