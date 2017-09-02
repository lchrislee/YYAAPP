package com.lchrislee.yyaapp.presenters;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.lchrislee.yyaapp.R;
import com.lchrislee.yyaapp.fragments.dialogs.SaveDialog;
import com.lchrislee.yyaapp.utilities.ImageIO;
import com.lchrislee.yyaapp.views.BrushSizeView;
import com.lchrislee.yyaapp.views.PaletteView;
import com.lchrislee.yyaapp.views.canvas.CanvasView;

public class PaintPresenter
{

    public interface SaveFinishedCallback
    {
        void onSaveFinished (boolean isSuccessful);
    }

    private static final String TAG = "PaintPresenter";

    private final CanvasView canvas;

    private SaveFinishedCallback listener;

    public PaintPresenter (
        @NonNull View holder
    ) {
        canvas = holder.findViewById(R.id.fragment_paint_canvas);

        final BrushSizeView brush = holder.findViewById(R.id.fragment_paint_stroke_size);
        brush.setSizeListener(canvas::changeStrokeSize);

        final PaletteView palette = holder.findViewById(R.id.fragment_paint_palette);
        palette.setColorSelectListener(canvas::changePaintColor);
    }

    public void setSaveFinishedListener (
        @NonNull SaveFinishedCallback callbackListener
    ) {
        this.listener = callbackListener;
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
        fragment.setValidSaveListener( (appName, imageName) -> {
            boolean isSuccessful = ImageIO.save(appName, imageName, canvas.drawing());

            if (listener != null)
            {
                listener.onSaveFinished(isSuccessful);
            }
        });
        return fragment;
    }

    public void openImage (
        @Nullable Bitmap image
    ) {
        this.canvas.useImage(image);
    }
}
