package com.pheiffware.sphere3.sphereMath;

import com.pheiffware.lib.geometry.Axis;
import com.pheiffware.lib.graphics.Camera;
import com.pheiffware.sphere3.SphereMath;

/**
 * Created by Steve on 7/11/2017.
 */

public class SphereCamera extends Camera
{
    /**
     * Move the camera in the direction it is facing by the given number of degrees.
     *
     * @param degrees
     */
    public void moveForward(float degrees)
    {
        viewMatrix.multiplyBy(SphereMath.zwRotation(-degrees));
    }

    /**
     * Turns an x,y input vector into forward/backward/left/right motion.
     * <p>
     *
     * @param xInput           sideways movement
     * @param yInput           forward movement
     * @param degreesPerLength conversion factor between xInput,y length and angle to rotate
     */
    @Override
    public void forwardStrafeInput(float xInput, float yInput, float degreesPerLength)
    {
        rotateInput(xInput, yInput, Axis.X, Axis.Z, Axis.W, degreesPerLength);
    }

    /**
     * Turns an x,y input vector into up/down/left/right motion.
     * <p>
     *
     * @param xInput           sideways movement
     * @param yInput           up/down movement
     * @param degreesPerLength conversion factor between xInput,y length and angle to rotate
     */
    @Override
    public void upStrafeInput(float xInput, float yInput, float degreesPerLength)
    {
        rotateInput(xInput, yInput, Axis.X, Axis.Y, Axis.W, degreesPerLength);
    }
}
