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
import com.lchrislee.yyaapp.model.CanvasChange;
import com.lchrislee.yyaapp.model.CanvasHistory;

import java.util.Iterator;

/*
 * Since I never made a paint program, this part is highly based off of this tutorial:
 * https://code.tutsplus.com/tutorials/android-sdk-create-a-drawing-app-touch-interaction--mobile-19202
 */

public class CanvasView extends View
{

    private static final String TAG = "CanvasView";

    private final Paint canvasBrush = new Paint(Paint.DITHER_FLAG);

    private Path path;
    private Paint paint;

    private Canvas internalCanvas;
    private Bitmap internalBitmap;

    private int paintColor;
    private float strokeWidth;

    private CanvasHistory history;

    public CanvasView (Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        initialize();
    }

    private void initialize ()
    {
        strokeWidth = getContext().getResources().getDimension(R.dimen.stroke_default);
        generateCurrentBrush();
        history = new CanvasHistory();
    }

    @Override
    protected void onSizeChanged (int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        createCanvas(w, h);
    }

    @Override
    protected void onDraw (Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.drawBitmap(internalBitmap, 0, 0, canvasBrush);
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent (MotionEvent event)
    {
        float xPos = event.getX();
        float yPos = event.getY();

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN: // This assumes the user will only ever use one finger.
                history.clearUndo();
                path.moveTo(xPos, yPos);
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(xPos, yPos);
                break;
            case MotionEvent.ACTION_UP:
                history.addDrawingChange(new CanvasChange(path, paint));
                internalCanvas.drawPath(path, paint);
                generateCurrentBrush();
                paint.setColor(paintColor);
                break;
            default:
                return false;
        }

        // Redraws view on next iteration of main loop instead of immediately.
        postInvalidate();
        return true;
    }

    private void generateCurrentBrush ()
    {
        path = new Path();
        paint = new Paint();

        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(strokeWidth);
    }

    public void changePaintColor (
        @ColorRes int newColor
    ) {
        paintColor = ContextCompat.getColor(getContext(), newColor);
        paint.setColor(paintColor);
    }

    public void clearCanvas ()
    {
        createCanvas(getWidth(), getHeight());
        history.clearAllHistory();
    }

    private void createCanvas (int w, int h)
    {
        internalBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        internalCanvas = new Canvas(internalBitmap);
        postInvalidate();
    }

    public void undoLast ()
    {
        CanvasChange canvasChange = history.lastAddition();
        if (canvasChange == null)
        {
            return;
        }

        history.addUndoChange(canvasChange);
        drawHistory();
    }

    public void redoLast ()
    {
        CanvasChange canvasChange = history.lastUndo();
        if (canvasChange == null)
        {
            return;
        }

        history.addDrawingChange(canvasChange);
        drawHistory();
    }

    private void drawHistory ()
    {
        createCanvas(getWidth(), getHeight());

        Iterator<CanvasChange> pathIterator = history.fromBeginning();
        if (pathIterator == null)
        {
            return;
        }

        CanvasChange currentCanvasChange;

        while (pathIterator.hasNext())
        {
            currentCanvasChange = pathIterator.next();
            internalCanvas.drawPath(currentCanvasChange.path, currentCanvasChange.brush);
        }

        postInvalidate();
    }
}
