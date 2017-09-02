package com.lchrislee.yyaapp.utilities;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.FileNotFoundException;

public class LoadImageTask extends AsyncTask<Void, Void, Bitmap>
{

    public interface LoadResultCallback
    {
        void onLoadResult(@Nullable Bitmap image);
    }

    private static final String TAG = "LoadImageTask";
    public static final int REQUEST_EXTERNAL = 100;

    private final ContentResolver resolver;
    private final Uri data;

    private LoadResultCallback listener;

    public LoadImageTask (ContentResolver resolver, Uri data)
    {
        this.resolver = resolver;
        this.data = data;
    }

    public void setLoadResultListener (LoadResultCallback listener)
    {
        this.listener = listener;
    }

    @Override
    protected @Nullable Bitmap doInBackground (Void... voids)
    {
        Bitmap output;
        try
        {
            output = BitmapFactory.decodeStream(resolver.openInputStream(data));
        } catch (FileNotFoundException e)
        {
            Log.e(TAG, "Could not open image from Uri.");
            return null;
        }

        if (output.getWidth() > output.getHeight())
        {
            Matrix rotate = new Matrix();
            rotate.postRotate(90);
            output = Bitmap.createBitmap(
                output,
                0,
                0, // Use bitmap from top left.
                output.getWidth(),
                output.getHeight(),
                rotate,
                true
            );
        }
        return output;
    }

    @Override
    protected void onPostExecute (@Nullable Bitmap bitmap)
    {
        super.onPostExecute(bitmap);
        if (listener != null)
        {
            listener.onLoadResult(bitmap);
        }
    }
}
