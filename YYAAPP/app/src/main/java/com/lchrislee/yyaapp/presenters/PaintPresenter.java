package com.lchrislee.yyaapp.presenters;

import android.graphics.Bitmap;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.lchrislee.yyaapp.fragments.dialogs.SaveDialog;
import com.lchrislee.yyaapp.utilities.ImageIO;
import com.lchrislee.yyaapp.views.BrushSizeView;
import com.lchrislee.yyaapp.views.PaletteView;
import com.lchrislee.yyaapp.views.canvas.CanvasView;

public class PaintPresenter implements
    PaletteView.ColorSelect, BrushSizeView.StrokeSizeChange, SaveDialog.SaveDetailsFinalized
{

    public interface SaveFinished
    {
        void OnSaveFinished(boolean isSuccessful);
    }

    private static final String TAG = "PaintPresenter";

    private final CanvasView canvas;

    private SaveFinished saveFinishedListener;

    public PaintPresenter (
        @NonNull CanvasView canvas,
        @NonNull PaletteView palette,
        @NonNull BrushSizeView brush
    ) {
        this.canvas = canvas;
        brush.setStrokeChangeListener(this);
        palette.setColorSelectListener(this);
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
        if(ImageIO.save(fileName, canvas.drawing()))
        {
            saveFinishedListener.OnSaveFinished(true);
        }
        else if (saveFinishedListener != null)
        {
            saveFinishedListener.OnSaveFinished(false);
        }
    }

    public void setSaveFinishedListener (
        @NonNull SaveFinished saveFinishedListener
    ) {
        this.saveFinishedListener = saveFinishedListener;
    }

    public void loadImage(
        @Nullable Bitmap image
    ) {
        this.canvas.useImageAsBase(image);
    }
}
