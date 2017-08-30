package com.lchrislee.yyaapp.activities;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v7.app.AppCompatActivity;

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
