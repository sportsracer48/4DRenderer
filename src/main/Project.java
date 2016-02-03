package main;
/* 
 * Copyright (c) 2002-2008 LWJGL Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'LWJGL' nor the names of
 *   its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

/**
 * Project.java
 * <p/>
 * <p/>
 * Created 11-jan-2004
 *
 * @author Erik Duijs
 */
public class Project{

	private static final float[] IDENTITY_MATRIX =
		new float[] {
			1.0f, 0.0f, 0.0f, 0.0f,
			0.0f, 1.0f, 0.0f, 0.0f,
			0.0f, 0.0f, 1.0f, 0.0f,
			0.0f, 0.0f, 0.0f, 1.0f };

	public static final FloatBuffer matrix = BufferUtils.createFloatBuffer(16);


	/**
	 * Make matrix an identity matrix
	 */
	private static void __gluMakeIdentityf(FloatBuffer m) {
		m.clear();
		m.put(IDENTITY_MATRIX);
		m.flip();
	}

	/**
	 * Method gluPerspective.
	 *
	 * @param fovy
	 * @param aspect
	 * @param zNear
	 * @param zFar
	 */
	public static FloatBuffer gluPerspective(float fovy, float aspect, float zNear, float zFar) {
		float sine, cotangent, deltaZ;
		float radians = (float) (fovy / 2 * Math.PI / 180);

		deltaZ = zFar - zNear;
		sine = (float) Math.sin(radians);

		if ((deltaZ == 0) || (sine == 0) || (aspect == 0)) {
			return null;
		}

		cotangent = (float) Math.cos(radians) / sine;

		__gluMakeIdentityf(matrix);

		matrix.put(0 * 4 + 0, cotangent / aspect);
		matrix.put(1 * 4 + 1, cotangent);
		matrix.put(2 * 4 + 2, - (zFar + zNear) / deltaZ);
		matrix.put(2 * 4 + 3, -1);
		matrix.put(3 * 4 + 2, -2 * zNear * zFar / deltaZ);
		matrix.put(3 * 4 + 3, 0);

		return matrix;
	}
	
	public static Matrix4f gluOrtho(float left, float right, float bottom, float top, float near, float far)
	{
		float[] mat={
				2/(right-left),0,0,-(right+left)/(right-left),
				0,2/(top-bottom),0,-(top+bottom)/(top-bottom),
				0,0,-2/(far-near),(far+near)/(far-near),
				0,0,0,1
			};
			matrix.clear();
			matrix.put(mat);
			matrix.flip();
			return (Matrix4f) new Matrix4f().load(matrix);
	}

