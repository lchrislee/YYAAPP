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

import java.util.LinkedList;

public class PaletteView extends LinearLayout
{

    public interface ColorSelectCallback
    {
        void onColorSelected (@ColorInt int color);
    }

    private static final String TAG = "PaletteView";

    private LinkedList<AppCompatImageButton> buttons;

    @ColorInt
    private int color;

    private ColorSelectCallback listener;

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
        buttons = new LinkedList<>();

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

            // Cannot use RxBinding's subscription because I need the view to differentiate color.
            imageButton.setOnClickListener(this::onClick);

            buttons.add(imageButton);
            addView(imageButton);
        }

        color = (Integer) buttons.getFirst().getTag();
        buttons.getFirst().setImageResource(android.R.drawable.ic_menu_view);

        attributes.recycle();
    }

    public void setColorSelectListener (
        @NonNull ColorSelectCallback callbackListener
    ) {
        this.listener = callbackListener;
    }

    private void onClick (
        @NonNull View view
    ) {
        AppCompatImageButton button = (AppCompatImageButton) view;

        for (AppCompatImageButton b : buttons)
        {
            b.setImageDrawable(null);
        }

        button.setImageResource(android.R.drawable.ic_menu_view);

        color = (int) button.getTag();

        if (listener != null)
        {
            listener.onColorSelected(color);
        }
    }

}
