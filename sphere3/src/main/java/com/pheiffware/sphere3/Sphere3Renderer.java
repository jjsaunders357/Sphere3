package com.pheiffware.sphere3;

import android.opengl.GLES20;

import com.pheiffware.lib.AssetLoader;
import com.pheiffware.lib.and.graphics.AndGraphicsUtils;
import com.pheiffware.lib.and.gui.graphics.openGL.GameRenderer;
import com.pheiffware.lib.and.gui.graphics.openGL.SystemInfo;
import com.pheiffware.lib.and.input.TouchAnalyzer;
import com.pheiffware.lib.geometry.Transform2D;
import com.pheiffware.lib.graphics.GraphicsException;
import com.pheiffware.lib.graphics.Matrix4;
import com.pheiffware.lib.graphics.Projection;
import com.pheiffware.lib.graphics.managed.GLCache;
import com.pheiffware.lib.graphics.managed.Technique;
import com.pheiffware.lib.graphics.managed.engine.ObjectHandle;
import com.pheiffware.lib.graphics.managed.engine.ObjectManager;
import com.pheiffware.lib.graphics.managed.engine.renderers.SimpleRenderer;
import com.pheiffware.lib.graphics.managed.light.Lighting;
import com.pheiffware.lib.graphics.managed.program.GraphicsConfig;
import com.pheiffware.lib.graphics.managed.program.RenderProperty;
import com.pheiffware.lib.graphics.managed.techniques.Std3DTechnique;
import com.pheiffware.lib.utils.dom.XMLParseException;
import com.pheiffware.sphere3.sphereMath.SphereCamera;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Steve on 7/7/2017.
 */

public class Sphere3Renderer extends GameRenderer
{
    private SphereCamera camera;
    private Projection projection;
    private ObjectManager objectManager;
    private Sphere3ColladaLoader loader;
    private Lighting lighting;
    private SimpleRenderer simpleRenderer;
    private Technique colorTechnique;
    private Technique textureTechnique;
    private ObjectHandle cube;
    private List<ObjectHandle> objects;

    public Sphere3Renderer()
    {
        super(AndGraphicsUtils.GL_VERSION_30, AndGraphicsUtils.GL_VERSION_30, "shaders");
    }

    @Override
    protected void onSurfaceCreated(AssetLoader al, GLCache glCache, SystemInfo systemInfo) throws GraphicsException
    {
        objects = new ArrayList<>();
        camera = new SphereCamera();
        projection = new Projection(90f, 1f, 0.01f, 1.001f, false);
        camera.moveForward(-20.f);


        //BS Fill-in technique
        colorTechnique = glCache.buildTechnique(Std3DTechnique.class, GraphicsConfig.TEXTURED_MATERIAL, false);
        textureTechnique = glCache.buildTechnique(SphereTextureMaterialTechnique.class);

        lighting = new Lighting(new float[]{0.2f, 0.2f, 0.2f, 1.0f}, new float[]{-3, 3, 0, 1}, new float[]{1.0f, 1.0f, 1.0f, 1.0f});
        simpleRenderer = new SimpleRenderer(colorTechnique, textureTechnique);

        objectManager = new ObjectManager();

        //TODO 1.0 = 2/2: What is the correct length/angle conversion here?
        loader = new Sphere3ColladaLoader(objectManager, glCache, al, "images", colorTechnique, textureTechnique, (float) (1.05 * 36.0 / (Math.sqrt(3))));
        try
        {
            Map<String, ObjectHandle> primitives = loader.loadCollada("meshes/primitives.dae");
            cube = primitives.get("Cube");
            cube.setProperty(RenderProperty.MODEL_MATRIX, Matrix4.newIdentity());
            objectManager.packAndTransfer();

            objects.add(cube);
            Matrix4 cubeTransform = Matrix4.newIdentity();
            for (int i = 1; i < 10; i++)
            {
                cubeTransform = Matrix4.multiply(cubeTransform, SphereMath.zwRotation(36));
                ObjectHandle object = cube.copy();
                object.setProperty(RenderProperty.MODEL_MATRIX, cubeTransform);
                objects.add(object);
            }
        }
        catch (XMLParseException | IOException e)
        {
            throw new RuntimeException("Failure", e);
        }
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        //Must use front face given we look down the +z-axis in the neutral position (opposite of OpenGL standard).
        GLES20.glCullFace(GLES20.GL_BACK);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        simpleRenderer.add(objects);
    }

    @Override
    public void onDrawFrame() throws GraphicsException
    {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClearDepthf(1);
        GLES20.glViewport(0, 0, getSurfaceWidth(), getSurfaceHeight());
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        simpleRenderer.setConstantProperty(RenderProperty.PROJECTION_LINEAR_DEPTH, projection.getLinearDepth());
        simpleRenderer.setConstantProperty(RenderProperty.PROJECTION_MATRIX, projection.getProjectionMatrix());
        simpleRenderer.setConstantProperty(RenderProperty.VIEW_MATRIX, camera.getViewMatrix());
        simpleRenderer.setConstantProperty(RenderProperty.LIGHTING, lighting);
        simpleRenderer.setConstantProperty(RenderProperty.DEPTH_Z_CONST, 1.0f);
        simpleRenderer.setConstantProperty(RenderProperty.DEPTH_Z_FACTOR, 1.0f);
        simpleRenderer.applyConstantProperties();
        simpleRenderer.render();
        GLES20.glFinish();
    }

    @Override
    public void onSurfaceResize(int width, int height)
    {
        super.onSurfaceResize(width, height);
        projection.setAspect(width / (float) height);
    }

    @Override
    public void onTouchTransformEvent(TouchAnalyzer.TouchTransformEvent event)
    {
        int numPointers = event.numPointers;
        Transform2D transform = event.transform;
        if (numPointers > 2)
        {
//            //Geometric average of x and y scale factors
//            float scaleFactor = (float) Math.sqrt(transform.scale.x * transform.scale.y);
//            camera.zoom(scaleFactor);

            camera.upStrafeInput((float) transform.translation.x, (float) -transform.translation.y, 0.3f);
            camera.roll((float) (180 * transform.rotation / Math.PI));
        }
        else if (numPointers > 1)
        {
            camera.roll((float) (180 * transform.rotation / Math.PI));
            camera.rotateInput((float) transform.translation.x, (float) -transform.translation.y, 1.0f);
        }
        else
        {
            camera.forwardStrafeInput((float) transform.translation.x, (float) -transform.translation.y, 0.3f);
        }
    }

}
