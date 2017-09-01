package com.lchrislee.yyaapp.views.canvas.model;

import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.NonNull;

public class BrushStroke
{

    public final Path path;
    public final Paint paint;

    public BrushStroke (
        @NonNull Path path,
        @NonNull Paint paint
    ) {

        this.path = path;
        this.paint = paint;
    }
}
