#type VERTEX
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
	gl_Position = vec4(
	    positionEyeSpace.x * projectionScaleX,
	    positionEyeSpace.y * projectionScaleY,
	    //TODO: Must compute value here for clipping purposes.  Suggested option is z-1.  Anything on the front demi-hypersphere will be in clipping space for z and anything in the back will be invsible.
	    0, //Computed per fragment
    	-positionEyeSpace.z
    );
}