#version 330
layout (location = 0) in vec4 vertexIn;
layout (location = 1) in vec4 colorIn;
layout (location = 2) in vec4 normIn;
layout (location = 3) in vec4 model1;
layout (location = 4) in vec4 model2;
layout (location = 5) in vec4 model3;
layout (location = 6) in vec4 model4;
layout (location = 7) in vec4 modelPos;



uniform mat4 viewMatrix;
uniform vec4 eyePos;

out vec4 vColor;
out vec4 vPos;
out vec4 vLight;
out vec4 vNorm;

void main()
{
	mat4 model=mat4(model1,model2,model3,model4);
	mat4 normalMatrix=transpose(inverse(viewMatrix*model));
	vPos=viewMatrix*((model*vertexIn)+modelPos-eyePos);
	vNorm=normalMatrix*normIn;
	vLight=normalize(viewMatrix*vec4(1.1,-1.2,1,.9));
	vColor=colorIn;
}