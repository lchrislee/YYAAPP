package com.lchrislee.yyaapp.views.canvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.ColorInt;
import android.support.annotation.Dimension;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.lchrislee.yyaapp.R;

/*
 * Since I never made a paint program, coordinating brush events is based off of this tutorial:
 * https://code.tutsplus.com/tutorials/android-sdk-create-a-drawing-app-touch-interaction--mobile-19202
 */

public class CanvasView extends View
{

    private static final String TAG = "CanvasView";

    private CanvasViewInternal internal;

    public CanvasView (Context context)
    {
        super(context);
        initialize();
    }

    public CanvasView (Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public CanvasView (Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        initialize();
    }

    private void initialize ()
    {
        @ColorInt
        final int defaultColor = ContextCompat.getColor(getContext(), R.color.black);
        @Dimension
        float defaultSize = getContext().getResources().getDimension(R.dimen.stroke_default);
        internal = new CanvasViewInternal(defaultColor, defaultSize);
    }

    @Override
    protected void onSizeChanged (int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        internal.changeDimensions(w, h);
        internal.createEmptyCanvas();
        // Redraws view on next iteration of main loop instead of immediately.
        postInvalidate();
    }

    @Override
    protected void onDraw (Canvas canvas)
    {
        super.onDraw(canvas);
        internal.update(canvas);
    }

    @Override
    public boolean onTouchEvent (MotionEvent event)
    {
        float xPos = event.getX();
        float yPos = event.getY();

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN: // This assumes the user will only ever use one finger.
                internal.movePath(xPos, yPos);
                break;
            case MotionEvent.ACTION_MOVE:
                internal.lineTo(xPos, yPos);
                break;
            case MotionEvent.ACTION_UP:
                internal.drawCurrentPath();
                break;
            default:
                return false;
        }

        postInvalidate();
        return true;
    }

    public void changeBrush(
        @ColorInt int newColor,
        @Dimension float newSize
    ) {
        changePaintColor(newColor);
        changeStrokeSize(newSize);
    }

    public void changePaintColor (
        @ColorInt int newColor
    ) {
        internal.changePaintColor(newColor);
    }

    public void changeStrokeSize (
        @Dimension float size
    ){
        internal.changeStrokeWidth(size);
    }

    public void clear ()
    {
        internal.clearHistory();
        internal.createEmptyCanvas();
        postInvalidate();
    }

    public void undo ()
    {
        internal.undo();
        postInvalidate();
    }

    public void redo ()
    {
        internal.redo();
        postInvalidate();
    }

    public
    @NonNull
    Bitmap drawing()
    {
        return internal.currentDrawing();
    }
}