	public static FloatBuffer gluLookAt4(
			float centerx,
			float centery,
			float centerz,
			float centerw) {
			
			float[] zaxis={centerx,centery,centerz,centerw};
			zaxis=normalize4(zaxis);
			float[] yaxis={0,1,0,0};
			float[] xaxis={1,0,0,0};
			float[] waxis=normalize4(cross4(xaxis,zaxis,yaxis));
			yaxis=normalize4(cross4(zaxis,xaxis,waxis));
			xaxis=normalize4(cross4(zaxis,waxis,yaxis));

			__gluMakeIdentityf(matrix);
			matrix.put(0 * 4 + 0, xaxis[0]);
			matrix.put(1 * 4 + 0, xaxis[1]);
			matrix.put(2 * 4 + 0, xaxis[2]);
			matrix.put(3 * 4 + 0, xaxis[3]);

			matrix.put(0 * 4 + 1, yaxis[0]);
			matrix.put(1 * 4 + 1, yaxis[1]);
			matrix.put(2 * 4 + 1, yaxis[2]);
			matrix.put(3 * 4 + 1, yaxis[3]);

			matrix.put(0 * 4 + 2, zaxis[0]);
			matrix.put(1 * 4 + 2, zaxis[1]);
			matrix.put(2 * 4 + 2, zaxis[2]);
			matrix.put(3 * 4 + 2, zaxis[3]);
			
			matrix.put(0 * 4 + 3, waxis[0]);
			matrix.put(1 * 4 + 3, waxis[1]);
			matrix.put(2 * 4 + 3, waxis[2]);
			matrix.put(3 * 4 + 3, waxis[3]);
			
			return matrix;
		}
	public static FloatBuffer gluLookAt(
			float centerx,
			float centery,
			float centerz) {
			
			float[] zaxis={centerx,centery,centerz};
			normalize(zaxis);
			zaxis=normalize(zaxis);
			float[] yaxis={0,1,0};
			float[] xaxis=new float[3];
			cross(zaxis,yaxis,xaxis);
			normalize(xaxis);
			cross(xaxis,zaxis,yaxis);
			normalize(yaxis);

			__gluMakeIdentityf(matrix);
			matrix.put(0 * 4 + 0, xaxis[0]);
			matrix.put(1 * 4 + 0, xaxis[1]);
			matrix.put(2 * 4 + 0, xaxis[2]);

			matrix.put(0 * 4 + 1, yaxis[0]);
			matrix.put(1 * 4 + 1, yaxis[1]);
			matrix.put(2 * 4 + 1, yaxis[2]);

			matrix.put(0 * 4 + 2, -zaxis[0]);
			matrix.put(1 * 4 + 2, -zaxis[1]);
			matrix.put(2 * 4 + 2, -zaxis[2]);
			
			return matrix;
		}
	
	public static Matrix4f scale(float x, float y, float z, float w)
	{
		float[] mat={
				x,0,0,0,
				0,y,0,0,
				0,0,z,0,
				0,0,0,w
			};
			matrix.clear();
			matrix.put(mat);
			matrix.flip();
			return (Matrix4f) new Matrix4f().loadTranspose(matrix);
	}
	
	public static Matrix4f rotXY(float theta)
	{
		float[] mat={
			(float) cos(theta),(float) sin(theta),0,0,
			(float) -sin(theta),(float) cos(theta),0,0,
			0,0,1,0,
			0,0,0,1
		};
		matrix.clear();
		matrix.put(mat);
		matrix.flip();
		return (Matrix4f) new Matrix4f().loadTranspose(matrix);
		
	}
	public static Matrix4f rotXZ(float theta)
	{
		float[] mat={
			(float) cos(theta),0,(float) -sin(theta),0,
			0,1,0,0,
			(float) sin(theta),0,(float) cos(theta),0,
			0,0,0,1
		};
		matrix.clear();
		matrix.put(mat);
		matrix.flip();
		return (Matrix4f) new Matrix4f().loadTranspose(matrix);
	}
	public static Matrix4f rotYU(float theta)
	{
		float[] mat={
			1,0,0,0,
			0,(float) cos(theta),0,(float) -sin(theta),
			0,0,1,0,
			0,(float) sin(theta),0,(float) cos(theta)
		};
		matrix.clear();
		matrix.put(mat);
		matrix.flip();
		return (Matrix4f) new Matrix4f().loadTranspose(matrix);
	}
	public static Matrix4f rotYZ(float theta)
	{
		float[] mat={
			1,0,0,0,
			0,(float) cos(theta),(float) sin(theta),0,
			0,(float) -sin(theta),(float) cos(theta),0,
			0,0,0,1
		};
		matrix.clear();
		matrix.put(mat);
		matrix.flip();
		return (Matrix4f) new Matrix4f().loadTranspose(matrix);
	}
	public static Matrix4f rotXU(float theta)
	{
		float[] mat={
			(float) cos(theta),0,0,(float) sin(theta),
			0,1,0,0,
			0,0,1,0,
			(float) -sin(theta),0,0,(float) cos(theta)
		};
		matrix.clear();
		matrix.put(mat);
		matrix.flip();
		return (Matrix4f) new Matrix4f().loadTranspose(matrix);
	}
	public static Matrix4f rotZU(float theta)
	{
		float[] mat={
			1,0,0,0,
			0,1,0,0,
			0,0,(float) cos(theta),(float) -sin(theta),
			0,0,(float) sin(theta),(float) cos(theta)
		};
		matrix.clear();
		matrix.put(mat);
		matrix.flip();
		return (Matrix4f) new Matrix4f().loadTranspose(matrix);
	}
	
	
	public static float[] normalize4(float[] v)
	{
		float[] toReturn=new float[v.length];
		float mag=0;
		for(int i=0;i<v.length;i++)
		{
			mag+=v[i]*v[i];
		}
		mag=(float) Math.sqrt(mag);
		for(int i=0;i<v.length;i++)
		{
			toReturn[i]=v[i]/mag;
		}
		return toReturn;
	}
	
