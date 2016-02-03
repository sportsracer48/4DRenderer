package main;

import instance.InstanceRenderer;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;

import model.ColorMaker;
import model.HeightMap4D;
import model.MinimapFront;
import model.MinimapBack;
import model.Model3D;
import model.Model4D;
import model.Plane;
import model.PlayerIcon;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import collision.CollisionMesh;
import collision.Line3f;
import collision.HitBox;
import collision.SphereBox;
import shader.Program;
import shader.Shader;
import shader.Uniform;
import static java.lang.Math.*;

public class ScrollUp
{
	static Shader vs3D;
	static Shader vs2D;
	static Shader vs4D;
	static Shader gs4D;
	static Shader gs3D;
	static Shader gs2D;
	static Shader fs;
	static Shader fs4D;
	static Program program4D;
	static Program program3D;
	static Program program2D;
	
	static InstanceRenderer groundRenderer;
	static InstanceRenderer cylRenderer;
	static InstanceRenderer coneRenderer;
	static InstanceRenderer wallRenderer;
	static InstanceRenderer wallBaseRenderer;
	static InstanceRenderer treeBaseRenderer;
	static InstanceRenderer ballRenderer;
	static InstanceRenderer icoRenderer;
	
	static Uniform viewMatrixId4D;
	static Uniform eyePosId4D;
	static Uniform projectionMatrixId4D;
	static Uniform flatShadeId4D;

	static Uniform modelMatrixId3D;
	static Uniform modelPosId3D;
	static Uniform viewMatrixId3D;
	static Uniform normalMatrixId3D;
	static Uniform eyePosId3D;
	static Uniform projectionMatrixId3D;
	
	static Uniform viewMatrixId2D;
	static Uniform eyePosId2D;
	static Uniform projectionMatrixId2D;
	static Uniform xOffsetId2D;

	static PlayerIcon playerIcon;
	
	static ArrayList<CollisionMesh> obsticles=new ArrayList<CollisionMesh>();

	static Matrix4f view;
	static Matrix4f inverseView;

	static float psi=0;
	static float yaw=(float) (PI/2);
	static float pitch=(float) (PI/2);
	static boolean flying=false;
	static float  playerRadius=.3f;

	static float[] cam={32,0,32,32};
	
	static float[] lastPos={32,32,32};
	static float[] pos={32,32,32};
	
	static float[] camDir={0,0,0,0};
	static HeightMap4D heightMap;
	static MinimapFront minimapFront;
	static MinimapBack minimapBack;
	static Plane mapPlane;
	static Entity[] trees=new Entity[500];
	static Entity[] walls=new Entity[50];

	static int width;
	static int height;

	public static void main(String[] args) throws Exception
	{	
		init();

		long lastTime=System.currentTimeMillis();
		long dt=0;
		int time=0;
		int frames=0;
		

		while(!Display.isCloseRequested())
		{
			lastTime=System.currentTimeMillis();

			update(dt);
			render();
			dt=System.currentTimeMillis()-lastTime;
			frames++;
			time+=dt;
			if(time>1000)
			{
				System.out.println(frames+" FPS");
				time=0;
				frames=0;
			}
		}
		Display.destroy();

	}

