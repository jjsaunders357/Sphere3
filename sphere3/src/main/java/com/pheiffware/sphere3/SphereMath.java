package com.pheiffware.sphere3;

import com.pheiffware.lib.graphics.Matrix4;

/**
 * Created by Steve on 7/8/2017.
 */

public class SphereMath
{
    /**
     * Creates a rotation matrix which rotates in the zw-plane.  Rotation is from -w towards +z.
     *
     * @param angleInDegrees
     * @return
     */
    public static Matrix4 zwRotation(float angleInDegrees)
    {
        double angleInRadians = Math.toRadians(angleInDegrees);
        float cosAngle = (float) Math.cos(angleInRadians);
        float sinAngle = (float) Math.sin(angleInRadians);
        float[] m = new float[]
                {
                        1, 0, 0, 0,
                        0, 1, 0, 0,
                        0, 0, cosAngle, sinAngle,
                        0, 0, -sinAngle, cosAngle
                };
        return new Matrix4(m);
    }

    /**
     * Creates a rotation matrix which rotates in the xw-plane.  Rotation is from -w towards +x (+x towards +w).
     *
     * @param angleInDegrees
     * @return
     */
    public static Matrix4 xwRotation(float angleInDegrees)
    {
        double angleInRadians = Math.toRadians(angleInDegrees);
        float cosAngle = (float) Math.cos(angleInRadians);
        float sinAngle = (float) Math.sin(angleInRadians);
        float[] m = new float[]
                {
                        cosAngle, 0, 0, sinAngle,
                        0, 1, 0, 0,
                        0, 0, 1, 0,
                        -sinAngle, 0, 0, cosAngle
                };
        return new Matrix4(m);
    }
}
