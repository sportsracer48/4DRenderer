package model;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;


public class Model3D extends Model
{
	public float[] verts;
	public int[] tris;
	
	public Model3D()
	{
		
	}
	
	public Model3D(String obj) throws IOException
	{
		color=new ColorMaker(1,0,1,0,1,0);
		color.setAlpha(0.2f);
		primType=GL11.GL_TRIANGLES;
		
		
		File file=new File(obj);
		BufferedReader in=new BufferedReader(new FileReader(file));
		ArrayList<Float> vertList=new ArrayList<Float>();
		ArrayList<Integer> faceList=new ArrayList<Integer>();
		for(String line=in.readLine();line!=null;line=in.readLine())
		{
			line=line.trim();
			if(line.isEmpty()|| line.startsWith("#"))
			{
				continue;
			}
			String[] tokens=line.split(" ");
			if(line.startsWith("v "))
			{
				vertList.add(Float.parseFloat(tokens[1]));
				vertList.add(Float.parseFloat(tokens[2]));
				vertList.add(Float.parseFloat(tokens[3]));
			}
			if(line.startsWith("f "))
			{
				if(tokens.length!=4)
				System.out.println(tokens.length);
				for(int i=1;i<=3;i++)
				{
					faceList.add(Integer.parseInt(tokens[i])-1);
				}
			}
		}
		in.close();
		verts=new float[vertList.size()];
		tris=new int[faceList.size()];
		for(int i=0;i<verts.length;i++)
		{
			verts[i]=vertList.get(i);
		}
		for(int i=0;i<tris.length;i++)
		{
			tris[i]=faceList.get(i);
		}
	}
	
	public int numTriangles()
	{
		return tris.length/3;
	}
	public int numVerts()
	{
		return verts.length/3;
	}
	
	public Triangle getTriangle(int id)
	{
		return new Triangle(verts,tris[id*3+0],tris[id*3+1],tris[id*3+2]);
	}

	public float[] getVerts()
	{
		return verts;
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
	
	public float[] getNorms()
	{
		return new float[verts.length/3*4];
	}

	public Model3D getBase()
	{
		return null;
	}
}
