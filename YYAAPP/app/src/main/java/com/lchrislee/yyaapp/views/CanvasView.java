package com.lchrislee.yyaapp.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.lchrislee.yyaapp.R;

/*
 * Since I never made a paint program, this part is highly based off of this tutorial:
 * https://code.tutsplus.com/tutorials/android-sdk-create-a-drawing-app-touch-interaction--mobile-19202
 *
 */

public class CanvasView extends View
{
    private static final String TAG = "CanvasView";

    private final Paint canvasBrush = new Paint(Paint.DITHER_FLAG);

    private Path path;
    private Paint brush;
    private Canvas internalCanvas;
    private Bitmap internalBitmap;

    public CanvasView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        initialize();
    }

    private void initialize()
    {
        path = new Path();
        brush = new Paint();

        brush.setAntiAlias(true);
        brush.setStyle(Paint.Style.STROKE);
        brush.setStrokeJoin(Paint.Join.ROUND);
        brush.setStrokeCap(Paint.Cap.ROUND);
        brush.setStrokeWidth(
                getContext().getResources().getDimension(R.dimen.stroke_default)
        );
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        createCanvas(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(internalBitmap, 0, 0, canvasBrush);
        canvas.drawPath(path, brush);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float xPos = event.getX();
        float yPos = event.getY();

        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(xPos, yPos);
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(xPos, yPos);
                break;
            case MotionEvent.ACTION_UP:
                internalCanvas.drawPath(path, brush);
                path.reset();
                break;
            default:
                return false;
        }

        postInvalidate();
        return true;
    }

    public void changePaintColor(@ColorRes int newColor)
    {
        final int paintColor = ContextCompat.getColor(getContext(), newColor);
        brush.setColor(paintColor);
    }

    public void clear()
    {
        createCanvas(getWidth(), getHeight());
    }

    private void createCanvas(int w, int h)
    {
        internalBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        internalCanvas = new Canvas(internalBitmap);
        postInvalidate();
    }
}
