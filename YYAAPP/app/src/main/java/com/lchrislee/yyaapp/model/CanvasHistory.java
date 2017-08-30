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
        @NonNull CanvasChange change
    ) {
        drawingHistory.addChange(change);
    }

    public void addUndoChange (
        @NonNull CanvasChange canvasChange
    ) {
        undoHistory.addChange(canvasChange);
    }

    public
    @Nullable
    CanvasChange lastAddition ()
    {
        return drawingHistory.mostRecentChange();
    }

    public
    @Nullable
    CanvasChange lastUndo ()
    {
        return undoHistory.mostRecentChange();
    }

    public
    @Nullable
    Iterator<CanvasChange> fromBeginning ()
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
