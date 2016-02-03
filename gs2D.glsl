#version 330
#define start0 0
#define start1 1
#define start2 2

#define end0 1
#define end1 2
#define end2 0


layout(triangles) in;
layout(line_strip, max_vertices=2) out;

uniform mat4 projectionMatrix;
uniform float xOffset;

in vec4 vColor[3];
in vec4 vPos[3];
in vec4 vLight[3];
in vec4 vNorm[3];

out vec4 color;
out vec3 norm;
flat out vec3 light;

void main()
{
	if(vPos[0].z>0 && vPos[1].z>0 && vPos[2].z>0)
	{
		return;
	}
	if(vPos[0].z<0 && vPos[1].z<0 && vPos[2].z<0)
	{
		return;
	}
	bool has0=false;
	bool has1=false;

	vec4 p0=vec4(0,0,0,0);
	vec4 p1=vec4(0,0,0,0);

	vec4 c0=vec4(0,0,0,0);
	vec4 c1=vec4(0,0,0,0);

	//find the first intersection

	vec4 edge=vec4(0,0,0,0);
	float u=0;

	if(!has1)
	{
	edge=vPos[end0]-vPos[start0];
	u=-vPos[start0].z/edge.z;
	if(u>=0 && u<=1)
	{
	if(!has0){has0=true;p0=vPos[start0]+u*edge;c0=vColor[start0]*(1-u)+vColor[end0]*u;}
	else if(!has1){has1=true;p1=vPos[start0]+u*edge;c1=vColor[start0]*(1-u)+vColor[end0]*u;}
	}
	}

	if(!has1)
	{
	edge=vPos[end1]-vPos[start1];
	u=-vPos[start1].z/edge.z;
	if(u>=0 && u<=1)
	{
	if(!has0){has0=true;p0=vPos[start1]+u*edge;c0=vColor[start1]*(1-u)+vColor[end1]*u;}
	else if(!has1){has1=true;p1=vPos[start1]+u*edge;c1=vColor[start1]*(1-u)+vColor[end1]*u;}
	}
	}

	if(!has1)
	{
	edge=vPos[end2]-vPos[start2];
	u=-vPos[start2].z/edge.z;
	if(u>=0 && u<=1)
	{
	if(!has0){has0=true;p0=vPos[start2]+u*edge;c0=vColor[start2]*(1-u)+vColor[end2]*u;}
	else if(!has1){has1=true;p1=vPos[start2]+u*edge;c1=vColor[start2]*(1-u)+vColor[end2]*u;}
	}
	}



	if(!has1)
	{
		return;
	}
	light=vLight[0].xyz;
	norm=vLight[0].xyz;

	p0.w=1;
	p1.w=1;
	p0.z=-5;
	p1.z=-5;
	p0.x+=xOffset;
	p1.x+=xOffset;
	p0.y+=2;
	p1.y+=2;


	gl_Position=projectionMatrix*p0;
	color=c0;
	EmitVertex();

	gl_Position=projectionMatrix*p1;
	color=c1;
	EmitVertex();

	EndPrimitive();
}