	public static void init() throws Exception
	{
		System.setProperty("org.lwjgl.opengl.Window.undecorated", "True");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width=screenSize.width;
		height=screenSize.height;
		
		Display.setDisplayMode(new DisplayMode(width,height));
		Display.setVSyncEnabled(true);
		PixelFormat pixelFormat = new PixelFormat(32,0,8,8,4);
		ContextAttribs contextAtrributes = new ContextAttribs(3, 3).withProfileCore(true);

		Display.create(pixelFormat,contextAtrributes);
		Mouse.setGrabbed(true);


		Model3D sphere=new Model3D("sphere.obj");
		Model3D hemisphere=new Model3D("hemisphereuv.obj");
		Model3D buildingBase=new Model3D("untitled.obj");
		Model4D cyl=new Model4D(sphere,8,false,false);
		Model4D cone=new Model4D(sphere,0,8,0,0);
		Model4D house=new Model4D(buildingBase,4,true,false);
		Model4D hyperSphere=new Model4D(hemisphere);
		Model4D cell600=new Model4D("600cell.obj4D");

		cyl.color=new ColorMaker(.09f,.045f,
				.025f,.05f,
				.025f,.05f
				);
		cone.color=new ColorMaker(.05f,.1f,
				.2f,.1f,
				.05f,.1f
				);

		house.color=new ColorMaker(.4f,.1f,
				.4f,.2f,
				.4f,.2f);
		
		hyperSphere.color=new ColorMaker(.2f,.2f,
										 .2f,.2f,
										 .7f,.3f);
		
		cell600.color=new ColorMaker(.2f,.2f,
				 .2f,.2f,
				 .7f,.3f);

		heightMap=new HeightMap4D("saved.png");
		minimapFront=new MinimapFront();
		minimapBack=new MinimapBack();
		mapPlane=new Plane();

		ModelRenderer canopy=new ModelRenderer(cone);
		canopy.setOffset(0, 3, 0, 0);
		canopy.setScale(1.5f, 1.2f, 1.5f,1.5f);

		ModelRenderer trunk=new ModelRenderer(cyl);
		trunk.setOffset(0, -3, 0, 0);
		trunk.setScale(.4f, 1, .4f,.4f);

		ModelRenderer wall=new ModelRenderer(house);
		wall.setScale(1, 2, 1, 1);
		wall.setOffset(0,-3f,0,0);

		playerIcon=new PlayerIcon();
		
		cylRenderer=new InstanceRenderer(cyl);
		coneRenderer=new InstanceRenderer(cone);
		treeBaseRenderer=new InstanceRenderer(cyl.getBase());
		
		wallRenderer=new InstanceRenderer(house);
		wallBaseRenderer=new InstanceRenderer(house.getBase());
		
		groundRenderer=new InstanceRenderer(heightMap);
		groundRenderer.addInstance(new Matrix4f(), new Vector4f());
		groundRenderer.finish();
		
		ballRenderer=new InstanceRenderer(hyperSphere);
		ballRenderer.addInstance(new Matrix4f(), new Vector4f(30,heightMap.heightAt(30,32,32)+2,32,32));
		ballRenderer.finish();
		
		icoRenderer=new InstanceRenderer(cell600);
		icoRenderer.addInstance(Project.scale(1.3f, 1.3f, 1.3f, 1.3f), new Vector4f(34,heightMap.heightAt(34,32,32)+2,32,32));
		icoRenderer.finish();

		for(int i=0;i<trees.length;i++)
		{
			float x,z,w;
			x=(float) (heightMap.width*1.98f*Math.random());
			z=(float) (heightMap.width*1.98f*Math.random());
			w=(float) (heightMap.width*1.98f*Math.random());
			trees[i]=new Entity(x,heightMap.heightAt(x,z,w),z,w,trunk,canopy);
			
			cylRenderer.addInstance(trees[i], 0);
			coneRenderer.addInstance(trees[i], 1);
			treeBaseRenderer.addBaseInstance(trees[i]);
			
			obsticles.add(new SphereBox(x,z,w,.4f+playerRadius));
		}

		for(int i=0;i<walls.length;i++)
		{
			float x,z,w;
			x=(float) (heightMap.width*1.98f*Math.random());
			z=(float) (heightMap.width*1.98f*Math.random());
			w=(float) (heightMap.width*1.98f*Math.random());
			walls[i]=new Entity(x,heightMap.heightAt(x,z,w),z,w,wall);
			walls[i].yaw=(float) (Math.random()*2*PI);
			walls[i].pitch=(float) (Math.random()*PI-PI/2);
			
			wallRenderer.addInstance(walls[i], 0);
			wallBaseRenderer.addBaseInstance(walls[i]);
			
			float halfLength=3.682408f+playerRadius;
			float sideLength=halfLength*2;
			float halfDepth=.049501f+playerRadius;
			float sideDepth=halfDepth*2;
			Vector4f iVec=new Vector4f(0,0,sideLength,0);
			Vector4f j=new Vector4f(sideLength,0,0,0);
			Vector4f k=new Vector4f(0,sideDepth,0,0);
			Vector4f startOffset=new Vector4f(-halfLength,-halfDepth,-halfLength,0);
			
			Matrix4f model=Project.rotXY(-walls[i].yaw);
			Matrix4f.mul(Project.rotXZ(-walls[i].pitch), model, model);
			
			Matrix4f.transform(model, iVec, iVec);
			Matrix4f.transform(model, j, j);
			Matrix4f.transform(model, k, k);
			Matrix4f.transform(model, startOffset, startOffset);

			obsticles.add(new HitBox(
					new Vector3f(x+startOffset.x,z+startOffset.y,w+startOffset.z),
					new Vector3f(iVec.x,iVec.y,iVec.z),
					new Vector3f(j.x,j.y,j.z),
					new Vector3f(k.x,k.y,k.z)
					));
		}
		
		cylRenderer.finish();
		coneRenderer.finish();
		treeBaseRenderer.finish();
		
		wallRenderer.finish();
		wallBaseRenderer.finish();


		vs2D=new Shader("vs2D.glsl",GL20.GL_VERTEX_SHADER);
		vs3D=new Shader("vs.glsl",GL20.GL_VERTEX_SHADER);
		vs4D=new Shader("vs4D.glsl",GL20.GL_VERTEX_SHADER);
		gs4D=new Shader("unrollGs.glsl",GL32.GL_GEOMETRY_SHADER);
		gs3D=new Shader("gs3D.glsl",GL32.GL_GEOMETRY_SHADER);
		gs2D=new Shader("Gs2D.glsl",GL32.GL_GEOMETRY_SHADER);
		fs=new Shader("fs.glsl",GL20.GL_FRAGMENT_SHADER);
		fs4D=new Shader("fs4D.glsl",GL20.GL_FRAGMENT_SHADER);

		program4D=new Program();
		program3D=new Program();
		program2D=new Program();

		program4D.addShader(vs4D);
		program4D.addShader(gs4D);
		program4D.addShader(fs4D);

		program3D.addShader(vs3D);
		program3D.addShader(gs3D);
		program3D.addShader(fs);
		
		program2D.addShader(vs2D);
		program2D.addShader(gs2D);
		program2D.addShader(fs);

		program3D.bindAttribLocation(0, "vertexIn");
		program3D.bindAttribLocation(1, "colorIn");
		program3D.bindAttribLocation(2, "normIn");

		program4D.link();
		program3D.link();
		program2D.link();

		viewMatrixId4D=program4D.getUniform("viewMatrix");
		projectionMatrixId4D=program4D.getUniform("projectionMatrix");
		eyePosId4D=program4D.getUniform("eyePos");
		flatShadeId4D=program4D.getUniform("flatShade");

		modelMatrixId3D=program3D.getUniform("model");
		modelPosId3D=program3D.getUniform("modelPos");
		viewMatrixId3D=program3D.getUniform("viewMatrix");
		normalMatrixId3D=program3D.getUniform("normalMatrix");
		projectionMatrixId3D=program3D.getUniform("projectionMatrix");
		eyePosId3D=program3D.getUniform("eyePos");
		
		viewMatrixId2D=program2D.getUniform("viewMatrix");
		projectionMatrixId2D=program2D.getUniform("projectionMatrix");
		eyePosId2D=program2D.getUniform("eyePos");
		xOffsetId2D=program2D.getUniform("xOffset");

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL13.GL_MULTISAMPLE);
		GL11.glDisable(GL11.GL_DITHER);
		GL11.glClearColor(0f, 0f, 0f, 0f);

