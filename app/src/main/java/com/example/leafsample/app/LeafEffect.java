package com.example.leafsample.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Random;

/**
 * Created by mattlyzheng on 2015/9/24.
 */
public class LeafEffect extends View {
    private Leaf[] leaves;
    private float deltaA;
    private Random random;
    private Matrix matrix;
    private Handler handler = new Handler()  {
        @Override
        public void handleMessage(Message msg) {
            invalidate();
        }
    };

    public LeafEffect(Context context) {
        this(context, null);
    }

    public LeafEffect(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LeafEffect(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        deltaA = 0;
        random = new Random();
        matrix = new Matrix();
        leaves = new Leaf[8];
        for (int i = 0; i < 8; i++) {
            leaves[i] = new Leaf(getContext());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < 8; i++) {
            if (leaves[i].delayFly == 0) {
                leaves[i].drawLeaf(canvas);
            } else {
                --leaves[i].delayFly;
            }

        }
//        try {
//            Thread.sleep(50);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        invalidate();
        handler.sendEmptyMessageDelayed(0, 100);
    }

    class Leaf{
        public float center_x;
        public float center_y;
        public float angle;
        public Bitmap leaf;
        public boolean toLeft;
        public int delayFly;

        public Leaf(Context context){
            delayFly = generateRandomInt(0, 200);
            center_x = generateRandomInt(28, 800-28);
            center_y = -58;
            this.angle = generateRandomInt(-25, 25);
            toLeft = generateRandomInt(0, 1) == 0;
            leaf = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.yellow_leaf)).getBitmap();
        }

        public void drawLeaf(Canvas canvas) {
            //正为右，负为左
            matrix.setRotate(-angle, 28, 4);
            canvas.translate(center_x, center_y);
            canvas.drawBitmap(leaf, matrix, null);
            canvas.translate(-center_x, -center_y);
            if (toLeft)
                angle -= 0.5;
            else
                angle += 0.5;

//            angle += deltaAngle;
            if (angle > 25) {
                angle = 25;
                toLeft = true;
            }
            else if (angle < -25) {
                angle = -25;
                toLeft = false;
            }

            center_x += 10 * Math.sin(angle/180*Math.PI);
            center_y += 10 * Math.cos(angle/180*Math.PI);
//            center_x += 0;
//            center_y += 1;
            if (center_y > 1024) {
                delayFly = generateRandomInt(0, 200);
                center_y = -58;
            }
            if (center_x < 0)
                center_x = 0;
            else if (center_x > 1080)
                center_x = 1080;
        }
    }

    private int generateRandomInt(int start, int end) {
        return random.nextInt(end - start + 1) + start;
    }
}
