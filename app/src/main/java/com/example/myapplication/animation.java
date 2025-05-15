package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.util.Log;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;

public class animation extends AppCompatActivity {
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private int progressStatus = 0;
    private Bitmap bitmap;
    private ProgressBar progressBar;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation);

        surfaceView = findViewById(R.id.surfaceView3);
        surfaceHolder = surfaceView.getHolder();
        progressBar = findViewById(R.id.progressBar);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.animation);
        mediaPlayer = MediaPlayer.create(this, R.raw.music);
        mediaPlayer.start();
        startAnimation();
    }

    private void startAnimation() {
        new Thread(() -> {
            while (progressStatus < 100) {
                progressStatus++;
                drawCanvas(progressStatus);
                updateProgressBar(progressStatus);

                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    Log.e("AnimationActivity", "Thread interrupted", e);
                }
            }
            mediaPlayer.stop();
            mediaPlayer.release();
            startActivity(new Intent(animation.this, loginActivity.class));
            finish();
        }).start();
    }

    private void drawCanvas(int progress) {
        Canvas canvas = null;
        try {
            canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                canvas.drawColor(Color.WHITE);

                float width = canvas.getWidth();
                float height = canvas.getHeight();
                float scaledWidth = (width * progress) / 100.0f;
                float scaledHeight = (bitmap.getHeight() * scaledWidth) / bitmap.getWidth();

                float xStart = (width - scaledWidth) / 2;
                float yStart = (height - scaledHeight) / 2;

                canvas.drawBitmap(Bitmap.createScaledBitmap(bitmap, (int) scaledWidth, (int) scaledHeight,
                        true), xStart, yStart, new Paint());
            } else {
                Log.e("AnimationActivity", "Failed to lock canvas");
            }
        } finally {
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    private void updateProgressBar(int progress) {
        runOnUiThread(() -> progressBar.setProgress(progress));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}
