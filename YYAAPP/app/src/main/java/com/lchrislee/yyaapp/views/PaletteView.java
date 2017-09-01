package com.lchrislee.yyaapp.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lchrislee.yyaapp.R;

import java.util.ArrayList;

public class PaletteView extends LinearLayout
        implements View.OnClickListener
{

    public interface ColorSelect
    {
        void OnColorSelected (@ColorInt int color);
    }

    private static final String TAG = "PaletteView";

    private ArrayList<AppCompatImageButton> buttons;

    @ColorInt
    private int colorSelected;

    private ColorSelect colorSelectListener;

    public PaletteView (Context context)
    {
        super(context);
        initialize(null);
    }

    public PaletteView (Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        initialize(attrs);
    }

    public PaletteView (Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initialize(attrs);
    }

    private void initialize (
        @Nullable AttributeSet attrs
    ) {
        buttons = new ArrayList<>();

        final TypedArray attributes = getContext().getTheme().obtainStyledAttributes(
            attrs,
            R.styleable.PaletteView,
            0,
            R.style.PaletteDefaultStyle
        );

        final int colorsId = attributes.getResourceId(
            R.styleable.PaletteView_colors,
            R.array.palette_colors_default
        );

        int size = (int) getResources().getDimension(R.dimen.x5);
        int margin = (int) getResources().getDimension(R.dimen.x0_5);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(margin, margin, margin, margin);
        params.width = size;
        params.height = size;

        final int[] values = getResources().getIntArray(colorsId);

        for (int colorId : values)
        {
            AppCompatImageButton imageButton = new AppCompatImageButton(getContext());
            imageButton.setLayoutParams(params);
            imageButton.setTag(colorId);
            imageButton.setBackgroundColor(colorId);
            imageButton.setOnClickListener(this);
            buttons.add(imageButton);
            addView(imageButton);
        }

        colorSelected = (Integer) buttons.get(0).getTag();
        buttons.get(0).setImageResource(android.R.drawable.ic_menu_view);

        attributes.recycle();
    }

    public void setColorSelectListener (
        @NonNull ColorSelect colorSelectListener
    ) {
        this.colorSelectListener = colorSelectListener;
    }

    @Override
    public void onClick (View view)
    {
        designateCurrentColor((AppCompatImageButton) view);

        colorSelected = (int) view.getTag();

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
