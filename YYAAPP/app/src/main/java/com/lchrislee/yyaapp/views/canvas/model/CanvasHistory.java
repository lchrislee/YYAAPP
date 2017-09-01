package com.lchrislee.yyaapp.views.canvas.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Iterator;
import java.util.Stack;

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

    public
    @Nullable
    Iterator<BrushStroke> fromBeginning ()
    {
        if (drawHistory.isEmpty())
        {
            return null;
        }
        return drawHistory.iterator();
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
