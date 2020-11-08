package com.example.se04arface;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;

import com.google.ar.core.AugmentedFace;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.core.Frame;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.ux.AugmentedFaceNode;

import java.util.Collection;

public class MainActivity extends AppCompatActivity {

    private ModelRenderable modelRenderable;
    private Texture texture;
    private boolean isAdded = false;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomArFragment customArFragment = (CustomArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);

        ModelRenderable
                .builder().setSource(this, R.raw.imperial_knight_helmet_3)
                .build()
                .thenAccept(modelRenderable1 -> {
                    modelRenderable = modelRenderable1;
                    modelRenderable.setShadowCaster(false);
                    modelRenderable.setShadowReceiver(false);
                });

        Texture
                .builder().setSource(this, R.drawable.at1_helmet_3_diff)
                .build()
                .thenAccept(texture1 -> this.texture = texture1);

        Texture
                .builder().setSource(this, R.drawable.at1_helmet_3_normal)
                .build()
                .thenAccept(texture1 -> this.texture = texture1);

        Texture
                .builder().setSource(this, R.drawable.at1_helmet_3_spec)
                .build()
                .thenAccept(texture1 -> this.texture = texture1);

        customArFragment.getArSceneView().getCameraStreamRenderPriority(Renderable.RENDER_PRIORITY_FIRST);

        customArFragment.getArSceneView().getScene().addOnUpdateListener(frameTime -> {

            if (modelRenderable == null || texture == null) return;

            Frame frame = customArFragment.getArSceneView().getArFrame();

            Collection<AugmentedFace> augmentedFaces = frame.getUpdatedTrackables(AugmentedFace.class);

            for (AugmentedFace augmentedFace : augmentedFaces) {

                if (isAdded) return;

                AugmentedFaceNode augmentedFaceNode = new AugmentedFaceNode(augmentedFace);
                augmentedFaceNode.setParent(customArFragment.getArSceneView().getScene());
                augmentedFaceNode.setFaceRegionsRenderable(modelRenderable);
                augmentedFaceNode.setFaceMeshTexture(texture);

                isAdded = true;
            }
        });
    }
}