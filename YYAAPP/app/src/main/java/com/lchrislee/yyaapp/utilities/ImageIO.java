package com.lchrislee.yyaapp.utilities;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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
    private static final String TAG = "ImageIO";
    private static final String IMAGE_TYPE = ".png";
    public static final int REQUEST_EXTERNAL = 100;

    /*
     * Saving
     */

    public static boolean save (
        @NonNull String appName,
        @NonNull String imageName,
        @NonNull Bitmap image
    ) {
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

    /*
     * Opening
     */

    public static @Nullable Bitmap load (
        @NonNull ContentResolver resolver,
        @NonNull Uri data
    ) {
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
}
