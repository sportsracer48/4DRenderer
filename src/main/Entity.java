package main;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import shader.Uniform;

public class Entity
{
	float x,y,z,w;
	float yaw;
	float pitch;
	
	ModelRenderer[] modelRenderers;
	
	public Entity(float x, float y, float z, float w, ModelRenderer...modelRenderers)
	{
		this.x=x;
		this.y=y;
		this.z=z;
		this.w=w;
		
		this.modelRenderers=modelRenderers;
	}
	
	public Vector4f getPostition(int part)
	{
		return modelRenderers[part].getPosition(x, y, z, w, yaw, pitch);
	}
	
	public Matrix4f getModelMatrix(int part)
	{
		return modelRenderers[part].getModelMatrix(x, y, z, w, yaw, pitch);
	}
	
	public Matrix4f getBaseMatrix()
	{
		return modelRenderers[0].getBaseMatrix(x, z, w, yaw, pitch);
	}
	public Vector4f getBasePosition()
	{
		return modelRenderers[0].getBasePosition(x, z, w, yaw, pitch);
	}
	
	public void renderBase(Uniform modelMat, Uniform modelPos)
	{
		modelRenderers[0].renderBase(modelMat, modelPos, x, z, w, yaw,pitch);
	}
}
