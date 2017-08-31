package com.lchrislee.yyaapp.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.lchrislee.yyaapp.R;
import com.lchrislee.yyaapp.presenters.PaintPresenter;
import com.lchrislee.yyaapp.views.BrushSizeView;
import com.lchrislee.yyaapp.views.PaletteView;
import com.lchrislee.yyaapp.views.canvas.CanvasView;

public class PaintActivity extends AppCompatActivity
{

    private static final String TAG = "PaintActivity";

    private PaintPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setTitle(R.string.toolbar_paint_new);
            actionBar.setDisplayShowTitleEnabled(true);
        }

        final CanvasView canvas = (CanvasView) findViewById(R.id.activity_paint_canvas);
        final PaletteView palette = (PaletteView) findViewById(R.id.activity_paint_palette);
        final BrushSizeView brush = (BrushSizeView) findViewById(R.id.activity_paint_stroke_size);

        presenter = new PaintPresenter(canvas, palette, brush);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_paint, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.menu_paint_save:
                Toast.makeText(
                    getApplicationContext(),
                    R.string.NOT_IMPLEMENTED,
                    Toast.LENGTH_SHORT
                ).show();
                break;
            case R.id.menu_paint_undo:
                presenter.undoChange();
                break;
            case R.id.menu_paint_redo:
                presenter.redoChange();
                break;
            case R.id.menu_paint_clear:
                presenter.clearCanvas();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.refresh();
    }

}
