package com.pheiffware.sphere3;

import com.pheiffware.lib.ParseException;
import com.pheiffware.lib.graphics.GraphicsException;
import com.pheiffware.lib.graphics.managed.program.RenderProperty;
import com.pheiffware.lib.graphics.managed.program.UniformName;
import com.pheiffware.lib.graphics.managed.program.shader.ShaderBuilder;
import com.pheiffware.lib.graphics.managed.texture.Texture;

import java.io.IOException;
import java.util.Map;

/**
 * Shades mesh with a textured color and with given lights' settings.  Handles, ambient, diffuse and specular lighting.
 * Created by Steve on 4/23/2016.
 */
public class SphereTextureMaterialTechnique extends SphereTechnique3D
{
    public SphereTextureMaterialTechnique(ShaderBuilder shaderBuilder, Map<String, Object> localConfig) throws GraphicsException, IOException, ParseException
    {
        super(shaderBuilder, localConfig, new RenderProperty[]{
                RenderProperty.PROJECTION_LINEAR_DEPTH,
                RenderProperty.VIEW_MATRIX,
                RenderProperty.MODEL_MATRIX,
                RenderProperty.LIGHTING,
                RenderProperty.MAT_COLOR_TEXTURE,
                RenderProperty.SPEC_MAT_COLOR,
                RenderProperty.SHININESS
        }, "sphere/vert_texture.glsl", "sphere/frag_texture.glsl");
    }

    public void applyConstantPropertiesImplement()
    {
        setProjection();
        //setLightingConstants();
    }

    @Override
    public void applyInstanceProperties()
    {
        setViewModel();
        Texture texture = (Texture) getPropertyValue(RenderProperty.MAT_COLOR_TEXTURE);
        setUniformValue(UniformName.MATERIAL_SAMPLER, texture.autoBind());

//        setSpecLightingColor();
//
//        Lighting lighting = (Lighting) getPropertyValue(RenderProperty.LIGHTING);
//
//        setUniformValue(UniformName.AMBIENT_LIGHT_COLOR, lighting.getAmbientLightColor());
//        setUniformValue(UniformName.LIGHT_COLOR, lighting.getColors());
//        setUniformValue(UniformName.SHININESS, getPropertyValue(RenderProperty.SHININESS));
    }

}
