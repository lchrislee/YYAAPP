package com.lchrislee.yyaapp.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Iterator;

public class CanvasHistory
{

    private final CanvasChangeStack drawHistory;
    private final CanvasChangeStack undoHistory;

    public CanvasHistory ()
    {
        drawHistory = new CanvasChangeStack();
        undoHistory = new CanvasChangeStack();
    }

    public void addDrawingChange (
        @NonNull BrushStroke change
    ) {
        drawHistory.addChange(change);
    }

    public void addUndoChange (
        @NonNull BrushStroke brushStroke
    ) {
        undoHistory.addChange(brushStroke);
    }

    public
    @Nullable
    BrushStroke lastAddition ()
    {
        return drawHistory.mostRecentChange();
    }

    public
    @Nullable
    BrushStroke lastUndo ()
    {
        return undoHistory.mostRecentChange();
    }

    public
    @Nullable
    Iterator<BrushStroke> fromBeginning ()
    {
        return drawHistory.fromBeginning();
    }

    public void clearAllHistory ()
    {
        drawHistory.clearHistory();
        clearUndo();
    }

    public void clearUndo ()
    {
        undoHistory.clearHistory();
    }
}
