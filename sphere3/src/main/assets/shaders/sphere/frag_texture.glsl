#version 300 es
precision mediump float;

const float ZERO=0.0;
const int numLights = 4;

//Is the light on?
uniform bool onState[numLights];

//Position of light
uniform vec4 lightPositionEyeSpace[numLights];

//The light color * specular material color (this will carry opaqueness typically)
uniform vec4 specLightMaterialColor[numLights];

//The light color * diff material color (this will be transparent typically as it adds nothing to determining opaqueness)
uniform vec4 diffuseLightMaterialColor[numLights];

//The ambient light color * material color (this will be transparent typically as it adds nothing to determining opaqueness)
uniform vec4 ambientLightMaterialColor;

// How shiny the material is.  This determines the exponent used in rendering.
uniform float shininess;

// How opaque the material.  Typically this, plus the specular highlighting will determine opaqueness.
uniform float materialAlpha;

//Texture color of object
uniform sampler2D materialColorSampler;

//Position of point being rendered in eye space
in vec4 positionEyeSpace;
in vec4 normalEyeSpace;
in vec2 texCoord;

layout(location = 0) out vec4 fragColor;

void main()
{
    //Base color of material
    vec4 sampledColor = texture(materialColorSampler,texCoord);
    //Color of fragment is the combination of all colors
	fragColor = sampledColor;
	gl_FragDepth = (positionEyeSpace.w+1.0)*0.5;
}

