package com.lchrislee.yyaapp.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.lchrislee.yyaapp.R;
import com.lchrislee.yyaapp.fragments.PaintFragment;

public class PaintActivity extends AppCompatActivity
{

    private static final String TAG = "PaintActivity";

    private PaintFragment paintFragment;

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
            actionBar.setTitle(R.string.app_name);
            actionBar.setDisplayShowTitleEnabled(true);
        }

        paintFragment = PaintFragment.newInstance();

        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.activity_paint_frame, paintFragment)
        .commit();
    }

}
