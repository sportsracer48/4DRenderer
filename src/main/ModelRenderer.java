package main;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import shader.Uniform;
import model.Model;

public class ModelRenderer
{
	Model m;
	float x=0,y=0,z=0,w=0;
	float xScale=1,yScale=1,zScale=1,wScale=1;
	
	public ModelRenderer(Model m)
	{
		this.m=m;
	}
	
	public void setOffset(float x, float y, float z, float w)
	{
		this.x=x;
		this.y=y;
		this.z=z;
		this.w=w;
	}
	
	public void setScale(float x, float y, float z, float w)
	{
		this.xScale=x;
		this.yScale=y;
		this.zScale=z;
		this.wScale=w;
	}
	
	public Vector4f getPosition(float x, float y, float z, float w,float yaw, float pitch)
	{
		return new Vector4f(x+this.x,y+this.y,z+this.z,w+this.w);
	}
	
	public Matrix4f getModelMatrix(float x, float y, float z, float w,float yaw, float pitch)
	{
		Matrix4f model=Project.scale(xScale, yScale, zScale, wScale);
		Matrix4f.mul(Project.rotXZ(yaw), model, model);
		Matrix4f.mul(Project.rotXU(pitch), model, model);
		
		return model;
	}
	
	public void renderBase(Uniform modelMat, Uniform modelPos, float x, float z, float w,float yaw, float pitch)
	{
		Matrix4f model=Project.scale(xScale/64f, zScale/64f, wScale/64f, 1);
		Matrix4f.mul(Project.rotXY(-yaw), model, model);
		Matrix4f.mul(Project.rotXZ(-pitch), model, model);
		
		modelMat.set4M(model);
		modelPos.set4f((x+this.x)/64f-.5f,(z+this.z)/64f-.5f,(w+this.w)/64f-.5f,0);
		
		m.getBase().render();
	}

	public Vector4f getBasePosition(float x, float z, float w,float yaw, float pitch)
	{
		return new Vector4f((x+this.x)/64f-.5f,(z+this.z)/64f-.5f,(w+this.w)/64f-.5f,0);
	}

	public Matrix4f getBaseMatrix(float x, float z, float w, float yaw, float pitch)
	{
		Matrix4f model=Project.scale(xScale/64f, zScale/64f, wScale/64f, 1);
		Matrix4f.mul(Project.rotXY(-yaw), model, model);
		Matrix4f.mul(Project.rotXZ(-pitch), model, model);
		return model;
	}
}
