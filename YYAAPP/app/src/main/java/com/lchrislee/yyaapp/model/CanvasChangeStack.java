package com.lchrislee.yyaapp.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Iterator;
import java.util.Stack;

class CanvasChangeStack
{

    private final Stack<CanvasChange> changes;

    CanvasChangeStack ()
    {
        changes = new Stack<>();
    }

    void addChange (
        @NonNull CanvasChange newPath
    ) {
        changes.add(newPath);
    }

    @Nullable
    CanvasChange mostRecentChange ()
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
    Iterator<CanvasChange> fromBeginning ()
    {
        if (changes.isEmpty())
        {
            return null;
        }
        return changes.iterator();
    }
}
