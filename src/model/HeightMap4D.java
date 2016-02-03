package model;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL32;

import main.Project;

public class HeightMap4D extends Model
{
	
	static int[] tetsPerCube={
			1,0,2,5,//swapped
			0,3,2,7,//not
			5,0,7,4,//0,5
			2,5,7,6,//2,5
			0,2,5,7,//0,2
			
			0,1,2,3,//0,1
			1,0,5,4,//0,1
			1,2,6,5,//1,2
	};
	
	public int width;
	float[][][] heights;
	
	float[] verticies;
	float[] norms;
	int[] tets;
	public HeightMap4D(String file) throws IOException
	{
		color=new ColorMaker(.05f,.1f,
							 .3f, 0f,
							 .1f,.2f
							);
		primType=GL32.GL_LINES_ADJACENCY;
		
		
		BufferedImage image=ImageIO.read(new File(file));
		WritableRaster raster=image.getRaster();
		width=image.getWidth();
		verticies=new float[width*width*width*4];
		norms=new float[verticies.length];
		heights=new float[width][width][width];
		
		for(int x=0; x<width;x++)
		{
			for(int z=0; z<width;z++)
			{
				for(int w=0; w<width;w++)
				{
					int vertId=w*width*width+z*width+x;
					verticies[vertId*4+0]=x*2+(float)Math.random()*.01f;
					verticies[vertId*4+1]=raster.getSampleFloat(x, z+w*width, 0)/32f+(float)Math.random()*.01f;
					verticies[vertId*4+2]=z*2+(float)Math.random()*.01f;
					verticies[vertId*4+3]=w*2+(float)Math.random()*.01f;
					heights[x][z][w]=verticies[vertId*4+1];
				}
			}
		}
		
		int[] cubes=new int[(width-1)*(width-1)*(width-1)*8];
		int cube=0;
		
		for(int x=0; x<width-1;x++)
		{
			for(int z=0; z<width-1;z++)
			{
				for(int w=0; w<width-1;w++)
				{
					cubes[cube*8+0]=w*width*width+z*width+x;
					cubes[cube*8+1]=w*width*width+z*width+(x+1);
					cubes[cube*8+2]=w*width*width+(z+1)*width+(x+1);
					cubes[cube*8+3]=w*width*width+(z+1)*width+x;
					cubes[cube*8+4]=(w+1)*width*width+z*width+x;
					cubes[cube*8+5]=(w+1)*width*width+z*width+(x+1);
					cubes[cube*8+6]=(w+1)*width*width+(z+1)*width+(x+1);
					cubes[cube*8+7]=(w+1)*width*width+(z+1)*width+x;
					cube++;
				}
			}
		}
		
		
		tets=new int[(width-1)*(width-1)*(width-1)*tetsPerCube.length];
		
		for(int i=0;i<(width-1)*(width-1)*(width-1);i++)
		{
			fillTets(cubes,i,tets);
		}
		
		
		
		//FILL VERTEX NORMALS
		NormalTracker[] trackers=new NormalTracker[verticies.length/4];
		for(int i=0;i<trackers.length;i++)
		{
			trackers[i]=new NormalTracker();
		}
		
		for(int i=0;i<tets.length/4;i++)
		{
			float[] cellNormal=getCellNormal(i);			
			for(int j=0;j<4;j++)
			{
				trackers[tets[i*4+j]].add(cellNormal);
			}
		}
		
		for(int i=0;i<verticies.length/4;i++)
		{
			float[] normal=trackers[i].getNormal();
			for(int j=0;j<4;j++)
			{
				norms[i*4+j]=normal[j];
			}
		}
	}
	
	public float[] getCellNormal(int cell)
	{
		float[][] points=new float[4][4];
		
		for(int point=0;point<4;point++)
		{
			for(int i=0;i<4;i++)
			{
				points[point][i]=verticies[tets[cell*4+point]*4+i];
			}
		}
		
		float[][] edges=new float[3][4];
		
		for(int edge=0;edge<3;edge++)
		{
			for(int i=0;i<4;i++)
			{
				edges[edge][i]=points[edge][i]-points[3][i];
			}
		}
		
		return Project.cross4(edges[0],edges[1],edges[2]);
	}
	
	public static void fillTets(int[] cubes, int cube, int[] out)
	{
		int startSrc=cube*8;
		int startDst=cube*tetsPerCube.length;
		for(int i=0;i<tetsPerCube.length;i++)
		{
			out[i+startDst]=cubes[tetsPerCube[i]+startSrc];
		}
	}
	
	public float heightAt(float x, float z, float w)
	{
		x/=2;
		z/=2;
		w/=2;
		if(x<0 || z<0 || w<0 || x>=width-1 || z>=width-1|| w>=width-1)
		{
			return 0;
		}
		int x0=(int) Math.floor(x);
		int z0=(int) Math.floor(z);
		int w0=(int) Math.floor(w);
		int x1=(int) Math.ceil(x);
		int z1=(int) Math.ceil(z);
		int w1=(int) Math.ceil(w);
		
		float xd=x-x0;
		float zd=z-z0;
		float wd=w-w0;
		
		float c00=heights[x0][z0][w0]*(1-xd)+heights[x1][z0][w0]*xd;
		float c10=heights[x0][z1][w0]*(1-xd)+heights[x1][z1][w0]*xd;
		float c01=heights[x0][z0][w1]*(1-xd)+heights[x1][z0][w1]*xd;
		float c11=heights[x0][z1][w1]*(1-xd)+heights[x1][z1][w1]*xd;
		
		float c0=c00*(1-zd)+c10*zd;
		float c1=c01*(1-zd)+c11*zd;
		
		return c0*(1-wd)+c1*wd;
		
	}
	
	public float[] getVerts()
	{
		return verticies;
	}
	public int[] getIds()
	{
		return tets;
	}
	public int getVertSize()
	{
		return 4;
	}
	public int getPrimSize()
	{
		return 4;
	}
	public float[] getNorms()
	{
		return norms;
	}

	public Model3D getBase()
	{
		return null;
	}
}
