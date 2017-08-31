package com.lchrislee.yyaapp.presenters;

import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;

import com.lchrislee.yyaapp.views.BrushView;
import com.lchrislee.yyaapp.views.PaletteView;
import com.lchrislee.yyaapp.views.canvas.CanvasView;

public class PaintPresenter
    implements PaletteView.ColorSelect, BrushView.StrokeSizeChange
{

    private PaletteView palette;
    private CanvasView canvas;
    private BrushView brush;

    public PaintPresenter (
        @NonNull CanvasView canvas,
        @NonNull PaletteView palette,
        @NonNull BrushView brush
    ) {
        this.palette = palette;
        this.canvas = canvas;
        this.brush = brush;
        brush.setStrokeChangeListener(this);
        palette.setColorSelectListener(this);
    }

    public void refresh ()
    {
        canvas.changeBrush(palette.colorSelected(), brush.strokeSize());
    }

    @Override
    public void OnColorSelected(
        @ColorRes int color
    ) {
        canvas.changePaintColor(color);
    }

    @Override
    public void OnStrokeChanged (
        float size
    ) {
        canvas.changeStrokeSize(size);
    }

    public void undoChange ()
    {
        canvas.undo();
    }

    public void redoChange ()
    {
        canvas.redo();
    }

    public void clearCanvas ()
    {
        canvas.clear();
    }
}
