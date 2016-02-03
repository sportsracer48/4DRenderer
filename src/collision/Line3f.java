package collision;

import org.lwjgl.util.vector.Vector3f;

public class Line3f
{
	Vector3f a,b;
	public Line3f(Vector3f a, Vector3f b)
	{
		this.a=a;
		this.b=b;
	}
	
	public Line3f(float x1, float y1, float z1,
				  float x2, float y2, float z2)
	{
		a=new Vector3f(x1,y1,z1);
		b=new Vector3f(x2,y2,z2);
	}
	
	public Vector3f ab()
	{
		return Vector3f.sub(b, a, null);
	}
	
	public float dist(Vector3f p)
	{
		Vector3f ap=Vector3f.sub(p, a, null);
		Vector3f bp=Vector3f.sub(p, b, null);
		Vector3f ab=ab();
		
		if(Vector3f.dot(ap, ab)<0)
		{
			return ap.length();
		}
		
		if(Vector3f.dot(bp, ab)>0)
		{
			return bp.length();
		}
		return Vector3f.cross(ap, ab, null).length()/ab.length();
	}
	
	public Line3f extend(float dist)
	{
		return new Line3f(
				Vector3f.sub(a,(Vector3f) ab().normalise().scale(dist),null),
				Vector3f.add(b,(Vector3f) ab().normalise().scale(dist),null)
				);
	}
	
	
}
