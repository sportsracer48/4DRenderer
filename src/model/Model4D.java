package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.opengl.GL32;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import main.Project;

public class Model4D extends Model
{
	public float[] verts;
	public int[] tets;
	public float[] norms;
	
	Model3D base;
	
	static int[] tetsPerPrism={
		0,3,5,2,
		0,1,2,4,
		0,4,2,3
	};
	
	public Model4D(Model3D base, float x, float y, float z, float w)//extrude to point
	{
		primType=GL32.GL_LINES_ADJACENCY;
		verts=new float[base.verts.length*4/3+8];
		tets=new int[base.tris.length*8/3];
		norms=new float[verts.length];
		this.base=base;
		
		for(int i=0;i<base.verts.length/3;i++)
		{
			verts[i*4+0]=base.verts[i*3+0]+(float)Math.random()*.001f;
			verts[i*4+1]=0f+(float)Math.random()*.001f;
			verts[i*4+2]=base.verts[i*3+1]+(float)Math.random()*.001f;
			verts[i*4+3]=base.verts[i*3+2]+(float)Math.random()*.001f;
		}
		verts[verts.length-4]=x+(float)Math.random()*.001f;
		verts[verts.length-3]=y+(float)Math.random()*.001f;
		verts[verts.length-2]=z+(float)Math.random()*.001f;
		verts[verts.length-1]=w+(float)Math.random()*.001f;
		verts[verts.length-8]=0+(float)Math.random()*.001f;
		verts[verts.length-7]=0+(float)Math.random()*.001f;
		verts[verts.length-6]=0+(float)Math.random()*.001f;
		verts[verts.length-5]=0+(float)Math.random()*.001f;
		
		for(int i=0;i<base.tris.length/3;i++)
		{
			tets[i*8+0]=base.tris[i*3+0];
			tets[i*8+1]=base.tris[i*3+1];
			tets[i*8+2]=base.tris[i*3+2];
			tets[i*8+3]=verts.length/4-1;
			tets[i*8+5]=base.tris[i*3+0];//Purposefully swapped
			tets[i*8+4]=base.tris[i*3+1];
			tets[i*8+6]=base.tris[i*3+2];
			tets[i*8+7]=verts.length/4-2;
		}
		
		NormalTracker[] trackers=new NormalTracker[verts.length/4];
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
		
		for(int i=0;i<verts.length/4;i++)
		{
			float[] normal=trackers[i].getNormal();
			for(int j=0;j<4;j++)
			{
				norms[i*4+j]=normal[j];
			}
		}
	}
	
