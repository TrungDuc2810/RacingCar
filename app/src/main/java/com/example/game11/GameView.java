package com.example.game11;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;

public class GameView extends View {
    private Paint myPaint;
    private Rect buttonRect;
    private int speed = 1;
    private int time = 0;
    private int score = 0;
    private int myCarPosition = 0;
    private final ArrayList<HashMap<String, Object>> otherCars = new ArrayList<>();

    private int viewWidth = 0;
    private int viewHeight = 0;
    private final MainActivity gameTask;
    private final String buttonText = "STOP";

    public GameView(Context context, MainActivity gameTask) {
        super(context);
        this.gameTask = gameTask;
        myPaint = new Paint();
        buttonRect = new Rect();
    }
    public int getScore(){
        return score;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        viewWidth = this.getMeasuredWidth();
        viewHeight = this.getMeasuredHeight();

        if (time % 700 < 10 + speed) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("lane", (int) (Math.random() * 3));
            map.put("startTime", time);
            otherCars.add(map);
        }
        time = time + 10 + speed;
        int carWidth = viewWidth / 5;
        int carHeight = carWidth + 10;
        myPaint.setStyle(Paint.Style.FILL);
        Drawable d = ContextCompat.getDrawable(getContext(), R.drawable.redcar);
        if (d != null) {
            d.setBounds(
                    myCarPosition * viewWidth / 3 + viewWidth / 15 + 25,
                    viewHeight - 2 - carHeight,
                    myCarPosition * viewWidth / 3 + viewWidth / 15 + carWidth - 25,
                    viewHeight - 2
            );
            d.draw(canvas);
        }

        myPaint.setColor(Color.GREEN);
        int highScore = 0;

        for (int i = 0; i < otherCars.size(); i++) {
            try {
                int carX = (int) otherCars.get(i).get("lane") * viewWidth / 3 + viewWidth / 15;
                int carY = time - (int) otherCars.get(i).get("startTime");

                Drawable d2 = ContextCompat.getDrawable(getContext(), R.drawable.yellowcar);
                if (d2 != null) {
                    d2.setBounds(
                            carX + 25, carY - carHeight, carX + carWidth - 25, carY
                    );
                    d2.draw(canvas);
                }

                if ((int) otherCars.get(i).get("lane") == myCarPosition) {
                    if (carY > viewHeight - 2 - carHeight && carY < viewHeight - 2) {
                        gameTask.closeGame(score);
                        score = 0;
                    }
                }
                if (carY > viewHeight + carHeight) {
                    otherCars.remove(i);
                    score++;
                    speed = 1 + Math.abs(score / 8);
                    if (score > highScore) {
                        highScore = score;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        myPaint.setColor(Color.WHITE);
        myPaint.setTextSize(40);
        canvas.drawText("SCORE: " + score, 80, 80, myPaint);
        canvas.drawText("SPEED: " + speed, 380, 80, myPaint);
        // Draw button
        myPaint.setColor(Color.parseColor("#FD397C"));
        myPaint.setStyle(Paint.Style.FILL);
        // Set button Rect coordinates
        buttonRect.left = 800;
        buttonRect.top = 30;
        buttonRect.right = buttonRect.left + 200; // Adjust button width as needed
        buttonRect.bottom = buttonRect.top + 100; // Adjust button height as needed
        canvas.drawRect(buttonRect, myPaint);

        // Draw button text
        myPaint.setColor(Color.WHITE);
        myPaint.setTextSize(40);
        float buttonTextWidth = myPaint.measureText(buttonText);
        float buttonTextX = buttonRect.centerX() - (buttonTextWidth / 2);
        float buttonTextY = buttonRect.centerY() + (myPaint.getTextSize() / 2);
        canvas.drawText(buttonText, buttonTextX, buttonTextY, myPaint);

        invalidate();
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float x1 = event.getX();
                if (x1 < viewWidth / 2) {
                    if (myCarPosition > 0) {
                        myCarPosition--;
                    }
                }
                if (x1 > viewWidth / 2) {
                    if (myCarPosition < 2) {
                        myCarPosition++;
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }
}