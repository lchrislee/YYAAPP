package com.lchrislee.yyaapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.lchrislee.yyaapp.R;
import com.lchrislee.yyaapp.presenters.PaintPresenter;
import com.lchrislee.yyaapp.views.BrushSizeView;
import com.lchrislee.yyaapp.views.PaletteView;
import com.lchrislee.yyaapp.views.canvas.CanvasView;

public class PaintFragment extends Fragment
{

    private PaintPresenter presenter;

    public PaintFragment ()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate (@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
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
                DialogFragment fragment = presenter.saveDialog();
                fragment.show(getChildFragmentManager(), "SaveDialog");
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
}
