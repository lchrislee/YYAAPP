package com.lchrislee.yyaapp.views.canvas;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.ColorInt;
import android.support.annotation.Dimension;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.lchrislee.yyaapp.views.canvas.model.BrushStroke;
import com.lchrislee.yyaapp.views.canvas.model.CanvasHistory;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/*
 * Since I never made a paint program, drawing on the Canvas is based off of this tutorial:
 * https://code.tutsplus.com/tutorials/android-sdk-create-a-drawing-app-touch-interaction--mobile-19202
 */

class CanvasViewInternal
{
    private final Paint canvasBrush = new Paint(Paint.DITHER_FLAG);

    private Canvas internalCanvas;
    private Bitmap internalBitmap;

    private Bitmap loadedBitmap;
    private boolean isLoaded;

    private int canvasWidth;
    private int canvasHeight;

    private float brushWidth;
    private int brushColor;

    private BrushStroke userBrush;
    private final CanvasHistory history;

    CanvasViewInternal (
        @ColorInt int defaultColor,
        @Dimension float defaultSize
    ) {
        this.brushColor = defaultColor;
        this.brushWidth = defaultSize;
        this.isLoaded = false;
        this.history = new CanvasHistory();
        this.canvasBrush.setColor(Color.WHITE);
        generateCurrentBrush();
    }

    private void generateCurrentBrush ()
    {
        Path path = new Path();
        Paint paint = new Paint();

        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(brushWidth);
        paint.setColor(brushColor);

        userBrush = new BrushStroke(path, paint);
    }

    void createBaseCanvas ()
    {
        if (isLoaded)
        {
            setupCanvas(loadedBitmap.copy(Bitmap.Config.ARGB_8888, true));
        }
        else
        {
            setupCanvas(Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888));
            internalCanvas.drawRect(0,0,canvasWidth, canvasHeight, canvasBrush);
        }
    }

    private void setupCanvas (
        @NonNull Bitmap image
    ) {
        internalBitmap = image;
        internalCanvas = new Canvas(image);
    }

    /*
     * Property Methods
     */

    void changePaintColor (
        @ColorInt int paintColor
    ) {
        brushColor = paintColor;
        userBrush.paint.setColor(brushColor);
    }

    void changeStrokeWidth (
        @Dimension float strokeWidth
    ) {
        brushWidth = strokeWidth;
        userBrush.paint.setStrokeWidth(brushWidth);
    }

    void changeDimensions (
        int width,
        int height
    ) {
        canvasWidth = width;
        canvasHeight = height;
    }

    @NonNull
    Bitmap currentDrawing()
    {
        return this.internalBitmap;
    }

    Observable<Boolean> useImage (
        @Nullable Bitmap image
    ) {
        if (image == null)
        {
            return null;
        }

        clearHistory();
        return Observable.fromCallable(() ->
        {
            loadedBitmap = Bitmap.createScaledBitmap(image, canvasWidth, canvasHeight, false);
            isLoaded = true;
            createBaseCanvas();
            return true;
        }).observeOn(Schedulers.computation());
    }

    /*
     * Drawing
     */

    void update (
        @NonNull Canvas canvas
    ) {
        canvas.drawBitmap(internalBitmap, 0, 0, canvasBrush);
        placeOnCanvas();
    }

    void moveTo (
        float xPos,
        float yPos
    ) {
        history.clearUndo();
        userBrush.path.moveTo(xPos, yPos);
    }

    void pathTo (
        float xPos,
        float yPos
    ) {
        userBrush.path.lineTo(xPos, yPos);
    }

    void finishStroke ()
    {
        history.addStroke(userBrush);
        placeOnCanvas();
        generateCurrentBrush();
    }

    private void placeOnCanvas ()
    {
        internalCanvas.drawPath(userBrush.path, userBrush.paint);
    }

    private void placeOnCanvas (
        @NonNull BrushStroke stroke
    ) {
        internalCanvas.drawPath(stroke.path, stroke.paint);
    }

    private
    @NonNull
    Observable<Boolean> drawHistory ()
    {
        createBaseCanvas();

        return Observable.fromCallable( () ->
        {
            history.applyToStrokes(brushStroke -> {
                if (brushStroke == null)
                {
                    return null;
                }
                placeOnCanvas(brushStroke);
                return null;
            });
            return true;
        }).observeOn(Schedulers.computation());
    }

    /*
     * History
     */

    @Nullable
    Observable<Boolean> undo()
    {
        BrushStroke brushStroke = history.previousStroke();
        if (brushStroke == null)
        {
            return null;
        }

        history.addUndo(brushStroke);
        return drawHistory();
    }

    @Nullable
    Observable<Boolean> redo()
    {
        BrushStroke brushStroke = history.previousUndo();
        if (brushStroke == null)
        {
            return null;
        }

        history.addStroke(brushStroke);
        return drawHistory();
    }

    void clearHistory ()
    {
        history.clearAll();
        isLoaded = false;
        loadedBitmap = null;
    }
}
