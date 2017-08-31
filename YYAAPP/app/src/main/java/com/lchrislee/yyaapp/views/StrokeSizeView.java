package com.lchrislee.yyaapp.views;

import android.content.Context;
import android.support.annotation.Dimension;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.lchrislee.yyaapp.R;

public class StrokeSizeView extends LinearLayout implements SeekBar.OnSeekBarChangeListener
{

    public interface StrokeSizeChange
    {
        void OnStrokeChanged(@Dimension float size);
    }

    private static final String TAG = "StrokeSizeView";

    private StrokeSizeChange strokeChangeListener;

    private float size;

    public StrokeSizeView (Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        initialize();
    }

    private void initialize ()
    {
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        final View internalView = inflater.inflate(R.layout.view_stroke_size, this, true);

        final SeekBar strokeSize = internalView.findViewById(R.id.view_stroke_seek);
        strokeSize.setOnSeekBarChangeListener(this);
        size = getContext().getResources().getDimension(R.dimen.stroke_default);
    }

    @Override
    public void onProgressChanged (SeekBar seekBar, int progress, boolean fromUser)
    {
        size = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            (progress * 4f) + 4f,
            getResources().getDisplayMetrics()
        );

        if (strokeChangeListener != null)
        {
            strokeChangeListener.OnStrokeChanged(size);
        }
    }

    @Override
    public void onStartTrackingTouch (SeekBar seekBar)
    {

    }

    @Override
    public void onStopTrackingTouch (SeekBar seekBar)
    {

    }

    public void setStrokeChangeListener (StrokeSizeChange strokeChangeListener)
    {
        this.strokeChangeListener = strokeChangeListener;
    }

    public float strokeSize ()
    {
        return size;
    }
}
