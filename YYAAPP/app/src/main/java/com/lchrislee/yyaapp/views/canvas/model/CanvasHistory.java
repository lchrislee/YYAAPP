package com.lchrislee.yyaapp.views.canvas.model;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Stack;
import java.util.function.Function;

public class CanvasHistory
{

    private final Stack<BrushStroke> drawHistory;
    private final Stack<BrushStroke> undoHistory;

    public CanvasHistory ()
    {
        drawHistory = new Stack<>();
        undoHistory = new Stack<>();
    }

    public void addStroke (
        @NonNull BrushStroke change
    ) {
        drawHistory.push(change);
    }

    public void addUndo (
        @NonNull BrushStroke brushStroke
    ) {
        undoHistory.push(brushStroke);
    }

    public
    @Nullable
    BrushStroke previousStroke ()
    {
        if (drawHistory.isEmpty())
        {
            return null;
        }
        return drawHistory.pop();
    }

    public
    @Nullable
    BrushStroke previousUndo ()
    {
        if (undoHistory.isEmpty())
        {
            return null;
        }

        return undoHistory.pop();
    }

    public void applyToStrokes (
        @NonNull Function<BrushStroke, Void> strokeMethod
    ) {
        if (drawHistory.isEmpty())
        {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            drawHistory.forEach(strokeMethod::apply);
        }
        else
        {
            for (BrushStroke stroke : drawHistory)
            {
                strokeMethod.apply(stroke);
            }
        }
    }

    public void clearAll ()
    {
        drawHistory.clear();
        undoHistory.clear();
    }

    public void clearUndo ()
    {
        undoHistory.clear();
    }
}
