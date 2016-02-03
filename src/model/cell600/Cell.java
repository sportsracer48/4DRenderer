package model.cell600;

import java.util.Arrays;

import main.Project;

import org.lwjgl.util.vector.Vector4f;

public class Cell
{
	int a,b,c,d;
	Vertex v1,v2,v3,v4;
	public Cell(Vertex a, Vertex b, Vertex c, Vertex d)
	{
		Vertex[] verts={a,b,c,d};
		Arrays.sort(verts);
		
		v1=verts[0];
		v2=verts[1];
		v3=verts[2];
		v4=verts[3];
		this.a=verts[0].id;
		this.b=verts[1].id;
		this.c=verts[2].id;
		this.d=verts[3].id;
	}
	
	public boolean equals(Object o)
	{
		if(o instanceof Cell)
		{
			Cell c= (Cell)o;
			return a==c.a && b==c.b && this.c==c.c && d==c.d;
		}
		return false;
	}
	
	public int hashCode()
	{
		return a*120*120*120+b*120*120+c*120+d;
	}
	
	public Vector4f normal()
	{
		return Project.cross4(Vector4f.sub(v1.asVector(), v4.asVector(), null), 
				Vector4f.sub(v2.asVector(), v4.asVector(), null), 
				Vector4f.sub(v3.asVector(), v4.asVector(), null));
	}
	
	public String toString()
	{
		if(Vector4f.dot(normal(), v1.asVector())>0)
		{
			return "f "+b+" "+a+" "+c+" "+d;
		}
		else
		{
			return "f "+a+" "+b+" "+c+" "+d;
		}
	}
}
