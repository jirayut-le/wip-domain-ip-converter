package com.ske13.ntk.wip;

import android.annotation.SuppressLint;
import android.graphics.drawable.AnimationDrawable;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import static com.ske13.ntk.wip.Converter.convert;

public class FullscreenActivity extends AppCompatActivity implements OnMapReadyCallback{

    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private EditText editText;
    private TextView resultTextView, countryTextView;

    private GoogleMap googleMap;
    private MapView mapView;

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        mVisible = true;
        mContentView = findViewById(R.id.fullscreen_content);
        editText = findViewById(R.id.input);
        resultTextView = findViewById(R.id.result);
        countryTextView = findViewById(R.id.country);

        mapView = findViewById(R.id.map);
        if(mapView != null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

        RelativeLayout relativeLayout = findViewById(R.id.Layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(4000);
        animationDrawable.setExitFadeDuration(4000);
//        animationDrawable.start();

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
                resize();
            }
        });

    }

    private void submit(){
        ArrayList<String> result = convert(editText.getText().toString());
        if(result.size() == 0 ) {
           result.add("Error incorrect input!");
           result.add("Error incorrect input!");
           result.add("0");
           result.add("0");
        }
            setText(result);
            setLocation(Double.parseDouble(result.get(2)), Double.parseDouble(result.get(3)));

    }

    private void setText(ArrayList<String> result ){
        this.resultTextView.setText(result.get(0).toString());
        this.countryTextView.setText(result.get(1).toString());
    }

    private void resize(){
        resizeLayout();
        hideTitle();
        resizeIcon();
    }

    private void resizeLayout(){
        RelativeLayout relativeLayout = findViewById(R.id.Layout);
        ViewGroup.LayoutParams params = relativeLayout.getLayoutParams();
        params.height = 750;
        relativeLayout.setLayoutParams(params);
    }

    private void hideTitle(){
        TextView textView = findViewById(R.id.fullscreen_content);
        textView.setVisibility(View.GONE);
    }

    private void resizeIcon(){
        ImageView image = findViewById(R.id.logo);
        image.setMaxHeight(150);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(100);
    }


    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getBaseContext());
        this.googleMap = googleMap;
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

    }

    private void setLocation(double lat, double lng){
        this.googleMap.clear();
        this.googleMap.addMarker(new MarkerOptions().position(new LatLng( lat, lng)).title("HI").snippet("Hello"));
        CameraPosition cameraPosition = CameraPosition.builder().target(new LatLng( lat, lng)).zoom(5).bearing(0).tilt(45).build();
        this.googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}