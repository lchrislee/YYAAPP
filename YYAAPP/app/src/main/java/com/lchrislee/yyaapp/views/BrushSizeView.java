package com.lchrislee.yyaapp.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Dimension;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import com.jakewharton.rxbinding2.widget.RxSeekBar;
import com.lchrislee.yyaapp.R;

public class BrushSizeView extends FrameLayout
{

    public interface SizeSelectCallback
    {
        void onSizeChanged (@Dimension float size);
    }

    private static final String TAG = "StrokeSizeView";

    private SizeSelectCallback listener;

    private int stepMultiplier;

    public BrushSizeView (Context context)
    {
        super(context);
        initialize(null);
    }

    public BrushSizeView (Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        initialize(attrs);
    }

    public BrushSizeView (Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initialize(attrs);
    }

    private void initialize (
        @Nullable AttributeSet attrs
    ) {
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        final View internalView = inflater.inflate(R.layout.view_brush, this, true);

        final SeekBar brushSize = internalView.findViewById(R.id.view_stroke_seek);

        RxSeekBar.changes(brushSize).subscribe(this::onProgress);

        // 0 designates no default style listed in theme but useImage the default style.
        final TypedArray viewAttributes = getContext().getTheme().obtainStyledAttributes(
            attrs,
            R.styleable.BrushSizeView,
            0,
            R.style.BrushViewDefaultStyle
        );

        final int stepCount = viewAttributes.getInt(
            R.styleable.BrushSizeView_step_count,
            getResources().getInteger(R.integer.seek_brush_step_count_default)
        );
        brushSize.setMax(stepCount);

        stepMultiplier = viewAttributes.getInt(
            R.styleable.BrushSizeView_step_multiplier,
            getResources().getInteger(R.integer.seek_brush_step_multiplier_default)
        );

        final int stepProgress = viewAttributes.getInt(
            R.styleable.BrushSizeView_step_progress,
            getResources().getInteger(R.integer.seek_brush_progress_default)
        );
        brushSize.setProgress(stepProgress);

        viewAttributes.recycle();
    }

    public void setSizeListener (
        @NonNull SizeSelectCallback callbackListener
    ) {
        this.listener = callbackListener;
    }

    private void onProgress(
        int progress
    ) {
        float size = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            (progress * stepMultiplier) + stepMultiplier,
            getResources().getDisplayMetrics()
        );

        if (listener != null)
        {
            listener.onSizeChanged(size);
        }
    }

}