	public static Vector4f cross4(Vector4f U, Vector4f V, Vector4f W)
	{
		float[] r=cross4(new float[]{U.x,U.y,U.z,U.w},
						 new float[]{V.x,V.y,V.z,V.w},
						 new float[]{W.x,W.y,W.z,W.w});
		return new Vector4f(r[0],r[1],r[2],r[3]);
	}
	
	public static float[] cross4 (float[] U, float[] V, float[] W)
	{
	    final float A, B, C, D, E, F;       // Intermediate Values

	    // Calculate intermediate values.

	    A = (V[0] * W[1]) - (V[1] * W[0]);
	    B = (V[0] * W[2]) - (V[2] * W[0]);
	    C = (V[0] * W[3]) - (V[3] * W[0]);
	    D = (V[1] * W[2]) - (V[2] * W[1]);
	    E = (V[1] * W[3]) - (V[3] * W[1]);
	    F = (V[2] * W[3]) - (V[3] * W[2]);

	    // Calculate the result-vector components.
	    float[] result=new float[4];
	    result[0] =   (U[1] * F) - (U[2] * E) + (U[3] * D);
	    result[1] = - (U[0] * F) + (U[2] * C) - (U[3] * B);
	    result[2] =   (U[0] * E) - (U[1] * C) + (U[3] * A);
	    result[3] = - (U[0] * D) + (U[1] * B) - (U[2] * A);

	    return result;
	}
	
	public static float[] normalize(float[] v) 
	{
		float r;

		r = (float)Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
		if ( r == 0.0 )
			return v;

		r = 1.0f / r;

		v[0] *= r;
		v[1] *= r;
		v[2] *= r;

		return v;
	}

	public static void cross(float[] v1, float[] v2, float[] result)
	{
		result[0] = v1[1] * v2[2] - v1[2] * v2[1];
		result[1] = v1[2] * v2[0] - v1[0] * v2[2];
		result[2] = v1[0] * v2[1] - v1[1] * v2[0];
	}
	
	//#define cross4(U,V,W) vec4(((U).y * (((V).z * (W).w) - ((V).w * (W).z))) - ((U).z * (((V).y * (W).w) - ((V).w * (W).y))) + ((U).w * (((V).y * (W).z) - ((V).z * (W).y))),- ((U).x * (((V).z * (W).w) - ((V).w * (W).z))) + ((U).z * (((V).x * (W).w) - ((V).w * (W).x))) - ((U).w * (((V).x * (W).z) - ((V).z * (W).x))),((U).x * (((V).y * (W).w) - ((V).w * (W).y))) - ((U).y * (((V).x * (W).w) - ((V).w * (W).x))) + ((U).w * (((V).x * (W).y) - ((V).y * (W).x))),- ((U).x * (((V).y * (W).z) - ((V).z * (W).y))) + ((U).y * (((V).x * (W).z) - ((V).z * (W).x))) - ((U).z * (((V).x * (W).y) - ((V).y * (W).x))))

}
