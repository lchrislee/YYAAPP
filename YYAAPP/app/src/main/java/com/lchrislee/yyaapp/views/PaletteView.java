package com.lchrislee.yyaapp.views;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.lchrislee.yyaapp.R;

import java.util.ArrayList;

public class PaletteView extends LinearLayout
        implements View.OnClickListener
{

    public interface ColorSelect
    {
        void OnColorSelected (@ColorRes int color);
    }

    private static final String TAG = "PaletteView";

    private ArrayList<AppCompatImageButton> buttons;

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
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        final View internalView = inflater.inflate(R.layout.view_palette, this, true);

        buttons = new ArrayList<>();

        buttons.add((AppCompatImageButton) internalView.findViewById(R.id.view_palette_black));
        buttons.add((AppCompatImageButton) internalView.findViewById(R.id.view_palette_white));
        buttons.add((AppCompatImageButton) internalView.findViewById(R.id.view_palette_red));
        buttons.add((AppCompatImageButton) internalView.findViewById(R.id.view_palette_yellow));
        buttons.add((AppCompatImageButton) internalView.findViewById(R.id.view_palette_green));
        buttons.add((AppCompatImageButton) internalView.findViewById(R.id.view_palette_blue));
        buttons.add((AppCompatImageButton) internalView.findViewById(R.id.view_palette_brown));

        for (AppCompatImageButton button : buttons)
        {
            button.setOnClickListener(this);
        }

        colorSelected = android.R.color.black;
        buttons.get(0).setImageResource(android.R.drawable.ic_menu_view);
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
        designateCurrentColor((AppCompatImageButton) view);

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

    private void designateCurrentColor(
        @NonNull AppCompatImageButton view
    ) {
        for (AppCompatImageButton button : buttons)
        {
            button.setImageDrawable(null);
        }

        view.setImageResource(android.R.drawable.ic_menu_view);
    }

}
