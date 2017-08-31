package com.lchrislee.yyaapp.views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.Dimension;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.lchrislee.yyaapp.R;

public class BrushSizeView extends LinearLayout implements SeekBar.OnSeekBarChangeListener
{

    public interface StrokeSizeChange
    {
        void OnStrokeChanged(@Dimension float size);
    }

    private static final String TAG = "StrokeSizeView";

    private StrokeSizeChange strokeChangeListener;

    private float size;
    private int stepMultiplier;

    public BrushSizeView (Context context)
    {
        super(context);
        initialize(null);
    }

    public BrushSizeView (Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initialize(attrs);
    }

    public BrushSizeView (Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        initialize(attrs);
    }

    private void initialize (
        @Nullable AttributeSet attrs
    ) {
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        final View internalView = inflater.inflate(R.layout.view_brush, this, true);

        final SeekBar brushSize = internalView.findViewById(R.id.view_stroke_seek);
        brushSize.setOnSeekBarChangeListener(this);

        final Context context = getContext();
        final Resources resources = context.getResources();

        // 0 designates no default style listed in theme but use the default style.
        TypedArray viewAttributes = context.getTheme().obtainStyledAttributes(
            attrs,
            R.styleable.BrushSizeView,
            0,
            R.style.BrushViewDefaultStyle
        );

        final int stepCount = viewAttributes.getInt(
            R.styleable.BrushSizeView_step_count,
            resources.getInteger(R.integer.stroke_seek_step_count_default)
        );
        brushSize.setMax(stepCount);

        stepMultiplier = viewAttributes.getInt(
            R.styleable.BrushSizeView_step_multiplier,
            resources.getInteger(R.integer.stroke_seek_step_multiplier_default)
        );

        final int stepProgress = viewAttributes.getInt(
            R.styleable.BrushSizeView_step_progress,
            resources.getInteger(R.integer.stroke_seek_progress_default)
        );
        brushSize.setProgress(stepProgress);
    }

    @Override
    public void onProgressChanged (SeekBar seekBar, int progress, boolean fromUser)
    {
        size = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            (progress * stepMultiplier) + stepMultiplier,
            getResources().getDisplayMetrics()
        );

        if (strokeChangeListener != null)
        {
            strokeChangeListener.OnStrokeChanged(size);
        }
    }

    @Override
    public void onStartTrackingTouch (SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch (SeekBar seekBar) {}

    public void setStrokeChangeListener (StrokeSizeChange strokeChangeListener)
    {
        this.strokeChangeListener = strokeChangeListener;
    }

    public float strokeSize ()
    {
        return size;
    }
}