package com.lchrislee.yyaapp.utilities;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageIO
{
    private static final String ALBUM_NAME = "YYAAPP";
    private static final String TAG = "ImageIO";
    private static final String IMAGE_TYPE = ".png";
    public static final int REQUEST_EXTERNAL = 100;

    public static boolean save (
        @NonNull String fileName,
        @NonNull Bitmap image
    ) {
        if (checkExternalNotWritable())
        {
            return false;
        }

        File directory = getAlbumDirectory();
        if (directory == null)
        {
            return false;
        }

        StringBuilder fullName = new StringBuilder()
            .append(directory.getPath())
            .append(File.separator)
            .append(fileName)
            .append(IMAGE_TYPE);

        File imageFile = new File(fullName.toString());

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

    private static boolean checkExternalNotWritable ()
    {
        return !Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    private static
    @Nullable
    File getAlbumDirectory ()
    {
        File albumDirectory = new File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            ALBUM_NAME
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

    public static @Nullable Bitmap load (
        @NonNull ContentResolver resolver,
        @NonNull Uri data
    ) {
        Bitmap output = null;
        try
        {
            output = BitmapFactory.decodeStream(resolver.openInputStream(data));
        } catch (FileNotFoundException e)
        {
            Log.e(TAG, "Could not open image from Uri.");
        }
        return output;
    }
}
