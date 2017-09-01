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
    private Bitmap loadedBitmap;
    private Bitmap internalBitmap;

    private float currentBrushWidth;
    private int currentBrushColor;

    private int canvasWidth;
    private int canvasHeight;
    private boolean isLoadedImage;

    private final CanvasHistory history;

    CanvasViewInternal (
        @ColorInt int defaultColor,
        @Dimension float defaultSize
    ) {
        this.currentBrushColor = defaultColor;
        this.currentBrushWidth = defaultSize;
        this.isLoadedImage = false;
        this.history = new CanvasHistory();
        this.canvasBrush.setColor(Color.WHITE);
        generateCurrentBrush();
    }

    private void generateCurrentBrush ()
    {
        this.currentBrushPath = new Path();
        this.currentBrushPaint = new Paint();

        this.currentBrushPaint.setAntiAlias(true);
        this.currentBrushPaint.setStyle(Paint.Style.STROKE);
        this.currentBrushPaint.setStrokeJoin(Paint.Join.ROUND);
        this.currentBrushPaint.setStrokeCap(Paint.Cap.ROUND);
        this.currentBrushPaint.setStrokeWidth(currentBrushWidth);
        this.currentBrushPaint.setColor(currentBrushColor);
    }

    void createBaseCanvas ()
    {
        if (isLoadedImage)
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
        currentBrushColor = paintColor;
        currentBrushPaint.setColor(paintColor);
    }

    void changeStrokeWidth (
        @Dimension float strokeWidth
    ) {
        currentBrushWidth = strokeWidth;
        currentBrushPaint.setStrokeWidth(strokeWidth);
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

    boolean use (
        @Nullable Bitmap image
    ) {
        if (image == null)
        {
            return false;
        }

        clearHistory();
        loadedBitmap = image;
        isLoadedImage = true;
        createBaseCanvas();
        return true;
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
        createBaseCanvas();

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
        isLoadedImage = false;
        loadedBitmap = null;
    }
}
