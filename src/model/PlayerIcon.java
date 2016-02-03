package model;

import org.lwjgl.opengl.GL11;

public class PlayerIcon extends Model3D
{
	static float[] sVerts=
			{
				0,0,.15f,
			   -.1f,0,-.15f,
				.1f,0,-.15f
			};
	static int sTris[]=
			{
				0,1,2,
				2,1,0
			};
	public PlayerIcon()
	{
		color=new ColorMaker(1,0,1,0,1,0);
		primType=GL11.GL_TRIANGLES;
		verts=sVerts;
		tris=sTris;
	}
	public float[] getVerts()
	{
		return verts;
	}

	public float[] getNorms()
	{
		return new float[verts.length/3*4];
	}

	public int[] getIds()
	{
		return tris;
	}

	public int getVertSize()
	{
		return 3;
	}

	public int getPrimSize()
	{
		return 3;
	}

	public Model3D getBase()
	{
		return this;
	}

}
