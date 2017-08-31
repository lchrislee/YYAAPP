package com.lchrislee.yyaapp.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Iterator;

public class CanvasHistory
{

    private final CanvasChangeStack drawingHistory;
    private final CanvasChangeStack undoHistory;

    public CanvasHistory ()
    {
        drawingHistory = new CanvasChangeStack();
        undoHistory = new CanvasChangeStack();
    }

    public void addDrawingChange (
        @NonNull BrushStroke change
    ) {
        drawingHistory.addChange(change);
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
        return drawingHistory.mostRecentChange();
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
        return drawingHistory.fromBeginning();
    }

    public void clearAllHistory ()
    {
        drawingHistory.clearHistory();
        undoHistory.clearHistory();
    }

    public void clearUndo ()
    {
        undoHistory.clearHistory();
    }
}
