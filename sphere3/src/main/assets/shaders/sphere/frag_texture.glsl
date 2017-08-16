#type FRAGMENT
#version 300 es
precision mediump float;

//The diffuse material color sampler
uniform mediump sampler2D diffuseMaterialColorSampler;

//Position of point being rendered in eye space
in vec4 positionEyeSpace;
in vec4 normalEyeSpace;
in vec2 texCoord;

layout(location = 0) out vec4 fragColor;

void main()
{
    //Base color of material
    vec4 sampledColor = texture(diffuseMaterialColorSampler,texCoord);
    //Color of fragment is the combination of all colors
	fragColor = sampledColor;
	gl_FragDepth = (positionEyeSpace.w+1.0)*0.5;
}

