#version 330
#define start0 0
#define start1 1
#define start2 2
#define start3 0
#define start4 1
#define start5 2

#define end0 1
#define end1 2
#define end2 0
#define end3 3
#define end4 3
#define end5 3

#define noSearch0 5
#define noSearch1 3
#define noSearch2 4
#define noSearch3 1
#define noSearch4 2
#define noSearch5 0

#define cross4(U,V,W) vec4(((U).y * (((V).z * (W).w) - ((V).w * (W).z))) - ((U).z * (((V).y * (W).w) - ((V).w * (W).y))) + ((U).w * (((V).y * (W).z) - ((V).z * (W).y))),- ((U).x * (((V).z * (W).w) - ((V).w * (W).z))) + ((U).z * (((V).x * (W).w) - ((V).w * (W).x))) - ((U).w * (((V).x * (W).z) - ((V).z * (W).x))),((U).x * (((V).y * (W).w) - ((V).w * (W).y))) - ((U).y * (((V).x * (W).w) - ((V).w * (W).x))) + ((U).w * (((V).x * (W).y) - ((V).y * (W).x))),- ((U).x * (((V).y * (W).z) - ((V).z * (W).y))) + ((U).y * (((V).x * (W).z) - ((V).z * (W).x))) - ((U).z * (((V).x * (W).y) - ((V).y * (W).x))))


layout(lines_adjacency) in;
layout(triangle_strip, max_vertices=4) out;

uniform mat4 projectionMatrix;
uniform int flatShade;

in vec4 vColor[4];
in vec4 vPos[4];
in vec4 vLight[4];
in vec4 vNorm[4];

out vec4 color;
out vec3 norm;
out vec4 viewPos;
flat out vec3 light;

