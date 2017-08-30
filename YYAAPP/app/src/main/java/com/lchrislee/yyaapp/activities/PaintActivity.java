package com.lchrislee.yyaapp.activities;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.lchrislee.yyaapp.R;
import com.lchrislee.yyaapp.views.CanvasView;
import com.lchrislee.yyaapp.views.PaletteView;

public class PaintActivity extends AppCompatActivity
    implements PaletteView.ColorSelect
{

    private static final String TAG = "PaintActivity";

    private CanvasView canvas;

    private PaletteView palette;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint);

        // Binds as if using findViewById.
        canvas = (CanvasView) findViewById(R.id.activity_paint_canvas);
        palette = (PaletteView) findViewById(R.id.activity_paint_palette);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setTitle(R.string.toolbar_paint_new);
            actionBar.setDisplayShowTitleEnabled(true);
        }
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
                canvas.undoLast();
                break;
            case R.id.menu_paint_redo:
                canvas.redoLast();
                break;
            case R.id.menu_paint_clear:
                canvas.clearCanvas();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        palette.setColorSelectListener(this);
        canvas.changePaintColor(palette.colorSelected());
    }

    @Override
    public void OnColorSelected(@ColorRes int color) {
        canvas.changePaintColor(color);
    }
}
