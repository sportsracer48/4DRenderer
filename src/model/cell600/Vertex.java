package model.cell600;

import java.util.ArrayList;
import java.util.HashSet;

import org.lwjgl.util.vector.Vector4f;

public class Vertex implements Comparable<Vertex>
{
	int id;
	float x, y, z, w;
	
	ArrayList<Vertex> nearby=new ArrayList<Vertex>();
	
	
	public Vertex(String line, int id)
	{
		String[] tokens=line.split(" ");
		this.id=id;
		x=Float.parseFloat(tokens[1]);
		y=Float.parseFloat(tokens[2]);
		z=Float.parseFloat(tokens[3]);
		w=Float.parseFloat(tokens[4]);
	}
	
	public double dist(Vertex v)
	{
		return Math.sqrt(
				(x-v.x)*(x-v.x)+
				(y-v.y)*(y-v.y)+
				(z-v.z)*(z-v.z)+
				(w-v.w)*(w-v.w)
				);
	}
	
	public void addAllNearby(ArrayList<Vertex> verts, float len)
	{
		for(Vertex v2:verts)
		{
			if(v2==this) continue;
			
			if(Math.abs(v2.dist(this)-len)<.001)
			{
				nearby.add(v2);
			}
		}
	}
	
	public Vector4f asVector()
	{
		return new Vector4f(x,y,z,w);
	}
	
	public boolean isNearby(Vertex v)
	{
		return this!=v && nearby.contains(v);
	}
	
	public HashSet<Cell> getAllCells()
	{
		HashSet<Cell> toReturn=new HashSet<Cell>();
		Vertex a=this;
		for(Vertex b:nearby)
		{
			for(Vertex c:nearby)
			{
				for(Vertex d:nearby)
				{
					if(b.isNearby(c) && b.isNearby(d) && c.isNearby(d))
					{
						toReturn.add(new Cell(a,b,c,d));
					}
				}
			}
		}
		return toReturn;
	}

	public int compareTo(Vertex v)
	{
		return id-v.id;
	}
}
