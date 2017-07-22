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

    public static Matrix4 up(float angleInDegrees)
    {
        double angleInRadians = Math.toRadians(angleInDegrees);
        float cosAngle = (float) Math.cos(angleInRadians);
        float sinAngle = (float) Math.sin(angleInRadians);
        float[] m = new float[]
                {
                        1, 0, 0, 0,
                        0, cosAngle, 0, sinAngle,
                        0, 0, 1, 0,
                        0, -sinAngle, 0, cosAngle
                };
        return new Matrix4(m);
    }


    //TODO: Remove
//    public void setNormalTransformFromVertexTransform(Matrix4 vertexTransform)
//    {
//        //Extract scale^2 factors
//        float[] t4m = vertexTransform.m;
//        float xScaleSquared = t4m[0] * t4m[0] + t4m[1] * t4m[1] + t4m[2] * t4m[2] + t4m[3] * t4m[3];
//        float yScaleSquared = t4m[4] * t4m[4] + t4m[5] * t4m[5] + t4m[6] * t4m[6] + t4m[7] * t4m[7];
//        float zScaleSquared = t4m[8] * t4m[8] + t4m[9] * t4m[9] + t4m[10] * t4m[10] + t4m[11] * t4m[11];
//        float wScaleSquared = t4m[12] * t4m[12] + t4m[13] * t4m[13] + t4m[14] * t4m[14] + t4m[15] * t4m[15];
//
//        //Find inverse^2 scale factors
//        float invXScaleSq = 1 / xScaleSquared;
//        float invYScaleSq = 1 / yScaleSquared;
//        float invZScaleSq = 1 / zScaleSquared;
//        float invWScaleSq = 1 / wScaleSquared;
//
//        //Take matrix and inverse scale it once to get rotation matrix and inverse scale again to apply inverse
//        //of scale operation to the rotation matrix.
//        m[0] = t4m[0] * invXScaleSq;
//        m[1] = t4m[1] * invXScaleSq;
//        m[2] = t4m[2] * invXScaleSq;
//        m[3] = t4m[3] * invXScaleSq;
//
//        m[4] = t4m[4] * invYScaleSq;
//        m[5] = t4m[5] * invYScaleSq;
//        m[6] = t4m[6] * invYScaleSq;
//        m[7] = t4m[7] * invYScaleSq;
//
//        m[8] = t4m[8] * invZScaleSq;
//        m[9] = t4m[9] * invZScaleSq;
//        m[10] = t4m[10] * invZScaleSq;
//        m[11] = t4m[11] * invZScaleSq;
//
//        m[12] = t4m[12] * invWScaleSq;
//        m[13] = t4m[13] * invWScaleSq;
//        m[14] = t4m[14] * invWScaleSq;
//        m[15] = t4m[15] * invWScaleSq;
//    }
}
