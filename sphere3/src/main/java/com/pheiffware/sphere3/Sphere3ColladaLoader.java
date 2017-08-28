package com.pheiffware.sphere3;

import com.pheiffware.lib.AssetLoader;
import com.pheiffware.lib.geometry.collada.ColladaMaterial;
import com.pheiffware.lib.graphics.GraphicsException;
import com.pheiffware.lib.graphics.Matrix4;
import com.pheiffware.lib.graphics.Mesh;
import com.pheiffware.lib.graphics.managed.GLCache;
import com.pheiffware.lib.graphics.managed.Technique;
import com.pheiffware.lib.graphics.managed.engine.ColladaLoader;
import com.pheiffware.lib.graphics.managed.engine.ObjectManager;
import com.pheiffware.lib.graphics.managed.program.RenderProperty;
import com.pheiffware.lib.graphics.managed.program.RenderPropertyValue;
import com.pheiffware.lib.graphics.managed.texture.Texture2D;
import com.pheiffware.sphere3.sphereMath.Spherizer;

/**
 * Created by Steve on 7/8/2017.
 */

public class Sphere3ColladaLoader extends ColladaLoader
{
    private final Technique colorTechnique;
    private final Technique textureTechnique;
    private final Spherizer spherizer;

    public Sphere3ColladaLoader(ObjectManager objectManager,
                                GLCache glCache,
                                AssetLoader al,
                                String imageDirectory,
                                Technique colorTechnique,
                                Technique textureTechnique, float degreesPerLength) throws GraphicsException
    {
        super(objectManager, glCache, al, imageDirectory);
        setHomogenizePositions(true);
        setHomogenizeNormals(true);
        this.colorTechnique = colorTechnique;
        this.textureTechnique = textureTechnique;
        this.spherizer = new Spherizer(degreesPerLength);
    }

    @Override
    protected void addMesh(Mesh mesh, ColladaMaterial material, Matrix4 initialMatrix, String name)
    {
        Technique technique;
        RenderPropertyValue[] renderProperties;

        if (material.imageFileName == null)
        {
            technique = colorTechnique;
            renderProperties = new RenderPropertyValue[]
                    {
                            new RenderPropertyValue(RenderProperty.MODEL_MATRIX, initialMatrix),
                            new RenderPropertyValue(RenderProperty.MAT_COLOR, material.diffuseColor.comps),
                            new RenderPropertyValue(RenderProperty.SPEC_MAT_COLOR, material.specularColor.comps),
                            new RenderPropertyValue(RenderProperty.SHININESS, material.shininess)
                    };
        }
        else
        {
            technique = textureTechnique;
            renderProperties = new RenderPropertyValue[]
                    {
                            new RenderPropertyValue(RenderProperty.MODEL_MATRIX, initialMatrix),
                            new RenderPropertyValue(RenderProperty.MAT_COLOR_TEXTURE, glCache.getTexture(getTexturePath(material.imageFileName))),
                            new RenderPropertyValue(RenderProperty.SPEC_MAT_COLOR, material.specularColor.comps),
                            new RenderPropertyValue(RenderProperty.SHININESS, material.shininess)
                    };
        }
        spherizer.spherizeMesh(mesh);
        objectManager.addStaticMesh(mesh, technique, renderProperties);
    }

    @Override
    protected Texture2D loadTexture2D(String imagePath) throws GraphicsException
    {
        return glCache.buildImageTex(imagePath).build();
    }
}
