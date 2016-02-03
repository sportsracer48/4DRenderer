#version 330

in vec4 color;
in vec3 norm;
in vec4 viewPos;
flat in vec3 light;

out vec4 fragColor;

void main()
{
	float ambientFactor=.3f;
	float diffuseFactor=dot(norm,light);
	vec4 lightColor=vec4(.1f);
    if(diffuseFactor<0)
    {
    	diffuseFactor=0;
    }
  	fragColor=color*(ambientFactor+diffuseFactor);
}