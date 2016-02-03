#version 330
layout (location=0) in vec4 vertexIn;
layout (location=1) in vec4 colorIn;
layout (location=2) in vec4 normIn;
layout (location=3) in mat4 model;
layout (location=7) in vec4 modelPos;
uniform mat4 viewMatrix;
uniform vec4 eyePos;

out vec4 vColor;
out vec4 vPos;
out vec4 vLight;
out vec4 vNorm;

void main()
{
	vPos=viewMatrix*((model*vertexIn)+modelPos-eyePos);
	vNorm=vec4(0,0,0,0);
	vLight=vec4(1,0,0,0);
	vColor=colorIn;
}