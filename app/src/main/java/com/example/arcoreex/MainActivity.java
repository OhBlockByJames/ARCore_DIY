package com.example.arcoreex;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.telephony.IccOpenLogicalChannelResponse;
import android.view.MotionEvent;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;


public class MainActivity extends AppCompatActivity {

    private ArFragment arFragment;
    private ModelRenderable sphereRenderable;
    private ViewRenderable calendarViewRenderable;
    private ModelRenderable truckRenderable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arFragment=(ArFragment)getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        MaterialFactory.makeOpaqueWithColor(this,new com.google.ar.sceneform.rendering.Color(Color.BLUE))
                .thenAccept( meterial -> {
                    sphereRenderable = ShapeFactory.makeSphere(0.1f,new Vector3(0.0f,0.15f,0.0f),meterial);
                });

        ViewRenderable.builder().setView(this,R.layout.calendar_viww).build()
                .thenAccept( viewRenderable -> calendarViewRenderable=viewRenderable );

        ModelRenderable.builder().setSource(this, Uri.parse("model.gltf"))
                .build()
                .thenAccept(modelRenderable -> truckRenderable=modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to 3d asset", Toast.LENGTH_SHORT).show();
                    return null;
                });

        arFragment.setOnTapArPlaneListener((HitResult hitResult, Plane plane, MotionEvent motionEvent)->{
            if(sphereRenderable!=null)
            {
                if(plane.getType()==Plane.Type.HORIZONTAL_UPWARD_FACING)
                {
                    //Create an Anchor
                    Anchor anchor=hitResult.createAnchor();
                    AnchorNode anchorNode=new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    //Create a Sphere TransformableNode
                    TransformableNode sphere = new TransformableNode(arFragment.getTransformationSystem());
                    sphere.setParent(anchorNode);
                    sphere.setRenderable(sphereRenderable);
                    sphere.select();

                    //Create a Calendar TransformableNode
//                    TransformableNode calendar=new TransformableNode(arFragment.getTransformationSystem());
//                    calendar.setParent(anchorNode);
//                    calendar.setRenderable(calendarViewRenderable);
//                    calendar.setLocalPosition(new Vector3(0f,0.5f,0f));
//                    calendar.select();

                    //Create a Truck TransformableNode
//                    TransformableNode truck=new TransformableNode(arFragment.getTransformationSystem());
//                    truck.setParent(anchorNode);
//                    truck.setRenderable(truckRenderable);
//                    truck.setLocalPosition(new Vector3(-0.250f,0f,0f));
//                    truck.select();
                }

            }
        });



//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}