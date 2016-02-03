package collision;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class PlaneBox extends CollisionMesh
{
	Vector3f s,i,j;
	Vector3f n;//noraml
	
	Matrix4f localToWorld;
	Matrix4f worldToLocal;
	
	float dist;
	
	static FloatBuffer matBuffer=BufferUtils.createFloatBuffer(9);
	static FloatBuffer matBuffer4=BufferUtils.createFloatBuffer(16);
	
	public PlaneBox(Vector3f s, Vector3f i, Vector3f j)
	{
		this.s=s;
		this.i=i;
		this.j=j;
		n=(Vector3f) Vector3f.cross(i, j, null).normalise().negate();
		float[] localToWorldArray={
				i.x , i.y , i.z , 0,
				j.x , j.y , j.z , 0,
				n.x , n.y , n.z , 0,
				s.x , s.y , s.z , 1
		};
		
		localToWorld=loadMat4(localToWorldArray);
		worldToLocal=Matrix4f.invert(localToWorld, null);
	}
	public boolean intersects(Line3f line)
	{
		Vector3f k=line.ab();
		Vector3f u=line.a;
		Vector3f ans=Vector3f.sub(u, s, null);
		
		float[] matArray={ i.x, i.y, i.z,
						   j.x, j.y, j.z,
						  -k.x,-k.y,-k.z};
		Matrix3f mat=loadMat(matArray);
		
		matArray=new float[]
				 { ans.x, ans.y, ans.z,
				   j.x, j.y, j.z,
				  -k.x,-k.y,-k.z};
		Matrix3f aMat=loadMat(matArray);
		
		matArray=new float[]
				 { i.x, i.y, i.z,
				   ans.x, ans.y, ans.z,
				  -k.x,-k.y,-k.z};
		Matrix3f bMat=loadMat(matArray);
		
		matArray=new float[]
				 { i.x, i.y, i.z,
				   j.x, j.y, j.z,
				  ans.x,ans.y,ans.z};
		Matrix3f cMat=loadMat(matArray);
		
		float d=mat.determinant();
		float a=aMat.determinant()/d;
		float b=bMat.determinant()/d;
		float c=cMat.determinant()/d;

		
		if(0<=a && a<=1 && 0<=b && b<=1 && 0<=c && c<=1)
		{
			dist=c;
			
			Vector4f endPos=new Vector4f(line.b.x, line.b.y, line.b.z, 1);
			Vector4f localEnd=Matrix4f.transform(worldToLocal, endPos, null);
			localEnd.z=.01f;
			Vector4f worldEnd=Matrix4f.transform(localToWorld, localEnd, null);
			correctPos=new Vector3f(worldEnd.x,worldEnd.y,worldEnd.z);

			return true;
		}
		return false;
	}
	
	public static Matrix4f loadMat4(float[] toLoad)
	{
		matBuffer4.clear();
		matBuffer4.put(toLoad);
		matBuffer4.flip();
		Matrix4f toReturn=new Matrix4f();
		toReturn.load(matBuffer4);
		return toReturn;
	}
	
	public static Matrix3f loadMat(float[] toLoad)
	{
		matBuffer.clear();
		matBuffer.put(toLoad);
		matBuffer.flip();
		Matrix3f toReturn=new Matrix3f();
		toReturn.load(matBuffer);
		return toReturn;
	}

}
