package com.lchrislee.yyaapp.model;

import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.NonNull;

public class CanvasChange
{

    public final Path path;
    public final Paint brush;

    public CanvasChange(
        @NonNull Path path,
        @NonNull Paint brush
    ) {

        this.path = path;
        this.brush = brush;
    }
}