void main()
{

	//culled!
	
	if(vPos[0].w>0 && vPos[1].w>0 && vPos[2].w>0 && vPos[3].w>0)
	{
		return;
	}
	if(vPos[0].w<0 && vPos[1].w<0 && vPos[2].w<0 && vPos[3].w<0)
	{
		return;
	}
	vec4 A=vPos[0]-vPos[3];
	vec4 B=vPos[1]-vPos[3];
	vec4 C=vPos[2]-vPos[3];
	vec4 cellNorm=cross4(A,B,C);
	if(dot(cellNorm,vPos[0])<=0)
	{
		return;
	}
	
	bool has0=false;
	bool has1=false;
	bool has2=false;
	bool has3=false;

	int edge0=0;
	int edge1=0;
	int edge2=0;
	int edge3=0;

	vec4 p0=vec4(0,0,0,0);
	vec4 p1=vec4(0,0,0,0);
	vec4 p2=vec4(0,0,0,0);
	vec4 p3=vec4(0,0,0,0);

	vec4 c0=vec4(0,0,0,0);
	vec4 c1=vec4(0,0,0,0);
	vec4 c2=vec4(0,0,0,0);
	vec4 c3=vec4(0,0,0,0);

	vec4 n0=vec4(0,0,0,0);
	vec4 n1=vec4(0,0,0,0);
	vec4 n2=vec4(0,0,0,0);
	vec4 n3=vec4(0,0,0,0);

	//find the first intersection

	vec4 edge=vec4(0,0,0,0);
	float u=0;

	int temp=0;
	vec4 tempP=vec4(0,0,0,0);
	vec4 tempC=vec4(0,0,0,0);
	vec4 tempN=vec4(0,0,0,0);

	if(!has3)
	{
	edge=vPos[end0]-vPos[start0];
	u=-vPos[start0].w/edge.w;
	if(u>=0 && u<=1)
	{
	if(!has0){has0=true;edge0=0;p0=vPos[start0]+u*edge;c0=vColor[start0]*(1-u)+vColor[end0]*u;n0=vNorm[start0]*(1-u)+vNorm[end0]*u;}
	else if(!has1){has1=true;edge1=0;p1=vPos[start0]+u*edge;c1=vColor[start0]*(1-u)+vColor[end0]*u;n1=vNorm[start0]*(1-u)+vNorm[end0]*u;}
	else if(!has2){has2=true;edge2=0;p2=vPos[start0]+u*edge;c2=vColor[start0]*(1-u)+vColor[end0]*u;n2=vNorm[start0]*(1-u)+vNorm[end0]*u;}
	else if(!has3){has3=true;edge3=0;p3=vPos[start0]+u*edge;c3=vColor[start0]*(1-u)+vColor[end0]*u;n3=vNorm[start0]*(1-u)+vNorm[end0]*u;}
	}
	}

	if(!has3)
	{
	edge=vPos[end1]-vPos[start1];
	u=-vPos[start1].w/edge.w;
	if(u>=0 && u<=1)
	{
	if(!has0){has0=true;edge0=1;p0=vPos[start1]+u*edge;c0=vColor[start1]*(1-u)+vColor[end1]*u;n0=vNorm[start1]*(1-u)+vNorm[end1]*u;}
	else if(!has1){has1=true;edge1=1;p1=vPos[start1]+u*edge;c1=vColor[start1]*(1-u)+vColor[end1]*u;n1=vNorm[start1]*(1-u)+vNorm[end1]*u;}
	else if(!has2){has2=true;edge2=1;p2=vPos[start1]+u*edge;c2=vColor[start1]*(1-u)+vColor[end1]*u;n2=vNorm[start1]*(1-u)+vNorm[end1]*u;}
	else if(!has3){has3=true;edge3=1;p3=vPos[start1]+u*edge;c3=vColor[start1]*(1-u)+vColor[end1]*u;n3=vNorm[start1]*(1-u)+vNorm[end1]*u;}
	}
	}

	if(!has3)
	{
	edge=vPos[end2]-vPos[start2];
	u=-vPos[start2].w/edge.w;
	if(u>=0 && u<=1)
	{
	if(!has0){has0=true;edge0=2;p0=vPos[start2]+u*edge;c0=vColor[start2]*(1-u)+vColor[end2]*u;n0=vNorm[start2]*(1-u)+vNorm[end2]*u;}
	else if(!has1){has1=true;edge1=2;p1=vPos[start2]+u*edge;c1=vColor[start2]*(1-u)+vColor[end2]*u;n1=vNorm[start2]*(1-u)+vNorm[end2]*u;}
	else if(!has2){has2=true;edge2=2;p2=vPos[start2]+u*edge;c2=vColor[start2]*(1-u)+vColor[end2]*u;n2=vNorm[start2]*(1-u)+vNorm[end2]*u;}
	else if(!has3){has3=true;edge3=2;p3=vPos[start2]+u*edge;c3=vColor[start2]*(1-u)+vColor[end2]*u;n3=vNorm[start2]*(1-u)+vNorm[end2]*u;}
	}
	}

	if(!has3)
	{
	edge=vPos[end3]-vPos[start3];
	u=-vPos[start3].w/edge.w;
	if(u>=0 && u<=1)
	{
	if(!has0){has0=true;edge0=3;p0=vPos[start3]+u*edge;c0=vColor[start3]*(1-u)+vColor[end3]*u;n0=vNorm[start3]*(1-u)+vNorm[end3]*u;}
	else if(!has1){has1=true;edge1=3;p1=vPos[start3]+u*edge;c1=vColor[start3]*(1-u)+vColor[end3]*u;n1=vNorm[start3]*(1-u)+vNorm[end3]*u;}
	else if(!has2){has2=true;edge2=3;p2=vPos[start3]+u*edge;c2=vColor[start3]*(1-u)+vColor[end3]*u;n2=vNorm[start3]*(1-u)+vNorm[end3]*u;}
	else if(!has3){has3=true;edge3=3;p3=vPos[start3]+u*edge;c3=vColor[start3]*(1-u)+vColor[end3]*u;n3=vNorm[start3]*(1-u)+vNorm[end3]*u;}
	}
	}

	if(!has3)
	{
	edge=vPos[end4]-vPos[start4];
	u=-vPos[start4].w/edge.w;
	if(u>=0 && u<=1)
	{
	if(!has0){has0=true;edge0=4;p0=vPos[start4]+u*edge;c0=vColor[start4]*(1-u)+vColor[end4]*u;n0=vNorm[start4]*(1-u)+vNorm[end4]*u;}
	else if(!has1){has1=true;edge1=4;p1=vPos[start4]+u*edge;c1=vColor[start4]*(1-u)+vColor[end4]*u;n1=vNorm[start4]*(1-u)+vNorm[end4]*u;}
	else if(!has2){has2=true;edge2=4;p2=vPos[start4]+u*edge;c2=vColor[start4]*(1-u)+vColor[end4]*u;n2=vNorm[start4]*(1-u)+vNorm[end4]*u;}
	else if(!has3){has3=true;edge3=4;p3=vPos[start4]+u*edge;c3=vColor[start4]*(1-u)+vColor[end4]*u;n3=vNorm[start4]*(1-u)+vNorm[end4]*u;}
	}
	}

	if(!has3)
	{
	edge=vPos[end5]-vPos[start5];
	u=-vPos[start5].w/edge.w;
	if(u>=0 && u<=1)
	{
	if(!has0){has0=true;edge0=5;p0=vPos[start5]+u*edge;c0=vColor[start5]*(1-u)+vColor[end5]*u;n0=vNorm[start5]*(1-u)+vNorm[end5]*u;}
	else if(!has1){has1=true;edge1=5;p1=vPos[start5]+u*edge;c1=vColor[start5]*(1-u)+vColor[end5]*u;n1=vNorm[start5]*(1-u)+vNorm[end5]*u;}
	else if(!has2){has2=true;edge2=5;p2=vPos[start5]+u*edge;c2=vColor[start5]*(1-u)+vColor[end5]*u;n2=vNorm[start5]*(1-u)+vNorm[end5]*u;}
	else if(!has3){has3=true;edge3=5;p3=vPos[start5]+u*edge;c3=vColor[start5]*(1-u)+vColor[end5]*u;n3=vNorm[start5]*(1-u)+vNorm[end5]*u;}
	}
	}



	if(!has2)
	{
		return;
	}
	light=vLight[0].xyz;

	p0.w=1;
	p1.w=1;
	p2.w=1;
	p3.w=1;

	vec3 faceNorm=cross((p0-p1).xyz,(p2-p1).xyz);
	vec4 surfNorm=vec4(faceNorm.x,faceNorm.y,faceNorm.z,0);
	float normDot=dot(cellNorm,surfNorm);

	if(flatShade==1)
	{
		n0=(surfNorm*normDot);
		n1=(surfNorm*normDot);
		n2=(surfNorm*normDot);
		n3=(surfNorm*normDot);
	}


	if(!has3)
	{
		if(edge0==0 && edge1==noSearch0){temp=edge1;edge1=edge2;edge2=temp;tempP=p1;p1=p2;p2=tempP;tempC=c1;c1=c2;c2=tempC;tempN=n1;n1=n2;n2=tempN;}
		if(edge0==1 && edge1==noSearch1){temp=edge1;edge1=edge2;edge2=temp;tempP=p1;p1=p2;p2=tempP;tempC=c1;c1=c2;c2=tempC;tempN=n1;n1=n2;n2=tempN;}
		if(edge0==2 && edge1==noSearch2){temp=edge1;edge1=edge2;edge2=temp;tempP=p1;p1=p2;p2=tempP;tempC=c1;c1=c2;c2=tempC;tempN=n1;n1=n2;n2=tempN;}
		if(edge0==3 && edge1==noSearch3){temp=edge1;edge1=edge2;edge2=temp;tempP=p1;p1=p2;p2=tempP;tempC=c1;c1=c2;c2=tempC;tempN=n1;n1=n2;n2=tempN;}
		if(edge0==4 && edge1==noSearch4){temp=edge1;edge1=edge2;edge2=temp;tempP=p1;p1=p2;p2=tempP;tempC=c1;c1=c2;c2=tempC;tempN=n1;n1=n2;n2=tempN;}
		if(edge0==5 && edge1==noSearch5){temp=edge1;edge1=edge2;edge2=temp;tempP=p1;p1=p2;p2=tempP;tempC=c1;c1=c2;c2=tempC;tempN=n1;n1=n2;n2=tempN;}


		if(normDot<0)//CHANGE PLACES!
		{
			tempP=p0;
			p0=p2;
			p2=tempP;

			tempC=c0;
			c0=c2;
			c2=tempC;

			tempN=n0;
			n0=n2;
			n2=tempN;
		}

		gl_Position=projectionMatrix*p0;
		color=c0;
		norm=normalize(n0.xyz);
		viewPos=p0;
		EmitVertex();

		gl_Position=projectionMatrix*p1;
		color=c1;
		norm=normalize(n1.xyz);
		viewPos=p1;
		EmitVertex();

		gl_Position=projectionMatrix*p2;
		color=c2;
		norm=normalize(n2.xyz);
		viewPos=p2;
		EmitVertex();

		EndPrimitive();
	}
	else
	{
		if(edge0==0 && edge1==noSearch0){temp=edge1;edge1=edge2;edge2=temp;tempP=p1;p1=p2;p2=tempP;tempC=c1;c1=c2;c2=tempC;tempN=n1;n1=n2;n2=tempN;}
		if(edge0==1 && edge1==noSearch1){temp=edge1;edge1=edge2;edge2=temp;tempP=p1;p1=p2;p2=tempP;tempC=c1;c1=c2;c2=tempC;tempN=n1;n1=n2;n2=tempN;}
		if(edge0==2 && edge1==noSearch2){temp=edge1;edge1=edge2;edge2=temp;tempP=p1;p1=p2;p2=tempP;tempC=c1;c1=c2;c2=tempC;tempN=n1;n1=n2;n2=tempN;}
		if(edge0==3 && edge1==noSearch3){temp=edge1;edge1=edge2;edge2=temp;tempP=p1;p1=p2;p2=tempP;tempC=c1;c1=c2;c2=tempC;tempN=n1;n1=n2;n2=tempN;}
		if(edge0==4 && edge1==noSearch4){temp=edge1;edge1=edge2;edge2=temp;tempP=p1;p1=p2;p2=tempP;tempC=c1;c1=c2;c2=tempC;tempN=n1;n1=n2;n2=tempN;}
		if(edge0==5 && edge1==noSearch5){temp=edge1;edge1=edge2;edge2=temp;tempP=p1;p1=p2;p2=tempP;tempC=c1;c1=c2;c2=tempC;tempN=n1;n1=n2;n2=tempN;}
		if(edge1==0 && edge2==noSearch0){temp=edge2;edge2=edge3;edge3=temp;tempP=p2;p2=p3;p3=tempP;tempC=c2;c2=c3;c3=tempC;tempN=n2;n2=n3;n3=tempN;}
		if(edge1==1 && edge2==noSearch1){temp=edge2;edge2=edge3;edge3=temp;tempP=p2;p2=p3;p3=tempP;tempC=c2;c2=c3;c3=tempC;tempN=n2;n2=n3;n3=tempN;}
		if(edge1==2 && edge2==noSearch2){temp=edge2;edge2=edge3;edge3=temp;tempP=p2;p2=p3;p3=tempP;tempC=c2;c2=c3;c3=tempC;tempN=n2;n2=n3;n3=tempN;}
		if(edge1==3 && edge2==noSearch3){temp=edge2;edge2=edge3;edge3=temp;tempP=p2;p2=p3;p3=tempP;tempC=c2;c2=c3;c3=tempC;tempN=n2;n2=n3;n3=tempN;}
		if(edge1==4 && edge2==noSearch4){temp=edge2;edge2=edge3;edge3=temp;tempP=p2;p2=p3;p3=tempP;tempC=c2;c2=c3;c3=tempC;tempN=n2;n2=n3;n3=tempN;}
		if(edge1==5 && edge2==noSearch5){temp=edge2;edge2=edge3;edge3=temp;tempP=p2;p2=p3;p3=tempP;tempC=c2;c2=c3;c3=tempC;tempN=n2;n2=n3;n3=tempN;}



		if(normDot<0)//CHANGE PLACES!
		{
			tempP=p0;
			p0=p3;
			p3=tempP;
			
			tempC=c0;
			c0=c3;
			c3=tempC;

			tempN=n0;
			n0=n3;
			n3=tempN;

			tempP=p1;
			p1=p2;
			p2=tempP;
			
			tempC=c1;
			c1=c2;
			c2=tempC;

			tempN=n1;
			n1=n2;
			n2=tempN;
		}

		gl_Position=projectionMatrix*p0;
		color=c0;
		norm=normalize(n0.xyz);
		viewPos=p0;
		EmitVertex();

		gl_Position=projectionMatrix*p1;
		color=c1;
		norm=normalize(n1.xyz);
		viewPos=p1;
		EmitVertex();

		gl_Position=projectionMatrix*p3;
		color=c3;
		norm=normalize(n3.xyz);
		viewPos=p3;
		EmitVertex();

		gl_Position=projectionMatrix*p2;
		color=c2;
		norm=normalize(n2.xyz);
		viewPos=p2;
		EmitVertex();

		EndPrimitive();
	}
}