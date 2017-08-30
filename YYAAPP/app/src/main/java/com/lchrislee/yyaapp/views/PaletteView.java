package com.lchrislee.yyaapp.views;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.lchrislee.yyaapp.R;

public class PaletteView extends LinearLayout
        implements View.OnClickListener
{

    public interface ColorSelect
    {

        void OnColorSelected (@ColorRes int color);
    }

    private static final String TAG = "PaletteView";

    @ColorRes
    private int colorSelected;

    private ColorSelect colorSelectListener;

    public PaletteView (Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        initialize();
    }

    private void initialize ()
    {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View internalView = inflater.inflate(R.layout.view_palette, this, true);

        final Button black = internalView.findViewById(R.id.view_palette_black);
        black.setOnClickListener(this);
        final Button white = internalView.findViewById(R.id.view_palette_white);
        white.setOnClickListener(this);
        final Button red = internalView.findViewById(R.id.view_palette_red);
        red.setOnClickListener(this);
        final Button yellow = internalView.findViewById(R.id.view_palette_yellow);
        yellow.setOnClickListener(this);
        final Button green = internalView.findViewById(R.id.view_palette_green);
        green.setOnClickListener(this);
        final Button blue = internalView.findViewById(R.id.view_palette_blue);
        blue.setOnClickListener(this);
        final Button brown = internalView.findViewById(R.id.view_palette_brown);
        brown.setOnClickListener(this);

        colorSelected = android.R.color.black;
    }

    public void setColorSelectListener (@NonNull ColorSelect colorSelectListener)
    {
        this.colorSelectListener = colorSelectListener;
    }

    public
    @ColorRes
    int colorSelected ()
    {
        return colorSelected;
    }

    @Override
    public void onClick (View view)
    {
        switch (view.getId())
        {
            case R.id.view_palette_white:
                colorSelected = android.R.color.white;
                break;
            case R.id.view_palette_red:
                colorSelected = R.color.red;
                break;
            case R.id.view_palette_yellow:
                colorSelected = R.color.yellow;
                break;
            case R.id.view_palette_green:
                colorSelected = R.color.green;
                break;
            case R.id.view_palette_blue:
                colorSelected = R.color.blue;
                break;
            case R.id.view_palette_brown:
                colorSelected = R.color.brown;
                break;
            default:
                colorSelected = R.color.black;
                break;
        }

        if (colorSelectListener != null)
        {
            colorSelectListener.OnColorSelected(colorSelected);
        }
    }

}
