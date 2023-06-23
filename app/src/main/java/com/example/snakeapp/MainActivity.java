package com.example.snakeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.graphics.Color;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {



    private final List<SnakePoints> snakePoints = new ArrayList<>();
    private SurfaceView surfaceView;
    private TextView ScoreTV;

    private SurfaceHolder surfaceHolder;

    //snake moving position. Values must be right, left, top, bottom.
    //By default snake move to right
    private String movingposition = "right";

    private int score = 0;

    private static final int pointSize = 28;

    private static final int defaultTalePoints = 3;

    private static final int snakeColor = Color.YELLOW;

    // speed must be 0 - 1000
    private static final int snakeMovingSpeed = 800;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getting surfaceview and score Textview from xml file
        surfaceView = findViewById(R.id.SurfaceView);
        ScoreTV = findViewById(R.id.ScoreTV);

        //getting ImageButtons from xml file
        final AppCompatImageButton topBtn = findViewById(R.id.topBtn);
        final AppCompatImageButton leftBtn = findViewById(R.id.leftBtn);
        final AppCompatImageButton rightBtn = findViewById(R.id.rightBtn);
        final AppCompatImageButton bottomBtn = findViewById(R.id.bottomBtn);

        //adding callback to surfaceview
        surfaceView.getHolder().addCallback(this);

        topBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!movingposition.equals("bottom")){
                    movingposition = "top";
                }
            }
        });

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!movingposition.equals("right")){
                    movingposition = "left";
                }
            }
        });

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!movingposition.equals("left")){
                    movingposition = "right";
                }
            }
        });

        bottomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!movingposition.equals("top")){
                    movingposition = "bottom";
                }
            }
        });
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        init();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }
    private void init(){
        snakePoints.clear();

        ScoreTV.setText("0");

        score = 0;

        movingposition = "right";

        int startPositionX = (pointSize) * defaultTalePoints;

        for (int i = 0; i < defaultTalePoints; i ++){
            SnakePoints snakePoints = new SnakePoints(positionX, positionY);
        }
    }
}