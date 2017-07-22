package com.pheiffware.sphere3.sphereMath;

import com.pheiffware.lib.geometry.Vec3D;
import com.pheiffware.lib.graphics.Matrix4;
import com.pheiffware.lib.graphics.managed.techniques.ProjectionLinearDepth;
import com.pheiffware.sphere3.SphereMath;

/**
 * Created by Steve on 7/11/2017.
 */

public class SphereCamera
{
    //Conversion factor from a distance on the screen in terms of dp to degrees.  degrees = SCREEN_DP_TO_DEGREES * dp
    private static final float SCREEN_DP_TO_DEGREES = 0.1f;

    private float FOV;
    private float aspect;
    private boolean flipVertical;

    //The linear depth projection representing the lens
    private ProjectionLinearDepth projectionLinearDepth;

    //Represent the composition of invOrientation * inverseTranslation
    private Matrix4 cameraMatrix;

    public SphereCamera(float FOV, float aspect, boolean flipVertical)
    {
        //Standing at [0,0,0,-1].  Looking in +z direction, with +y axis straight up and +x axis left
        cameraMatrix = Matrix4.newIdentity();
        this.FOV = FOV;
        this.aspect = aspect;
        this.flipVertical = flipVertical;
        setLens(FOV, aspect, flipVertical);
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
        projectionLinearDepth = new ProjectionLinearDepth(FOV, aspect, 0);
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
    public void forwardStrafeInputVector(float x, float y, float degreesPerLength)
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
    public void forwardStrafeInputVector(float x, float y)
    {
        forwardStrafeInputVector(x, y, SCREEN_DP_TO_DEGREES);
    }

    /**
     * Move the camera in the direction it is facing by the given number of degrees.
     *
     * @param degrees
     */
    public void moveForward(float degrees)
    {
        cameraMatrix.multiplyByLHS(SphereMath.zwRotation(-degrees));
    }

    public void moveScreenInputVector(float x, float y, float degreesPerLength)
    {
        float mag = (float) Math.sqrt(x * x + y * y);
        if (mag != 0)
        {
            float moveDegrees = degreesPerLength * mag;
            float xyAngle = (float) Math.toDegrees(Math.atan2(y, x));
            Matrix4 xyRot = Matrix4.newRotate(xyAngle, 0, 0, 1);
            Matrix4 invXYRot = Matrix4.newRotate(-xyAngle, 0, 0, 1);
            cameraMatrix.multiplyByLHS(xyRot);
            cameraMatrix.multiplyByLHS(SphereMath.xwRotation(-moveDegrees));
            cameraMatrix.multiplyByLHS(invXYRot);
        }
    }

    /**
     * Used to turn screen input (such as a mouse or touch/drag) into a camera rotation. Given the direction the camera is looking and an x,y vector, in screen space, rotate in the
     * plane described by the vectors (x,y,0) and (0,0,z). Rotate by an amount proportional to length.
     * <p/>
     * If x,y magnitude is 0, then nothing happens.
     *
     * @param x                       x screen movement
     * @param y                       y screen movement
     * @param cameraRotationPerLength the vector's length is scaled by this factor to convert it to degrees
     */
    public void rotateScreenInputVector(float x, float y, float cameraRotationPerLength)
    {
        float mag = (float) Math.sqrt(x * x + y * y);
        if (mag != 0)
        {
            Vec3D inScreenVec = new Vec3D(x, y, 0);
            Vec3D perpScreenVec = new Vec3D(0, 0, -1);
            Vec3D rotationAxis = Vec3D.cross(perpScreenVec, inScreenVec);
            float angleDegrees = cameraRotationPerLength * mag;
            rotateScreen(angleDegrees, rotationAxis);
        }
    }

    public void rotateScreenInputVector(float x, float y)
    {
        rotateScreenInputVector(x, y, SCREEN_DP_TO_DEGREES);
    }

    /**
     * Rotate the camera relative to "screen" coordinate system. x and y are in the screen and z is perpendicular to the screen.
     *
     * @param angleDegrees
     * @param rotationAxis
     */
    public final void rotateScreen(float angleDegrees, Vec3D rotationAxis)
    {
        rotateScreen(angleDegrees, (float) rotationAxis.x, (float) rotationAxis.y, (float) rotationAxis.z);
    }

    /**
     * Rotate the camera around the given "screen" axis by the specified amount. x and y are in the screen and z is perpendicular to the screen.
     *
     * @param angleDegrees
     * @param x            left/right (+/-)
     * @param y            up/down (+/-)
     * @param z
     */
    public void rotateScreen(float angleDegrees, float x, float y, float z)
    {
        cameraMatrix.multiplyByLHS(Matrix4.newRotate(-angleDegrees, x, y, z));
    }

    public void roll(float angleDegrees)
    {
        rotateScreen(angleDegrees, 0, 0, 1);
    }


    /**
     * Change the lens characteristics of the camera such as FOV
     *
     * @param FOV          field of view
     * @param aspect       width/height ratio
     * @param flipVertical should the projection be flipped?  Useful for rending to textures.
     */
    private void setLens(float FOV, float aspect, boolean flipVertical)
    {
        this.FOV = FOV;
        this.aspect = aspect;
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

    public ProjectionLinearDepth getProjection()
    {
        return projectionLinearDepth;
    }

    public Matrix4 getViewMatrix()
    {
        return cameraMatrix;
    }


}
