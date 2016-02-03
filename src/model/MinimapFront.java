package model;

import org.lwjgl.opengl.GL11;

public class MinimapFront extends Model
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
		//frontfaces
		1,2,5,
		5,2,6,
		2,3,7,
		2,7,6,
		4,5,7,
		7,5,6,
	};
	
	public MinimapFront()
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
