#version 330
in vec4 vertexIn;
in vec4 colorIn;
in vec4 normIn;
uniform mat4 model;
uniform vec4 modelPos;
uniform mat4 viewMatrix;
uniform mat4 normalMatrix;
uniform vec4 eyePos;

out vec4 vColor;
out vec4 vPos;
out vec4 vLight;
out vec4 vNorm;

void main()
{
	vPos=viewMatrix*((model*vertexIn)+modelPos-eyePos);
	vNorm=normalMatrix*normIn;
	vLight=normalize(viewMatrix*vec4(1.1,-1.2,1,.9));
	vColor=colorIn;
}