		program4D.bind();
		projectionMatrixId4D.set4M(Project.gluPerspective(70, (float)width/height, .1f, 100));
		program3D.bind();
		projectionMatrixId3D.set4M(Project.gluPerspective(60, (float)width/height, .1f, 100));
		program2D.bind();
		projectionMatrixId2D.set4M(Project.gluPerspective(60, (float)width/height, .1f, 100));
	}
	
	public static void update(long dt)
	{
		while(Keyboard.next())
		{
			if(Keyboard.getEventKeyState())
			{
				if(Keyboard.getEventKey()==Keyboard.KEY_SPACE)
				{
					flying=!flying;
				}
			}
		}

		psi+=Mouse.getDWheel()*.00003f;
		yaw+=Mouse.getDX()*.001f;
		if(!Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
		{
			pitch-=Mouse.getDY()*.001f;
		}
		else
		{
			psi-=Mouse.getDY()*.001f;
		}

		if(pitch<.01)
		{
			pitch=.01f;
		}
		if(pitch>PI-.01)
		{
			pitch=(float) (PI-.01);
		}

		camDir[0]=(float) (sin(pitch)*cos(yaw));
		camDir[1]=(float) (cos(pitch));
		camDir[2]=(float) (sin(pitch)*sin(yaw));
		
		Vector4f out=new Vector4f(0,0,0,1);
		Vector4f forward=new Vector4f(0,(float)-cos(pitch),-(float)sin(pitch),0);
		if(flying)
		{
			forward=new Vector4f(0,0,-1,0);
		}
		Vector4f left=new Vector4f(1,0,0,0);
		Vector4f right=new Vector4f(-1,0,0,0);

		view = Project.rotZU(psi);
		Matrix4f view2=(Matrix4f) new Matrix4f().load(Project.gluLookAt(camDir[0],camDir[1],camDir[2]));
		Matrix4f.mul(view2, view, view);

		inverseView=Matrix4f.invert(view, null);
		Matrix4f.transform(inverseView, forward, forward);
		Matrix4f.transform(inverseView, left, left);
		Matrix4f.transform(inverseView, right, right);
		Matrix4f.transform(inverseView, out, out);
		
		lastPos[0]=cam[0];
		lastPos[1]=cam[2];
		lastPos[2]=cam[3];
		
		
		float speed=.007f;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
		{
			speed=.001f;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W))
		{
			cam[0]+=forward.x*speed*dt;
			cam[1]+=forward.y*speed*dt;
			cam[2]+=forward.z*speed*dt;
			cam[3]+=forward.w*speed*dt;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			cam[0]-=forward.x*speed*dt;
			cam[1]-=forward.y*speed*dt;
			cam[2]-=forward.z*speed*dt;
			cam[3]-=forward.w*speed*dt;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A))
		{
			cam[0]+=right.x*speed*dt;
			cam[1]+=right.y*speed*dt;
			cam[2]+=right.z*speed*dt;
			cam[3]+=right.w*speed*dt;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			cam[0]+=left.x*speed*dt;
			cam[1]+=left.y*speed*dt;
			cam[2]+=left.z*speed*dt;
			cam[3]+=left.w*speed*dt;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_Q))
		{
			cam[0]+=out.x*speed*dt;
			cam[1]+=out.y*speed*dt;
			cam[2]+=out.z*speed*dt;
			cam[3]+=out.w*speed*dt;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_E))
		{
			cam[0]-=out.x*speed*dt;
			cam[1]-=out.y*speed*dt;
			cam[2]-=out.z*speed*dt;
			cam[3]-=out.w*speed*dt;
		}
		if(!flying)
		{
			if(cam[0]<0)
			{
				cam[0]=0;
			}
			if(cam[2]<0)
			{
				cam[2]=0;
			}
			if(cam[3]<0)
			{
				cam[3]=0;
			}

			if(cam[0]>heightMap.width*2-2.2)
			{
				cam[0]=heightMap.width*2-2.2f;
			}
			if(cam[2]>heightMap.width*2-2.2)
			{
				cam[2]=heightMap.width*2-2.2f;
			}
			if(cam[3]>heightMap.width*2-2.2)
			{
				cam[3]=heightMap.width*2-2.2f;
			}

			cam[1]=heightMap.heightAt(cam[0],cam[2],cam[3])+2;
			
			pos[0]=cam[0];
			pos[1]=cam[2];
			pos[2]=cam[3];
			
			Vector3f start=new Vector3f(lastPos[0],lastPos[1],lastPos[2]);
			Vector3f end=new Vector3f(pos[0],pos[1],pos[2]);
			Line3f player=new Line3f(start,end);
			
			boolean oneIntersect=false;
			
			for(CollisionMesh m:obsticles)
			{
				if(oneIntersect && m.intersects(player))
				{
					cam[0]=lastPos[0];
					cam[2]=lastPos[1];
					cam[3]=lastPos[2];
					cam[1]=heightMap.heightAt(cam[0],cam[2],cam[3])+2;
					break;
				}
				if(m.intersects(player))
				{
					cam[0]=m.correctPos.x;
					cam[2]=m.correctPos.y;
					cam[3]=m.correctPos.z;
					cam[1]=heightMap.heightAt(cam[0],cam[2],cam[3])+2;
					oneIntersect=true;
				}
			}
		}
	}

	public static void render() throws Exception
	{
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT|GL11.GL_COLOR_BUFFER_BIT);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		program4D.bind();
		viewMatrixId4D.set4M(view);
		eyePosId4D.set4f(cam[0],cam[1],cam[2],cam[3]);
		
		groundRenderer.render();
		cylRenderer.render();
		coneRenderer.render();
		
		ballRenderer.render();
		
		flatShadeId4D.seti(1);
		wallRenderer.render();
		icoRenderer.render();
		flatShadeId4D.seti(0);




		program3D.bind();
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT|GL11.GL_STENCIL_BUFFER_BIT);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		//view
		eyePosId3D.set4f(4f*1080/1920*width/height,2,5,0);
		Matrix4f model=new Matrix4f();
		viewMatrixId3D.set4M(model);
		
		//back
		model=new Matrix4f();
		modelMatrixId3D.set4M(model);
		modelPosId3D.set4f(0, 0, 0, 0);
		minimapBack.render();

		float x=cam[0]/heightMap.width/2-.5f;
		float z=cam[2]/heightMap.width/2-.5f;
		float w=cam[3]/heightMap.width/2-.5f;

		//plane stencil
		GL11.glEnable(GL11.GL_STENCIL_TEST);
		modelPosId3D.set4f(-x,-z,-w,0);
		model=new Matrix4f();
		Matrix4f.mul(Project.rotYZ((float) (psi+PI/2f)), model, model);
		modelMatrixId3D.set4M(model);
		GL11.glStencilFunc(GL11.GL_ALWAYS, 1, -1);
		GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);
		GL11.glColorMask(false, false, false, false);
		GL11.glDepthMask(false);
		mapPlane.render();
		
		//front
		model=new Matrix4f();
		modelMatrixId3D.set4M(model);
		modelPosId3D.set4f(0, 0, 0, 0);
		GL11.glDepthMask(true);
		GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
		minimapFront.render();

		//plane
		modelPosId3D.set4f(-x,-z,-w,0);
		model=new Matrix4f();
		Matrix4f.mul(Project.rotYZ((float) (psi+PI/2f)), model, model);
		modelMatrixId3D.set4M(model);
		GL11.glDepthFunc(GL11.GL_GREATER);
		GL11.glStencilFunc(GL11.GL_EQUAL, 1, -1);
		GL11.glColorMask(true, true, true, true);
		GL11.glDepthMask(true);
		mapPlane.render();
		
		
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glDisable(GL11.GL_STENCIL_TEST);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		
		//playericon
		model=new Matrix4f();
		Matrix4f.mul(Project.scale(.2f, .2f, .2f, .2f), model, model);
		Matrix4f.mul(Project.rotXZ((float) (yaw+PI/2)), model, model);
		Matrix4f.mul(Project.rotYZ((float) (psi+PI/2f)), model, model);
		modelMatrixId3D.set4M(model);
		modelPosId3D.set4f(-x,-z,-w,0);
		playerIcon.render();

		//front
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		model=new Matrix4f();
		modelMatrixId3D.set4M(model);
		modelPosId3D.set4f(0, 0, 0, 0);
		minimapFront.render();
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		GL11.glDisable(GL11.GL_BLEND);
		
		eyePosId3D.set4f(-4f*1080/1920*width/height, -2, 5, 0);
		model=new Matrix4f();
		Matrix4f.mul(Project.scale(.2f, .2f, .2f, .2f), model, model);
		Matrix4f.mul(Project.rotXZ((float) (-yaw-PI/2)), model, model);
		Matrix4f.mul(Project.rotYZ((float) (PI/2)), model, model);
		modelMatrixId3D.set4M(model);
		modelPosId3D.set4f(0,0,0,0);
		playerIcon.render();
		
		//trees n shit
		program2D.bind();
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		xOffsetId2D.setf(4f*1080/1920*width/height);
		eyePosId2D.set4f(cam[0]/64f-.5f,cam[2]/64f-.5f,cam[3]/64f-.5f,0);	
		model=Project.scale(1,-1,1,1);
		Matrix4f.mul(Project.rotYZ(psi), model, model);
		viewMatrixId2D.set4M(model);
		
		wallBaseRenderer.render();
		treeBaseRenderer.render();
		
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
				
		Display.update();
	}


}