	public Model4D(String file) throws IOException
	{
		ArrayList<Float> vertList=new ArrayList<Float>();
		ArrayList<Integer> cellList=new ArrayList<Integer>();
		
		File f=new File(file);
		BufferedReader in=new BufferedReader(new FileReader(f));
		String line;
		for(line=in.readLine();line!=null;line=in.readLine())
		{
			if(line.startsWith("v"))
			{
				String[] tokens=line.split(" ");
				for(int i=1;i<5;i++)
				{
					vertList.add(Float.parseFloat(tokens[i]));
				}
			}
			if(line.startsWith("f"))
			{
				String[] tokens=line.split(" ");
				for(int i=1;i<5;i++)
				{
					cellList.add(Integer.parseInt(tokens[i]));
				}
			}
		}
		in.close();
		
		primType=GL32.GL_LINES_ADJACENCY;
		verts=new float[vertList.size()];
		tets=new int[cellList.size()];
		norms=new float[vertList.size()];
		
		for(int i=0;i<vertList.size();i++)
		{
			verts[i]=vertList.get(i)+(float)Math.random()*.001f;
		}
		
		for(int i=0;i<cellList.size();i++)
		{
			tets[i]=cellList.get(i);
		}
		
		NormalTracker[] trackers=new NormalTracker[verts.length/4];
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
		
		for(int i=0;i<verts.length/4;i++)
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
				points[point][i]=verts[tets[cell*4+point]*4+i];
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
	
	public Model4D(Model3D base) //rotate
	{
		int iterations=32;
		primType=GL32.GL_LINES_ADJACENCY;
		verts=new float[base.numVerts()*4*iterations];
		norms=new float[verts.length];
		int[] prisms=new int[base.numTriangles()*6*iterations];
		tets=new int[prisms.length*2];
		this.base=base;
		
		int numVerts=base.numVerts();
		int numTris=base.numTriangles();
		
		for(int i=0;i<iterations;i++)
		{
			float theta=(float) (2*Math.PI*i/iterations);
			for(int v=0;v<numVerts;v++)
			{
				Vector4f vec=new Vector4f(  base.verts[v*3+0],
											base.verts[v*3+1],
											base.verts[v*3+2],
											0);
				Matrix4f rot=Project.rotXU(theta);
				Matrix4f.transform(rot, vec, vec);
				verts[i*numVerts*4+v*4+0]=vec.x+(float)Math.random()*.001f;
				verts[i*numVerts*4+v*4+1]=vec.y+(float)Math.random()*.001f;
				verts[i*numVerts*4+v*4+2]=vec.z+(float)Math.random()*.001f;
				verts[i*numVerts*4+v*4+3]=vec.w+(float)Math.random()*.001f;
			}
		}
		
		for(int i=0;i<iterations-1;i++)
		{
			for(int triId=0;triId<numTris;triId++)
			{
				for(int j=0;j<3;j++)
				{
					if(Math.abs(base.getTriangle(triId).getVert(j)[0])>.001)
					{
						prisms[i*numTris*6+triId*6+j]=base.getTriangle(triId).vertIds[j]+i*numVerts;
					}
					else
					{
						prisms[i*numTris*6+triId*6+j]=base.getTriangle(triId).vertIds[j];
					}
				}
				for(int j=0;j<3;j++)
				{
					if(Math.abs(base.getTriangle(triId).getVert(j)[0])>.001)
					{
						prisms[i*numTris*6+triId*6+3+j]=base.getTriangle(triId).vertIds[j]+(i+1)*numVerts;
					}
					else
					{
						prisms[i*numTris*6+triId*6+3+j]=base.getTriangle(triId).vertIds[j];
					}
				}
			}
		}
		
		for(int triId=0;triId<numTris;triId++)
		{
			int i=iterations-1;
			for(int j=0;j<3;j++)
			{
				if(Math.abs(base.getTriangle(triId).getVert(j)[0])>.001)
				{
					prisms[i*numTris*6+triId*6+j]=base.getTriangle(triId).vertIds[j]+i*numVerts;
				}
				else
				{
					prisms[i*numTris*6+triId*6+j]=base.getTriangle(triId).vertIds[j];
				}
			}
			prisms[i*numTris*6+triId*6+3]=base.getTriangle(triId).vertIds[0];
			prisms[i*numTris*6+triId*6+4]=base.getTriangle(triId).vertIds[1];
			prisms[i*numTris*6+triId*6+5]=base.getTriangle(triId).vertIds[2];
		}
		
		for(int prismId=0;prismId<prisms.length/6;prismId++)
		{
			tets[prismId*12+0]=prisms[prismId*6+3];
			tets[prismId*12+1]=prisms[prismId*6+5];
			tets[prismId*12+2]=prisms[prismId*6+2];
			tets[prismId*12+3]=prisms[prismId*6+4];
			                                    
			tets[prismId*12+4]=prisms[prismId*6+0];
			tets[prismId*12+5]=prisms[prismId*6+1];
			tets[prismId*12+6]=prisms[prismId*6+4];
			tets[prismId*12+7]=prisms[prismId*6+2];
			                                
			tets[prismId*12+8]=prisms[prismId*6+0];
			tets[prismId*12+9]=prisms[prismId*6+4];
			tets[prismId*12+10]=prisms[prismId*6+3];
			tets[prismId*12+11]=prisms[prismId*6+2];
		}
		
		NormalTracker[] trackers=new NormalTracker[verts.length/4];
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
		
		for(int i=0;i<verts.length/4;i++)
		{
			float[] normal=trackers[i].getNormal();
			for(int j=0;j<4;j++)
			{
				norms[i*4+j]=normal[j];
			}
		}
	}
	
	public Model4D(Model3D base, float height,boolean top, boolean bottom)//extrude
	{
		primType=GL32.GL_LINES_ADJACENCY;
		verts=new float[base.numVerts()*4*2+8];
		norms=new float[verts.length];
		int[] prisms=new int[base.numTriangles()*6];
		
		int numBases=0;
		if(top) numBases++;
		if(bottom) numBases++;
		
		tets=new int[prisms.length*2+base.numTriangles()*4*numBases];
		this.base=base;
		
		for(int vertId=0;vertId<base.numVerts();vertId++)
		{
			verts[vertId*8+0]=base.verts[vertId*3+0]+(float)Math.random()*.001f;
			verts[vertId*8+1]=(float)Math.random()*.001f;
			verts[vertId*8+2]=base.verts[vertId*3+1]+(float)Math.random()*.001f;
			verts[vertId*8+3]=base.verts[vertId*3+2]+(float)Math.random()*.001f;
			
			verts[vertId*8+4]=verts[vertId*8+0];
			verts[vertId*8+5]=height+verts[vertId*8+1];
			verts[vertId*8+6]=verts[vertId*8+2];
			verts[vertId*8+7]=verts[vertId*8+3];
		}
		
		verts[verts.length-4]=0+(float)Math.random()*.001f;//bottom, n-1
		verts[verts.length-3]=0+(float)Math.random()*.001f;
		verts[verts.length-2]=0+(float)Math.random()*.001f;
		verts[verts.length-1]=0+(float)Math.random()*.001f;
		
		verts[verts.length-8]=0+(float)Math.random()*.001f;//top, n-2
		verts[verts.length-7]=height+(float)Math.random()*.001f;
		verts[verts.length-6]=0+(float)Math.random()*.001f;
		verts[verts.length-5]=0+(float)Math.random()*.001f;
		
		for(int triId=0;triId<base.numTriangles();triId++)
		{
			prisms[triId*6+0]=base.getTriangle(triId).vertIds[0]*2;
			prisms[triId*6+1]=base.getTriangle(triId).vertIds[1]*2;
			prisms[triId*6+2]=base.getTriangle(triId).vertIds[2]*2;
			prisms[triId*6+3]=base.getTriangle(triId).vertIds[0]*2+1;
			prisms[triId*6+4]=base.getTriangle(triId).vertIds[1]*2+1;
			prisms[triId*6+5]=base.getTriangle(triId).vertIds[2]*2+1;
		}
		
		for(int prismId=0;prismId<prisms.length/6;prismId++)
		{
			tets[prismId*12+0]=prisms[prismId*6+3];
			tets[prismId*12+1]=prisms[prismId*6+5];
			tets[prismId*12+2]=prisms[prismId*6+4];
			tets[prismId*12+3]=prisms[prismId*6+2];
			                                    
			tets[prismId*12+4]=prisms[prismId*6+0];
			tets[prismId*12+5]=prisms[prismId*6+1];
			tets[prismId*12+6]=prisms[prismId*6+2];
			tets[prismId*12+7]=prisms[prismId*6+4];
			                                
			tets[prismId*12+8]=prisms[prismId*6+0];
			tets[prismId*12+9]=prisms[prismId*6+4];
			tets[prismId*12+10]=prisms[prismId*6+2];
			tets[prismId*12+11]=prisms[prismId*6+3];
		}
		
		for(int i=0;i<base.numTriangles();i++)
		{
			if(top && bottom)
			{
				tets[prisms.length*2+i*8+0]=base.tris[i*3+1]*2;
				tets[prisms.length*2+i*8+1]=base.tris[i*3+0]*2;
				tets[prisms.length*2+i*8+2]=base.tris[i*3+2]*2;
				tets[prisms.length*2+i*8+3]=verts.length/4-1;
				
				tets[prisms.length*2+i*8+4]=base.tris[i*3+0]*2+1;
				tets[prisms.length*2+i*8+5]=base.tris[i*3+1]*2+1;
				tets[prisms.length*2+i*8+6]=base.tris[i*3+2]*2+1;
				tets[prisms.length*2+i*8+7]=verts.length/4-2;
			}
			else if(top)
			{
				tets[prisms.length*2+i*4+0]=base.tris[i*3+0]*2+1;
				tets[prisms.length*2+i*4+1]=base.tris[i*3+1]*2+1;
				tets[prisms.length*2+i*4+2]=base.tris[i*3+2]*2+1;
				tets[prisms.length*2+i*4+3]=verts.length/4-2;
			}
			else if(bottom)
			{
				tets[prisms.length*2+i*4+0]=base.tris[i*3+1]*2;
				tets[prisms.length*2+i*4+1]=base.tris[i*3+0]*2;
				tets[prisms.length*2+i*4+2]=base.tris[i*3+2]*2;
				tets[prisms.length*2+i*4+3]=verts.length/4-1;
			}
		}
		
		NormalTracker[] trackers=new NormalTracker[verts.length/4];
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
		
		for(int i=0;i<verts.length/4;i++)
		{
			float[] normal=trackers[i].getNormal();
			for(int j=0;j<4;j++)
			{
				norms[i*4+j]=normal[j];
			}
		}
	}

	public float[] getVerts()
	{
		return verts;
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
		return base;
	}
}
