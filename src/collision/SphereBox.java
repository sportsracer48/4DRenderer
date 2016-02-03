package collision;

import org.lwjgl.util.vector.Vector3f;

public class SphereBox extends CollisionMesh
{
	Vector3f center;
	float radius;
	public SphereBox(Vector3f center, float radius)
	{
		this.center=center;
		this.radius=radius;
	}
	
	public SphereBox(float x, float y, float z, float radius)
	{
		this.center=new Vector3f(x,y,z);
		this.radius=radius;
	}
	
	public boolean intersects(Line3f line)
	{
		boolean collide=line.dist(center)<radius;
		if(collide)
		{
			correctPos=pointOnSurface(line.b);
			return true;
		}
		return false;
	}
	
	public Vector3f pointOnSurface(Vector3f p)
	{
		Vector3f op=Vector3f.sub(p, center, null);
		return Vector3f.add((Vector3f) op.normalise().scale(radius),center,null);
	}
}
