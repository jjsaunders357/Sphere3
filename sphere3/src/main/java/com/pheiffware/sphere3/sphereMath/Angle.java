package com.pheiffware.sphere3.sphereMath;

/**
 * Created by Steve on 7/8/2017.
 */

public class Angle
{
    public static Angle newDegrees(float degrees)
    {
        float radians = (float) Math.toRadians(degrees);
        return new Angle(radians);
    }

    public float cos;
    public float sin;

    private Angle(float radians)
    {
        this.cos = (float) Math.cos(radians);
        this.sin = (float) Math.sin(radians);
    }

    public void setDegrees(float degrees)
    {
        float radians = (float) Math.toRadians(degrees);
        this.cos = (float) Math.cos(radians);
        this.sin = (float) Math.sin(radians);
    }
}
