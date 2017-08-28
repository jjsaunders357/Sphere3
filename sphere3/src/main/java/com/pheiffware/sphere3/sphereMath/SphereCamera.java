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
        rotateInput(xInput, yInput, Axis.X, Axis.negZ, Axis.W, degreesPerLength);
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

    public void look()
    {
        //Implement look methods.
        //For shadow mapping, set orientation, at position 0, identically to a Euclidean camera then perform an inverse "translation" to the light's position.
        // Will have to set up an inverse "translation" matrix based on light's position.

//        Euclidean equivalent
//        lightCamera.setOrientation(0, 0, 1.0f, 0.0f, -1.0f, 0.0f);
//        lightCamera.setPosition(renderPosition[0], renderPosition[1], renderPosition[2]);

    }
}
