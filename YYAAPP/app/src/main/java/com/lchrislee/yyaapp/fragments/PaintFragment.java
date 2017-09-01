package com.lchrislee.yyaapp.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lchrislee.yyaapp.R;
import com.lchrislee.yyaapp.presenters.PaintPresenter;
import com.lchrislee.yyaapp.utilities.DataPersistence;
import com.lchrislee.yyaapp.views.BrushSizeView;
import com.lchrislee.yyaapp.views.PaletteView;
import com.lchrislee.yyaapp.views.canvas.CanvasView;

public class PaintFragment extends Fragment implements
    PaintPresenter.SaveFinished
{

    private static final String TAG = "PaintFragment";

    private PaintPresenter presenter;

    public PaintFragment ()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        final View holder = inflater.inflate(R.layout.fragment_paint, container, false);

        final CanvasView canvas = holder.findViewById(R.id.fragment_paint_canvas);
        final PaletteView palette = holder.findViewById(R.id.fragment_paint_palette);
        final BrushSizeView brush = holder.findViewById(R.id.fragment_paint_stroke_size);

        presenter = new PaintPresenter(canvas, palette, brush);
        presenter.setSaveFinishedListener(this);

        setHasOptionsMenu(true);

        return holder;
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_paint, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.menu_paint_undo:
                presenter.undoChange();
                break;
            case R.id.menu_paint_redo:
                presenter.redoChange();
                break;
            case R.id.menu_paint_save:
                if (detectInvalidPermissions())
                {
                    break;
                }
                displaySaveDialog();
                break;
            case R.id.menu_paint_clear:
                presenter.clearCanvas();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume ()
    {
        super.onResume();
        presenter.refresh();
    }

    private void displaySaveDialog()
    {
        DialogFragment fragment = presenter.saveDialog();
        fragment.show(getChildFragmentManager(), "SaveDialog");
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == DataPersistence.REQUEST_EXTERNAL)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                displaySaveDialog();
            }
            else
            {
                Toast.makeText(
                    getContext(),
                    R.string.dialog_save_permission_failed,
                    Toast.LENGTH_SHORT
                ).show();
            }
        }
    }

    private boolean detectInvalidPermissions ()
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        {
            return false;
        }

        int hasPermission = ContextCompat.checkSelfPermission(
            getContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        );

        if (hasPermission == PackageManager.PERMISSION_GRANTED)
        {
            return false;
        }

        requestPermissions(
            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
            DataPersistence.REQUEST_EXTERNAL
        );
        return true;
    }

    @Override
    public void OnSaveFinished (boolean isSuccessful)
    {
        int displayString = isSuccessful
            ? R.string.fragment_paint_save_success
            : R.string.fragment_paint_save_failure;

        Toast.makeText(
            getContext(),
            displayString,
            Toast.LENGTH_SHORT
        ).show();
    }
}
