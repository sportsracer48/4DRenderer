package collision;

import org.lwjgl.util.vector.Vector3f;

public class HitBox extends CollisionMesh
{
	PlaneBox[] boxes=new PlaneBox[6];
	public HitBox(Vector3f start, Vector3f i, Vector3f j, Vector3f k)
	{
		boxes[0]=new PlaneBox(start,i,j);
		boxes[1]=new PlaneBox(Vector3f.add(start,k,null),j,i);
		
		boxes[2]=new PlaneBox(start,k,i);
		boxes[3]=new PlaneBox(Vector3f.add(start,j,null),i,k);
		
		boxes[4]=new PlaneBox(start,j,k);
		boxes[5]=new PlaneBox(Vector3f.add(start,i,null),k,j);
	}
	
	public boolean intersects(Line3f line)
	{
		float minDist=Float.MAX_VALUE;
		boolean intersected=false;
		for(PlaneBox b:boxes)
		{
			if(b.intersects(line))
			{
				if(b.dist<minDist)
				{
					correctPos=b.correctPos;
					minDist=b.dist;
					intersected=true;
				}
			}
		}
		return intersected;
	}
}
