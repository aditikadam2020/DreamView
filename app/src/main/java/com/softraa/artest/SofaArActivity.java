package com.softraa.artest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.Objects;

public class SofaArActivity extends AppCompatActivity {
    CardView cd;
    private  SofaModel sofaModel ;
    private ArFragment arCam;
    private int tapCount = 0;


    public static boolean checkSystemSupport(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String openGlVersion = ((ActivityManager) Objects.requireNonNull(activity.getSystemService(Context.ACTIVITY_SERVICE))).getDeviceConfigurationInfo().getGlEsVersion();
            if (Double.parseDouble(openGlVersion) >= 3.0) {
                return true;
            } else {
                Toast.makeText(activity, "App needs OpenGl Version 3.0 or later", Toast.LENGTH_SHORT).show();
                activity.finish();
                return false;
            }
        } else {
            Toast.makeText(activity, "App does not support required Build Version", Toast.LENGTH_SHORT).show();
            activity.finish();
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laptop);
        Intent intent = getIntent();
        sofaModel = (SofaModel) intent.getSerializableExtra("model");
        System.out.println("Inside SofaArAct");
        System.out.println(sofaModel.toString());
        cd=findViewById(R.id.back);
        cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SofaArActivity.this, SofaDetailActivity.class));
                finish();
            }
        });

        if(checkSystemSupport(this)){
            // ArFragment is linked up with its respective id used in the activity_main.xml
            arCam = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arCameraArea);
            if (arCam != null) {
                arCam.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
                    tapCount++;
                    if (tapCount == 1) {
                        Anchor anchor = hitResult.createAnchor();
                            ModelRenderable.builder()
                                    .setSource(this, Uri.parse( sofaModel.getResource().toString()))
                                    .setIsFilamentGltf(true)
                                    .build()
                                    .thenAccept(modelRenderable -> addModel(anchor, modelRenderable))
                                    .exceptionally(throwable -> {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                        builder.setMessage("Something went wrong" + throwable.getMessage()).show();
                                        return null;
                                    });
                    }
                });
            }
        }

    }

    private void addModel(Anchor anchor, ModelRenderable modelRenderable) {

        // Creating a AnchorNode with a specific anchor
        AnchorNode anchorNode = new AnchorNode(anchor);

        // attaching the anchorNode with the ArFragment
        anchorNode.setParent(arCam.getArSceneView().getScene());

        // attaching the anchorNode with the TransformableNode
        TransformableNode model = new TransformableNode(arCam.getTransformationSystem());
        model.setParent(anchorNode);

        // attaching the 3d model with the TransformableNode
        // that is already attached with the node
        model.setRenderable(modelRenderable);
        model.select();
    }
}