package com.lchrislee.yyaapp.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Iterator;
import java.util.Stack;

class CanvasChangeStack
{

    private final Stack<BrushStroke> changes;

    CanvasChangeStack ()
    {
        changes = new Stack<>();
    }

    void addChange (
        @NonNull BrushStroke newPath
    ) {
        changes.add(newPath);
    }

    @Nullable
    BrushStroke mostRecentChange ()
    {
        if (changes.isEmpty())
        {
            return null;
        }
        return changes.pop();
    }

    void clearHistory ()
    {
        changes.clear();
    }

    @Nullable
    Iterator<BrushStroke> fromBeginning ()
    {
        if (changes.isEmpty())
        {
            return null;
        }
        return changes.iterator();
    }
}
