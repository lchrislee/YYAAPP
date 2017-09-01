package com.lchrislee.yyaapp.views.canvas;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.ColorInt;
import android.support.annotation.Dimension;
import android.support.annotation.NonNull;

import com.lchrislee.yyaapp.views.canvas.model.BrushStroke;
import com.lchrislee.yyaapp.views.canvas.model.CanvasHistory;

import java.util.Iterator;

/*
 * Since I never made a paint program, drawing on the Canvas is based off of this tutorial:
 * https://code.tutsplus.com/tutorials/android-sdk-create-a-drawing-app-touch-interaction--mobile-19202
 */

class CanvasViewInternal
{
    private final Paint canvasBrush = new Paint(Paint.DITHER_FLAG);

    private Paint currentBrushPaint;
    private Path currentBrushPath;

    private Canvas internalCanvas;
    private Bitmap internalBitmap;

    private float currentBrushWidth;
    private int currentBrushColor;

    private int canvasWidth;
    private int canvasHeight;

    private final CanvasHistory history;

    CanvasViewInternal (
        @ColorInt int defaultColor,
        @Dimension float defaultSize
    ) {
        this.currentBrushColor = defaultColor;
        this.currentBrushWidth = defaultSize;
        history = new CanvasHistory();
        generateCurrentBrush();
    }

    private void generateCurrentBrush ()
    {
        currentBrushPath = new Path();
        currentBrushPaint = new Paint();

        currentBrushPaint.setAntiAlias(true);
        currentBrushPaint.setStyle(Paint.Style.STROKE);
        currentBrushPaint.setStrokeJoin(Paint.Join.ROUND);
        currentBrushPaint.setStrokeCap(Paint.Cap.ROUND);
        currentBrushPaint.setStrokeWidth(currentBrushWidth);
        currentBrushPaint.setColor(currentBrushColor);
    }

    void createEmptyCanvas ()
    {
        internalBitmap = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888);
        internalCanvas = new Canvas(internalBitmap);
    }

    /*
     * Property Methods
     */

    void changePaintColor (
        @ColorInt int paintColor
    ) {
        this.currentBrushColor = paintColor;
        currentBrushPaint.setColor(paintColor);
    }

    void changeStrokeWidth (
        @Dimension float strokeWidth
    ) {
        this.currentBrushWidth = strokeWidth;
        currentBrushPaint.setStrokeWidth(strokeWidth);
    }

    void changeDimensions (
        int width,
        int height
    ) {
        this.canvasWidth = width;
        this.canvasHeight = height;
    }

    /*
     * Drawing Methods
     */

    void update (
        @NonNull Canvas canvas
    ) {
        canvas.drawBitmap(internalBitmap, 0, 0, canvasBrush);
        placeOnCanvas();
    }

    void movePath (
        float xPos,
        float yPos
    ) {
        history.clearUndo();
        currentBrushPath.moveTo(xPos, yPos);
    }

    void lineTo (
        float xPos,
        float yPos
    ) {
        currentBrushPath.lineTo(xPos, yPos);
    }

    void drawCurrentPath ()
    {
        history.addDrawingChange(new BrushStroke(currentBrushPath, currentBrushPaint));
        placeOnCanvas();
        generateCurrentBrush();
    }

    private void placeOnCanvas ()
    {
        internalCanvas.drawPath(currentBrushPath, currentBrushPaint);
    }

    private void drawHistory ()
    {
        createEmptyCanvas();

        Iterator<BrushStroke> pathIterator = history.fromBeginning();
        if (pathIterator == null)
        {
            return;
        }

        BrushStroke currentBrushStroke;

        while (pathIterator.hasNext())
        {
            currentBrushStroke = pathIterator.next();
            internalCanvas.drawPath(currentBrushStroke.path, currentBrushStroke.paint);
        }
    }

    /*
     * History Methods
     */

    void undo()
    {
        BrushStroke brushStroke = history.lastAddition();
        if (brushStroke == null)
        {
            return;
        }

        history.addUndoChange(brushStroke);
        drawHistory();
    }

    void redo()
    {
        BrushStroke brushStroke = history.lastUndo();
        if (brushStroke == null)
        {
            return;
        }

        history.addDrawingChange(brushStroke);
        drawHistory();
    }

    void clearHistory ()
    {
        history.clearAllHistory();
    }
}
