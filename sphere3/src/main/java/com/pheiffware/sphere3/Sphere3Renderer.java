package com.pheiffware.sphere3;

import android.opengl.GLES20;

import com.pheiffware.lib.AssetLoader;
import com.pheiffware.lib.and.graphics.AndGraphicsUtils;
import com.pheiffware.lib.and.gui.graphics.openGL.GameRenderer;
import com.pheiffware.lib.and.gui.graphics.openGL.SystemInfo;
import com.pheiffware.lib.and.input.TouchAnalyzer;
import com.pheiffware.lib.geometry.Transform2D;
import com.pheiffware.lib.graphics.Camera;
import com.pheiffware.lib.graphics.GraphicsException;
import com.pheiffware.lib.graphics.Matrix4;
import com.pheiffware.lib.graphics.managed.GLCache;
import com.pheiffware.lib.graphics.managed.engine.ObjectHandle;
import com.pheiffware.lib.graphics.managed.engine.ObjectManager;
import com.pheiffware.lib.graphics.managed.engine.renderers.SimpleRenderer;
import com.pheiffware.lib.graphics.managed.light.Lighting;
import com.pheiffware.lib.graphics.managed.program.RenderProperty;
import com.pheiffware.lib.graphics.managed.program.Technique;
import com.pheiffware.lib.graphics.managed.techniques.ColorMaterialTechnique;
import com.pheiffware.lib.utils.dom.XMLParseException;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Steve on 7/7/2017.
 */

public class Sphere3Renderer extends GameRenderer
{
    private static final double screenDragToCameraTranslation = 0.01f;
    private Camera camera;
    private ObjectManager objectManager;
    private Sphere3ColladaLoader loader;
    private Lighting lighting;
    private SimpleRenderer simpleRenderer;
    private Technique colorTechnique;
    private Technique textureTechnique;
    private ObjectHandle cube;

    public Sphere3Renderer()
    {
        super(AndGraphicsUtils.GL_VERSION_30, AndGraphicsUtils.GL_VERSION_30);
    }

    @Override
    protected void onSurfaceCreated(AssetLoader al, GLCache glCache, SystemInfo systemInfo) throws GraphicsException
    {
        camera = new Camera(90f, 1f, 0.1f, 100f, false);
        camera.setPosition(0f, 0f, 4f);
        colorTechnique = new ColorMaterialTechnique(al);
        //textureTechnique = new TextureMaterialTechnique(al);
        textureTechnique = new SphereTextureMaterialTechnique(al);

        lighting = new Lighting(new float[]{0.2f, 0.2f, 0.2f, 1.0f}, new float[]{-3, 3, 0, 1}, new float[]{1.0f, 1.0f, 1.0f, 1.0f});
        simpleRenderer = new SimpleRenderer();

        objectManager = new ObjectManager();
        //TODO: Make techniques centralized so we don't have to pass arguments.
        loader = new Sphere3ColladaLoader(objectManager, glCache, al, "images", colorTechnique, textureTechnique, 25.0f);
        try
        {
            Map<String, ObjectHandle> primitives = loader.loadCollada("meshes/primitives.dae");
            cube = primitives.get("Cube");
            cube.setProperty(RenderProperty.MODEL_MATRIX, Matrix4.newIdentity());
            objectManager.packAndTransfer();
        }
        catch (XMLParseException | IOException e)
        {
            throw new RuntimeException("Failure", e);
        }
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glCullFace(GLES20.GL_BACK);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
    }

    @Override
    public void onDrawFrame() throws GraphicsException
    {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClearDepthf(1);
        GLES20.glViewport(0, 0, getSurfaceWidth(), getSurfaceHeight());
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        colorTechnique.setProperty(RenderProperty.PROJECTION_LINEAR_DEPTH, camera.getProjectionLinearDepth());
        colorTechnique.setProperty(RenderProperty.VIEW_MATRIX, camera.getViewMatrix());
        colorTechnique.setProperty(RenderProperty.LIGHTING, lighting);
        colorTechnique.applyConstantProperties();

        textureTechnique.setProperty(RenderProperty.PROJECTION_LINEAR_DEPTH, camera.getProjectionLinearDepth());
        textureTechnique.setProperty(RenderProperty.VIEW_MATRIX, camera.getViewMatrix());
        textureTechnique.setProperty(RenderProperty.LIGHTING, lighting);
        textureTechnique.applyConstantProperties();

        simpleRenderer.add(cube);
        simpleRenderer.render();
        GLES20.glFinish();
    }

    @Override
    public void onSurfaceResize(int width, int height)
    {
        super.onSurfaceResize(width, height);
        camera.setAspect(width / (float) height);
    }

    @Override
    public void onTouchTransformEvent(TouchAnalyzer.TouchTransformEvent event)
    {
        int numPointers = event.numPointers;
        Transform2D transform = event.transform;
        if (numPointers > 2)
        {
            //Geometric average of x and y scale factors
            float scaleFactor = (float) Math.sqrt(transform.scale.x * transform.scale.y);
            camera.zoom(scaleFactor);
        }
        else if (numPointers > 1)
        {
            camera.roll((float) (180 * transform.rotation / Math.PI));
            camera.rotateScreenInputVector((float) transform.translation.x, (float) -transform.translation.y);
        }
        else
        {
            float cameraX = (float) (transform.translation.x * screenDragToCameraTranslation);
            float cameraZ = (float) (transform.translation.y * screenDragToCameraTranslation);
            camera.translateScreen(cameraX, 0, cameraZ);
        }
    }

}
