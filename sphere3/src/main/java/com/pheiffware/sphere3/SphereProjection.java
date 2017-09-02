package com.pheiffware.sphere3;

import com.pheiffware.lib.graphics.projection.FieldOfViewProjection;

/**
 * Created by Steve on 9/2/2017.
 */

public class SphereProjection
{
    private float verticalFOV;
    private float aspect;
    private float xScale;
    private float yScale;

    public SphereProjection(float verticalFOV)
    {
        this(verticalFOV, 1);
    }

    public SphereProjection(float verticalFOV, float aspect)
    {
        this.verticalFOV = verticalFOV;
        this.aspect = aspect;
        updateProjection();
    }

    public final void setVerticalFOV(float verticalFOV)
    {
        this.verticalFOV = verticalFOV;
        updateProjection();
    }

    public final void setAspect(float aspect)
    {
        this.aspect = aspect;
        updateProjection();
    }

    private void updateProjection()
    {
        yScale = FieldOfViewProjection.scaleFromFOV(verticalFOV);
        xScale = aspect * yScale;
    }

    public final float getXScale()
    {
        return xScale;
    }

    public final float getYScale()
    {
        return yScale;
    }
}
