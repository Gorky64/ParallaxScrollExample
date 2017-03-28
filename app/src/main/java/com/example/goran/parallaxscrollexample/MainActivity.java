package com.example.goran.parallaxscrollexample;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

public class MainActivity extends Activity {

    private ParallaxView parallaxView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get a Display object to access screen details
        Display display = getWindowManager().getDefaultDisplay();

        // Load the resolution into a Point object
        Point resolution = new Point();
        display.getSize(resolution);

        parallaxView = new ParallaxView(this, resolution.x, resolution.y);
        setContentView(parallaxView);

    }

    @Override
    protected void onPause() {
        super.onPause();
        parallaxView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        parallaxView.resume();
    }

}
