package model;

import org.lwjgl.opengl.GL11;

public class MinimapBack extends Model
{
	static float[] verts={
		-1/2f,-1/2f, -1/2f,//0
		 1/2f,-1/2f, -1/2f,//1
		 1/2f, 1/2f, -1/2f,//2
		-1/2f, 1/2f, -1/2f,//3
		-1/2f,-1/2f,  1/2f,//4
		 1/2f,-1/2f,  1/2f,//5
		 1/2f, 1/2f,  1/2f,//6
		-1/2f, 1/2f,  1/2f,//7
	};
	
	static int[] triangles=
	{
		//backfaces
		0,3,4,
		4,3,7,
		0,1,2,
		0,2,3,
		1,0,4,
		1,4,5,
	};
	
	public MinimapBack()
	{
		color=new ColorMaker(0.5f,0,0,0,0,0);
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
		return 3;
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
