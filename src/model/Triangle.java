package model;

import org.lwjgl.util.vector.Vector4f;

import main.Project;

public class Triangle
{
	int[] vertIds=new int[3];
	float[] verts;
	
	public Triangle(float[] verts,int v0, int v1, int v2)
	{
		this.vertIds[0]=v0;
		this.vertIds[1]=v1;
		this.vertIds[2]=v2;
		this.verts=verts;
	}
	
	public float[] getVert(int vert)
	{
		return new float[]{
				verts[vertIds[vert]*3+0],
				verts[vertIds[vert]*3+1],
				verts[vertIds[vert]*3+2]
				};
	}
	public Vector4f getVec(int vert)
	{
		return new Vector4f(
				verts[vertIds[vert]*3+0],
				verts[vertIds[vert]*3+1],
				verts[vertIds[vert]*3+2],
				1
				);
	}
	
	public Vector4f getNormal()
	{
		float[] u={		getVert(1)[0]-getVert(0)[0],
						getVert(1)[1]-getVert(0)[1],
						getVert(1)[2]-getVert(0)[2]};
		
		float[] v={		getVert(2)[0]-getVert(0)[0],
						getVert(2)[1]-getVert(0)[1],
						getVert(2)[2]-getVert(0)[2]};
		
		float[] result=new float[3];
		Project.cross(u, v, result);
		return (Vector4f) new Vector4f(result[0],result[1],result[2],0).normalise();
	}
		
}
