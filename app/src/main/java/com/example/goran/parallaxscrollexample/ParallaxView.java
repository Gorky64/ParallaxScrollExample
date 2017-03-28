package com.example.goran.parallaxscrollexample;

/**
 * Created by Goran on 27.3.2017..
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class ParallaxView extends SurfaceView implements Runnable {

    ArrayList<Background> backgrounds;
    Context context;
    long fps = 60;
    int screenWidth;
    int screenHeight;
    private volatile boolean running;
    private Thread gameThread = null;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder ourHolder;

    ParallaxView(Context context, int screenWidth, int screenHeight) {
        super(context);

        this.context = context;

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        ourHolder = getHolder();
        paint = new Paint();

        // Initialize our array list
        backgrounds = new ArrayList<>();

        //load the background data into the Background objects and
        // place them in our GameObject array list

        backgrounds.add(new Background(
                this.context,
                screenWidth,
                screenHeight,
                "skyline", 0, 80, 50));

        backgrounds.add(new Background(
                this.context,
                screenWidth,
                screenHeight,
                "grass", 70, 110, 200));

        // Add more backgrounds here

    }

    @Override
    public void run() {

        while (running) {
            long startFrameTime = System.currentTimeMillis();

            update();

            draw();

            // Calculate the fps this frame
            long timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }
        }
    }

    private void update() {
        // Update all the background positions
        for (Background bg : backgrounds) {
            bg.update(fps);
        }

    }

    private void draw() {

        if (ourHolder.getSurface().isValid()) {
            canvas = ourHolder.lockCanvas();
            canvas.drawColor(Color.argb(255, 0, 3, 70));
            drawBackground(0);
            paint.setTextSize(60);
            paint.setColor(Color.argb(255, 255, 255, 255));
            canvas.drawText("Example text", 350, screenHeight / 100 * 10, paint);
            paint.setTextSize(220);
            canvas.drawText("Text", 50, screenHeight / 100 * 80, paint);
            drawBackground(1);

            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause() {
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            // Error
        }
    }

    public void resume() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    private void drawBackground(int position) {

        // Make a copy of the relevant background
        Background bg = backgrounds.get(position);

        // define what portion of images to capture and
        // what coordinates of screen to draw them at

        // For the regular bitmap
        Rect fromRect1 = new Rect(0, 0, bg.width - bg.xClip, bg.height);
        Rect toRect1 = new Rect(bg.xClip, bg.startY, bg.width, bg.endY);

        // For the reversed background
        Rect fromRect2 = new Rect(bg.width - bg.xClip, 0, bg.width, bg.height);
        Rect toRect2 = new Rect(0, bg.startY, bg.xClip, bg.endY);

        //draw the two background bitmaps
        if (!bg.reversedFirst) {
            canvas.drawBitmap(bg.bitmap, fromRect1, toRect1, paint);
            canvas.drawBitmap(bg.bitmapReversed, fromRect2, toRect2, paint);
        } else {
            canvas.drawBitmap(bg.bitmap, fromRect2, toRect2, paint);
            canvas.drawBitmap(bg.bitmapReversed, fromRect1, toRect1, paint);
        }


    }
}