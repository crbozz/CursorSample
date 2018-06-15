package org.sidia.cursortest;

import android.util.Log;

import org.gearvrf.GVRAndroidResource;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRMain;
import org.gearvrf.GVRMaterial;
import org.gearvrf.GVRMeshCollider;
import org.gearvrf.GVRRenderData;
import org.gearvrf.GVRScene;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.physics.GVRFixedConstraint;
import org.gearvrf.physics.GVRRigidBody;
import org.gearvrf.physics.GVRWorld;
import org.gearvrf.scene_objects.GVRCubeSceneObject;
import org.gearvrf.scene_objects.GVRSphereSceneObject;

public class Main extends GVRMain {

    private static final String TAG = Main.class.getSimpleName();

    private GVRSceneObject cursor;

    @Override
    public void onInit(GVRContext gvrContext) {
        cursor = new GVRSceneObject(gvrContext, gvrContext.createQuad(1f, 1f),
                gvrContext.getAssetLoader().loadTexture(new GVRAndroidResource(gvrContext, R.raw.cursor)));
        cursor.getRenderData().setDepthTest(false);
        cursor.getRenderData().setRenderingOrder(GVRRenderData.GVRRenderingOrder.OVERLAY);
        cursor.setName("Cursor_Object");

        createObjects(gvrContext);
    }

    private GVRMaterial createSolid(GVRContext gvrContext, float r, float g, float b, float a) {
        GVRMaterial material = new GVRMaterial(gvrContext, GVRMaterial.GVRShaderType.Phong.ID);
        material.setDiffuseColor(r, g, b, a);
        return material;
    }

    private void createObjects(GVRContext gvrContext) {
        GVRScene scene = gvrContext.getMainScene();

        GVRSceneObject ball = new GVRSphereSceneObject(gvrContext, true);
        ball.getTransform().setPosition(-5f, 5f, -10f);
        ball.getRenderData().setMaterial(createSolid(gvrContext, 0f, 1f, 0f, 1f));
        ball.setName("Ball");
        ball.attachComponent(new GVRMeshCollider(gvrContext, false));
        scene.addSceneObject(ball);
        ball.attachComponent(new GVRRigidBody(gvrContext, 1f));

        GVRSceneObject box = new GVRCubeSceneObject(gvrContext, true);
        box.getTransform().setPosition(5f, 5f, -10f);
        box.getRenderData().setMaterial(createSolid(gvrContext, 0f, 0f, 1f, 1f));
        box.setName("Box");
        box.attachComponent(new GVRMeshCollider(gvrContext, true));
        scene.addSceneObject(box);
        box.attachComponent(new GVRRigidBody(gvrContext, 1f));

        GVRSceneObject floor = new GVRSceneObject(gvrContext, 100f, 100f);
        floor.getTransform().setPosition(0f, -10f, 0f);
        floor.getTransform().setRotationByAxis(-90f, 1f, 0f, 0f);
        floor.getRenderData().setMaterial(createSolid(gvrContext, 0.7f, 0.3f, 0f, 1f));
        floor.setName("Plane");
        floor.attachComponent(new GVRMeshCollider(gvrContext, floor.getRenderData().getMesh()));
        scene.addSceneObject(floor);
        floor.attachComponent(new GVRRigidBody(gvrContext, 0f));

        GVRWorld world = new GVRWorld(gvrContext);
        world.setGravity(0f, -10f, 0f);
        scene.getRoot().attachComponent(world);

        GVRRigidBody bodyB = (GVRRigidBody)box.getComponent(GVRRigidBody.getComponentType());
        GVRFixedConstraint fixed = new GVRFixedConstraint(gvrContext, bodyB);
        ball.attachComponent(fixed);

        world.setDraggingCursor(cursor);
        world.enableDragging();
    }
}
