#version 300 es
precision highp float;

//Transforms vertices to eye space
uniform mat4 viewModelMatrix;

//Linear depth projection inputs
uniform float projectionScaleX;
uniform float projectionScaleY;

in vec4 vertexPosition4;
in vec4 vertexNormal4;
in vec2 vertexTexCoord;
out vec4 positionEyeSpace;
out vec4 normalEyeSpace;
out vec2 texCoord;

void main()
{
	texCoord = vertexTexCoord;
	//Normal can be transformed with same matrix assuming its all rotations
	normalEyeSpace = viewModelMatrix * vertexNormal4;
	positionEyeSpace = viewModelMatrix * vertexPosition4;
    //TODO: Optimize
	gl_Position = vec4(
	    positionEyeSpace.x * projectionScaleX,
	    positionEyeSpace.y * projectionScaleY,
	    //Z - After division by w, z will LINEARLY map from [-1,1].  This essentially just takes the w value, which already ranges from [-1,1] and is quick/dirty measure of depth.
	    positionEyeSpace.z * positionEyeSpace.w,
    	positionEyeSpace.z
    );
}