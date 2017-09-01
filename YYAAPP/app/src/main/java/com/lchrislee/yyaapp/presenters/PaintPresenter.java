package com.lchrislee.yyaapp.presenters;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.lchrislee.yyaapp.fragments.dialogs.SaveDialog;
import com.lchrislee.yyaapp.views.BrushSizeView;
import com.lchrislee.yyaapp.views.PaletteView;
import com.lchrislee.yyaapp.views.canvas.CanvasView;

public class PaintPresenter implements
    PaletteView.ColorSelect, BrushSizeView.StrokeSizeChange, SaveDialog.SaveDetailsFinalized
{

    private final PaletteView palette;
    private final CanvasView canvas;
    private final BrushSizeView brush;

    public PaintPresenter (
        @NonNull CanvasView canvas,
        @NonNull PaletteView palette,
        @NonNull BrushSizeView brush
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
        @ColorInt int color
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

    public
    @NonNull
    DialogFragment saveDialog ()
    {
        SaveDialog fragment = SaveDialog.newInstance();
        fragment.setSaveListener(this);
        return fragment;
    }

    @Override
    public void OnSave (
        @NonNull String fileName
    ) {
        // TODO: Save using filename and the bitmap of canvas.
    }
}
