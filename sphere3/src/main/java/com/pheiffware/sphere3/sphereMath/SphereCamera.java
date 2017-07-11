package com.pheiffware.sphere3.sphereMath;

import com.pheiffware.lib.graphics.Matrix4;
import com.pheiffware.lib.graphics.managed.techniques.ProjectionLinearDepth;

/**
 * Created by Steve on 7/11/2017.
 */

public class SphereCamera
{
    //Conversion factor from a distance on the screen in terms of dp to degrees.  degrees = SCREEN_DP_TO_DEGREES * dp
    private static final float SCREEN_DP_TO_DEGREES = 0.1f;

    private float FOV;
    private float aspect;
    private float maxDepth;
    private boolean flipVertical;

    //The linear depth projection representing the lens
    private ProjectionLinearDepth projectionLinearDepth;

    //Represent the composition of invOrientation * inverseTranslation
    private Matrix4 cameraMatrix;

    public SphereCamera(float FOV, float aspect, float maxDepth, boolean flipVertical)
    {
        //Standing at [0,0,0,-1].  Looking in +z direction, with +y axis straight up and +x axis left
        cameraMatrix = Matrix4.newIdentity();
        this.FOV = FOV;
        this.aspect = aspect;
        this.maxDepth = maxDepth;
        this.flipVertical = flipVertical;
        setLens(FOV, aspect, maxDepth, flipVertical);
    }

    /**
     * Resets position to 0,0,0
     * Reset orientation back to looking down z-axis with positive y axis straight up.
     */
    public void reset()
    {
        cameraMatrix.setIdentity();
    }


    private void updateProjection()
    {
        projectionLinearDepth = new ProjectionLinearDepth(FOV, aspect, maxDepth);
    }

    /**
     * Used to turn screen input (such as a mouse or touch/drag) into a camera rotation. Given the direction the camera is looking and an x,y vector, in screen space,
     * move in the forward/sideways plane (y is forward, x is sideways).
     * <p>
     * This is described by a rotation in the zx --> w plane.
     * <p/>
     * If x,y magnitude is 0, then nothing happens.
     *
     * @param x                sideways movement
     * @param y                forward movement
     * @param degreesPerLength conversion factor between x,y length and angle to rotate
     */
    public void moveInputVector(float x, float y, float degreesPerLength)
    {
        float mag = (float) Math.sqrt(x * x + y * y);
        if (mag != 0)
        {
            float moveDegrees = degreesPerLength * mag;
            float xzAngle = (float) Math.toDegrees(Math.atan2(x, y));
            Matrix4 xzRot = Matrix4.newRotate(xzAngle, 0, 1, 0);
            Matrix4 invXZRot = Matrix4.newRotate(-xzAngle, 0, 1, 0);
            cameraMatrix.multiplyByLHS(xzRot);
            moveForward(moveDegrees);
            cameraMatrix.multiplyByLHS(invXZRot);
        }
    }

    /**
     * Used to turn screen input (such as a mouse or touch/drag) into a camera rotation. Given the direction the camera is looking and an x,y vector, in screen space,
     * move in the forward/sideways plane (y is forward, x is sideways).
     * <p>
     * This is described by a rotation in the zx --> w plane.
     * <p/>
     * If x,y magnitude is 0, then nothing happens.
     *
     * @param x sideways movement (assumed to be in units of dp)
     * @param y forward movement (assumed to be in units of dp)
     */
    public void moveScreenInputVector(float x, float y)
    {
        moveInputVector(x, y, SCREEN_DP_TO_DEGREES);
    }

    public void moveForward(float degrees)
    {
        double radians = Math.toRadians(degrees);
        float cosAngle = (float) Math.cos(radians);
        float sinAngle = (float) Math.sin(radians);
        float[] m = new float[]
                {
                        1, 0, 0, 0,
                        0, 1, 0, 0,
                        0, 0, cosAngle, -sinAngle,
                        0, 0, sinAngle, cosAngle
                };
        cameraMatrix.multiplyByLHS(new Matrix4(m));
    }

    public void moveRight(float angleInDegrees)
    {
        double angleInRadians = Math.toRadians(angleInDegrees);
        float cosAngle = (float) Math.cos(angleInRadians);
        float sinAngle = (float) Math.sin(angleInRadians);
        float[] m = new float[]
                {
                        cosAngle, 0, 0, -sinAngle,
                        0, 1, 0, 0,
                        0, 0, 1, 0,
                        sinAngle, 0, 0, cosAngle
                };
        cameraMatrix.multiplyByLHS(new Matrix4(m));
    }

    /**
     * Change the lens characteristics of the camera such as FOV
     *
     * @param FOV          field of view
     * @param aspect       width/height ratio
     * @param maxDepth     maximum visible depth.  Depth defined by atan2(z,-w)
     * @param flipVertical should the projection be flipped?  Useful for rending to textures.
     */
    private void setLens(float FOV, float aspect, float maxDepth, boolean flipVertical)
    {
        this.FOV = FOV;
        this.aspect = aspect;
        this.maxDepth = maxDepth;
        this.flipVertical = flipVertical;
        updateProjection();
    }

    public void setFOV(float FOV)
    {
        this.FOV = FOV;
        updateProjection();
    }

    public void setAspect(float aspect)
    {
        this.aspect = aspect;
        updateProjection();
    }

    public ProjectionLinearDepth getProjectionLinearDepth()
    {
        return projectionLinearDepth;
    }

    public Matrix4 getViewMatrix()
    {
        return cameraMatrix;
    }

}
