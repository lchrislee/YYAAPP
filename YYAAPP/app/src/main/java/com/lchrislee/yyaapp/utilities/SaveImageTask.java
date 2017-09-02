package com.lchrislee.yyaapp.utilities;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveImageTask extends AsyncTask<Void, Void, Boolean>
{

    public interface SaveResultCallback
    {
        void onSaveResult(boolean isSuccessful);
    }

    private static final String TAG = "SaveImageTask";
    private static final String IMAGE_TYPE = ".png";

    private final String imageName;
    private final Bitmap image;
    private final String appName;

    private SaveResultCallback listener;

    public SaveImageTask (
        @NonNull String appName,
        @NonNull String imageName,
        @NonNull Bitmap image
    ) {
        this.imageName = imageName;
        this.image = image;
        this.appName = appName;
    }

    public void setSaveResultListener (SaveResultCallback listener)
    {
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground (Void... voids)
    {
        if (checkExternalNotWritable())
        {
            return false;
        }

        File directory = albumDirectory(appName);
        if (directory == null)
        {
            return false;
        }

        StringBuilder fileName = new StringBuilder()
            .append(directory.getPath())
            .append(File.separator)
            .append(imageName)
            .append(IMAGE_TYPE);

        File imageFile = new File(fileName.toString());

        FileOutputStream outputStream;
        try
        {
            outputStream = new FileOutputStream(imageFile);
            image.compress(Bitmap.CompressFormat.PNG, 100, outputStream); // Bitmap is lossless.
            outputStream.close();
        }
        catch (FileNotFoundException e)
        {
            Log.e(TAG, "Could not save file.");
            return false;
        }
        catch (IOException e)
        {
            Log.e(TAG, "Could not close file stream.");
            return false;
        }

        return true;
    }

    @Override
    protected void onPostExecute (Boolean aBoolean)
    {
        super.onPostExecute(aBoolean);

        if (listener != null)
        {
            listener.onSaveResult(aBoolean);
        }
    }

    private static boolean checkExternalNotWritable ()
    {
        return !Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    private static
    @Nullable
    File albumDirectory (
        @NonNull String appName
    ) {
        File albumDirectory = new File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            appName
        );

        if (!albumDirectory.exists())
        {
            if (!albumDirectory.mkdirs())
            {
                return null;
            }
        }

        return albumDirectory;
    }
}
