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

import com.jakewharton.rxbinding2.view.RxView;
import com.lchrislee.yyaapp.R;

import io.reactivex.Observable;

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
        RxView.touches(this).subscribe(this::onTouch);
    }

    /*
     * Properties
     */

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

    public
    @NonNull
    Bitmap drawing()
    {
        return internal.currentDrawing();
    }

    /*
     * View specific
     */

    @Override
    protected void onSizeChanged (int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        internal.changeDimensions(w, h);
        internal.createBaseCanvas();
        // Redraws view on next iteration of main loop instead of immediately.
        postInvalidate();
    }

    @Override
    protected void onDraw (Canvas canvas)
    {
        super.onDraw(canvas);
        internal.update(canvas);
    }

    // This assumes the user will only ever use one finger.
    private void onTouch (
        MotionEvent event
    ) {
        float xPos = event.getX();
        float yPos = event.getY();

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                internal.moveTo(xPos, yPos);
                break;
            case MotionEvent.ACTION_MOVE:
                internal.pathTo(xPos, yPos);
                break;
            case MotionEvent.ACTION_UP:
                internal.finishStroke();
                break;
            default:
                return;
        }

        postInvalidate();
    }

    /*
     * Update drawing
     */

    public void clear ()
    {
        internal.clearHistory();
        internal.createBaseCanvas();
        postInvalidate();
    }

    public void undo ()
    {
        Observable<Boolean> validObserve = internal.undo();
        if (validObserve == null)
        {
            return;
        }

        validObserve.subscribe(object -> postInvalidate());
    }

    public void redo ()
    {
        Observable<Boolean> validObserve = internal.redo();
        if (validObserve == null)
        {
            return;
        }

        validObserve.subscribe(object -> postInvalidate());
    }

    public void useImage (
        @Nullable Bitmap image
    ) {
        Observable<Boolean> successObservation = internal.useImage(image);
        if (successObservation == null)
        {
            return;
        }

        successObservation.subscribe(isSuccess -> postInvalidate());
    }
}
