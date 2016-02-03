#version 330
layout(triangles) in;
layout(triangle_strip, max_vertices=3) out;

uniform mat4 projectionMatrix;

in vec4 vColor[3];
in vec4 vPos[3];
in vec4 vLight[3];

out vec4 color;
out vec3 norm;
flat out vec3 light;

void main()
{
	light=vLight[0].xyz;
	norm=vLight[0].xyz;

	color=vColor[0];
	gl_Position=projectionMatrix*vPos[0];
	EmitVertex();

	color=vColor[1];
	gl_Position=projectionMatrix*vPos[1];
	EmitVertex();

	color=vColor[2];
	gl_Position=projectionMatrix*vPos[2];
	EmitVertex();

	EndPrimitive();
}