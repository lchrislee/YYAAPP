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
    PaletteView.ColorSelectCallback, BrushSizeView.SizeChangeCallback, SaveDialog.ValidSaveCallback
{

    public interface SaveFinishedCallback
    {
        void onSaveFinished (boolean isSuccessful);
    }

    private static final String TAG = "PaintPresenter";

    private final CanvasView canvas;

    private SaveFinishedCallback callbackListener;

    public PaintPresenter (
        @NonNull CanvasView canvas,
        @NonNull PaletteView palette,
        @NonNull BrushSizeView brush
    ) {
        brush.setCallbackListener(this);
        palette.setCallbackListener(this);
        this.canvas = canvas;
        this.canvas.changeStrokeSize(brush.size());
        this.canvas.changePaintColor(palette.color());
    }

    public void setCallbackListener (
        @NonNull SaveFinishedCallback callbackListener
    ) {
        this.callbackListener = callbackListener;
    }

    /*
     * Interfaces
     */

    @Override
    public void onColorSelected (
        @ColorInt int color
    ) {
        canvas.changePaintColor(color);
    }

    @Override
    public void onSizeChanged (
        float size
    ) {
        canvas.changeStrokeSize(size);
    }

    @Override
    public void onSave (
        @NonNull String appName,
        @NonNull String imageName
    ) {
        boolean isSuccessful = ImageIO.save(appName, imageName, canvas.drawing());
        if (callbackListener != null)
        {
            callbackListener.onSaveFinished(isSuccessful);
        }
    }

    /*
     * Menu options
     */

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
        fragment.setCallbackListener(this);
        return fragment;
    }

    public void openImage (
        @Nullable Bitmap image
    ) {
        this.canvas.useImage(image);
    }
}
