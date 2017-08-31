package com.lchrislee.yyaapp.controllers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.ColorInt;
import android.support.annotation.Dimension;
import android.support.annotation.NonNull;

import com.lchrislee.yyaapp.model.CanvasChange;
import com.lchrislee.yyaapp.model.CanvasHistory;

import java.util.Iterator;

public class CanvasPresenter
{
    private final Paint canvasBrush = new Paint(Paint.DITHER_FLAG);

    private Paint brush;
    private Path brushPath;

    private Canvas internalCanvas;
    private Bitmap internalBitmap;

    private float brushWidth;
    private int brushColor;

    private int canvasWidth;
    private int canvasHeight;

    private CanvasHistory history;

    public CanvasPresenter (
        @ColorInt int defaultColor,
        @Dimension float defaultSize
    ) {
        this.brushColor = defaultColor;
        this.brushWidth = defaultSize;
        history = new CanvasHistory();
        generateCurrentBrush();
    }

    private void generateCurrentBrush ()
    {
        brushPath = new Path();
        brush = new Paint();

        brush.setAntiAlias(true);
        brush.setStyle(Paint.Style.STROKE);
        brush.setStrokeJoin(Paint.Join.ROUND);
        brush.setStrokeCap(Paint.Cap.ROUND);
        brush.setStrokeWidth(brushWidth);
        brush.setColor(brushColor);
    }

    // Returns whether the new canvas would be visible.
    public boolean createEmptyCanvas ()
    {
        history.clearAllHistory();
        internalBitmap = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888);
        internalCanvas = new Canvas(internalBitmap);
        return canvasHeight <= 0 || canvasWidth <= 0;
    }

    /*
     * Property Methods
     */

    public void changePaintColor (
        @ColorInt int paintColor
    ) {
        this.brushColor = paintColor;
        brush.setColor(paintColor);
    }

    public void changeStrokeWidth (
        @Dimension float strokeWidth
    ) {
        this.brushWidth = strokeWidth;
        brush.setStrokeWidth(strokeWidth);
    }

    public void changeDimensions (
        int width,
        int height
    ) {
        this.canvasWidth = width;
        this.canvasHeight = height;
    }

    /*
     * Drawing Methods
     */

    public void update (
        @NonNull Canvas canvas
    ) {
        canvas.drawBitmap(internalBitmap, 0, 0, canvasBrush);
        canvas.drawPath(brushPath, brush);
    }

    public void movePath (
        float xPos,
        float yPos
    ) {
        brushPath.moveTo(xPos, yPos);
    }

    public void lineTo (
        float xPos,
        float yPos
    ) {
        brushPath.lineTo(xPos, yPos);
    }

    public void drawCurrentPath ()
    {
        history.addDrawingChange(new CanvasChange(brushPath, brush));
        generateCurrentBrush();
        brush.setColor(brushColor);
    }

    private void drawHistory ()
    {
        createEmptyCanvas();

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
    }

    /*
     * History Methods
     */

    public void clearUndoHistory()
    {
        history.clearUndo();
    }

    public void undo()
    {
        CanvasChange canvasChange = history.lastAddition();
        if (canvasChange == null)
        {
            return;
        }

        history.addUndoChange(canvasChange);
        drawHistory();
    }

    public void redo()
    {
        CanvasChange canvasChange = history.lastUndo();
        if (canvasChange == null)
        {
            return;
        }

        history.addDrawingChange(canvasChange);
        drawHistory();
    }
}
