package com.lchrislee.yyaapp.views;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.Dimension;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.lchrislee.yyaapp.R;
import com.lchrislee.yyaapp.controllers.CanvasPresenter;

/*
 * Since I never made a paint program, this part is highly based off of this tutorial:
 * https://code.tutsplus.com/tutorials/android-sdk-create-a-drawing-app-touch-interaction--mobile-19202
 */

public class CanvasView extends View
{

    private static final String TAG = "CanvasView";

    private CanvasPresenter presenter;

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
        presenter = new CanvasPresenter(defaultColor, defaultSize);
    }

    @Override
    protected void onSizeChanged (int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        presenter.changeDimensions(w, h);
        presenter.createEmptyCanvas();
        postInvalidate();
    }

    @Override
    protected void onDraw (Canvas canvas)
    {
        super.onDraw(canvas);
        presenter.update(canvas);
    }

    @Override
    public boolean onTouchEvent (MotionEvent event)
    {
        float xPos = event.getX();
        float yPos = event.getY();

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN: // This assumes the user will only ever use one finger.
                presenter.clearUndoHistory();
                presenter.movePath(xPos, yPos);
                break;
            case MotionEvent.ACTION_MOVE:
                presenter.lineTo(xPos, yPos);
                break;
            case MotionEvent.ACTION_UP:
                presenter.drawCurrentPath();
                break;
            default:
                return false;
        }

        // Redraws view on next iteration of main loop instead of immediately.
        postInvalidate();
        return true;
    }

    public void changeBrush(
        @ColorRes int newColor,
        @Dimension float newSize
    ) {
        changePaintColor(newColor);
        changeStrokeSize(newSize);
    }

    public void changePaintColor (
        @ColorRes int newColor
    ) {
        @ColorInt
        int color = ContextCompat.getColor(getContext(), newColor);
        presenter.changePaintColor(color);
    }

    public void changeStrokeSize (
        @Dimension float size
    ){
        presenter.changeStrokeWidth(size);
    }

    public void clear ()
    {
        presenter.createEmptyCanvas();
    }

    public void undo ()
    {
        presenter.undo();
        postInvalidate();
    }

    public void redo ()
    {
        presenter.redo();
        postInvalidate();
    }
}
