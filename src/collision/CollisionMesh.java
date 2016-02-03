package collision;

import org.lwjgl.util.vector.Vector3f;

public abstract class CollisionMesh
{
	public Vector3f correctPos;
	public abstract boolean intersects(Line3f line);
}
