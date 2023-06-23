package com.example.snakeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {



    private final List<SnakePoints> snakePointsList = new ArrayList<>();
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

    private int positionX, positionY;

    private Timer timer;

    private Canvas canvas = null;

    private Paint pointColor = null;

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
        snakePointsList.clear();

        ScoreTV.setText("0");

        score = 0;

        movingposition = "right";

        int startPositionX = (pointSize) * defaultTalePoints;

        for (int i = 0; i < defaultTalePoints; i ++){

            SnakePoints snakePoints = new SnakePoints(startPositionX, pointSize);
            snakePointsList.add(snakePoints);

            startPositionX =startPositionX - (pointSize * 2);

        }

        addPoint();

        moveSnake();
    }

    private void addPoint(){
        int surfaceWidth = surfaceView.getWidth() - (pointSize * 2);
        int surfaceHeight = surfaceView.getHeight() - (pointSize * 2);

        int randomXPosition = new Random().nextInt(surfaceWidth / pointSize);
        int randomYPosition = new Random().nextInt(surfaceHeight / pointSize);

        if ((randomXPosition % 2) != 0){
            randomXPosition = randomXPosition + 1;
        }

        if ((randomYPosition % 2) != 0){
            randomYPosition = randomYPosition + 1;
        }

        positionX = (pointSize * randomXPosition) + pointSize;
        positionY = (pointSize * randomYPosition) + pointSize;

    }

    private void moveSnake(){
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int headpositionX = snakePointsList.get(0).getPositionX();
                int headpositionY = snakePointsList.get(0).getPositionY();

                if (headpositionX == positionX && positionY == headpositionY){

                    growSnake();

                    addPoint();
                }

                switch (movingposition){
                    case "right":
                        snakePointsList.get(0).setPositionX(headpositionX + (pointSize * 2));
                        snakePointsList.get(0).setPositionY(headpositionY);
                        break;

                    case "left":
                        snakePointsList.get(0).setPositionX(headpositionX - (pointSize * 2));
                        snakePointsList.get(0).setPositionY(headpositionY);
                        break;

                    case "top":
                        snakePointsList.get(0).setPositionX(headpositionX);
                        snakePointsList.get(0).setPositionY(headpositionY - (pointSize * 2));
                        break;

                    case "bottom":
                        snakePointsList.get(0).setPositionX(headpositionX);
                        snakePointsList.get(0).setPositionY(headpositionY + (pointSize * 2));
                        break;
                }

                if (checkGameOver(headpositionX, headpositionY)){

                    timer.purge();
                    timer.cancel();

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Your score =" + score);
                    builder.setTitle("Game Over");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Start again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            init();
                        }
                    });
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            builder.show();
                        }
                    });
                }

                else {
                    canvas = surfaceHolder.lockCanvas();

                    canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);

                    canvas.drawCircle(snakePointsList.get(0).getPositionX(), snakePointsList.get(0).getPositionY(), pointSize, createPointColor());

                    canvas.drawCircle(positionX, positionY, pointSize, createPointColor());

                    for (int i = 1; i < snakePointsList.size(); i++) {

                        int getTempPositionX = snakePointsList.get(i).getPositionX();
                        int getTempPositionY = snakePointsList.get(i).getPositionY();

                        snakePointsList.get(i).setPositionX(headpositionX);
                        snakePointsList.get(i).setPositionY(headpositionY);

                        canvas.drawCircle(snakePointsList.get(i).getPositionX(),snakePointsList.get(i).getPositionY(), pointSize, createPointColor());

                        headpositionX = getTempPositionX;
                        headpositionY = getTempPositionY;
                    }
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        },1000 - snakeMovingSpeed, 1000 - snakeMovingSpeed);
    }
    private void growSnake(){
        SnakePoints snakePoints = new SnakePoints(0, 0);

        snakePointsList.add(snakePoints);

        score ++;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ScoreTV.setText(String.valueOf(score));
            }
        });
    }
    private boolean checkGameOver(int headPositionX, int  headPositionY){
        boolean gameOver = false;

        if (snakePointsList.get(0).getPositionX() < 0 ||
                snakePointsList.get(0).getPositionY() < 0 ||
                snakePointsList.get(0).getPositionX() >= surfaceView.getWidth() ||
                snakePointsList.get(0).getPositionY() >= surfaceView.getHeight())
        {
            gameOver = true;
        }
        else{
            for(int i = 1; i < snakePointsList.size(); i ++){
                if (headPositionX == snakePointsList.get(i).getPositionX() &&
                        headPositionY == snakePointsList.get(i).getPositionY()){
                gameOver = true;
                break;
                }
            }
        }
        return gameOver;
    }
    private Paint createPointColor() {
        if (pointColor == null) {

            pointColor = new Paint();
            pointColor.setColor(snakeColor);
            pointColor.setStyle(Paint.Style.FILL);
            pointColor.setAntiAlias(true);

        }
            return pointColor;
    }
}