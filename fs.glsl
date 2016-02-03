#version 330

in vec4 color;
in vec3 norm;
flat in vec3 light;

out vec4 fragColor;

void main()
{
	vec4 lightColor=vec4(1,1,1,1);
	float amb=.3f;
	
	vec4 ambientColor = lightColor*amb;
	
	float diffuseFactor=dot(norm,light);
	
	vec4 diffuseColor = vec4(0, 0, 0, 0);
    
    if(diffuseFactor>0)
    {
    	diffuseColor=lightColor*diffuseFactor;
    }
  	fragColor=color*(ambientColor+diffuseColor);
}