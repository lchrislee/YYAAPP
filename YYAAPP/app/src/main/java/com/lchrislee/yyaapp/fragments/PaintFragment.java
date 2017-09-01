package com.lchrislee.yyaapp.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lchrislee.yyaapp.R;
import com.lchrislee.yyaapp.presenters.PaintPresenter;
import com.lchrislee.yyaapp.utilities.ImageIO;
import com.lchrislee.yyaapp.views.BrushSizeView;
import com.lchrislee.yyaapp.views.PaletteView;
import com.lchrislee.yyaapp.views.canvas.CanvasView;

import static android.app.Activity.RESULT_OK;

public class PaintFragment extends Fragment implements
    PaintPresenter.SaveFinishedCallback
{

    private static final String TAG = "PaintFragment";
    private static final int REQUEST_IMAGE = 200;

    private PaintPresenter presenter;

    public PaintFragment ()
    {
        // Required empty public constructor
    }

    public static
    @NonNull
    PaintFragment newInstance()
    {
        PaintFragment fragment = new PaintFragment();
        Bundle arguments = new Bundle();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        final View holder = inflater.inflate(R.layout.fragment_paint, container, false);

        final CanvasView canvas = holder.findViewById(R.id.fragment_paint_canvas);
        final PaletteView palette = holder.findViewById(R.id.fragment_paint_palette);
        final BrushSizeView brush = holder.findViewById(R.id.fragment_paint_stroke_size);

        presenter = new PaintPresenter(canvas, palette, brush);
        presenter.setCallbackListener(this);

        setHasOptionsMenu(true);

        return holder;
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_paint, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.menu_paint_undo:
                presenter.undoChange();
                break;
            case R.id.menu_paint_redo:
                presenter.redoChange();
                break;
            case R.id.menu_paint_save:
                if (detectInvalidPermissions())
                {
                    break;
                }
                displaySaveDialog();
                break;
            case R.id.menu_paint_open:
                requestImages();
            case R.id.menu_paint_clear:
                presenter.clearCanvas();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
     * Opening
     */

    /*
     * This method is all new to me, I have not requested images from a content provider before.
     * This code is based off of:
     * https://stackoverflow.com/questions/5309190/android-pick-images-from-gallery
     */
    private void requestImages ()
    {
        Intent documentIntent = new Intent(Intent.ACTION_GET_CONTENT);
        documentIntent.setType("image/*");

        Intent imageIntent = new Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        );
        imageIntent.setType("image/*");

        String displayPrompt = getString(R.string.fragment_paint_open_prompt);

        Intent chooserIntent = Intent.createChooser(documentIntent, displayPrompt);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{imageIntent});

        startActivityForResult(chooserIntent, REQUEST_IMAGE);
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode)
        {
            case REQUEST_IMAGE:
                if (resultCode == RESULT_OK)
                {
                    if (data == null)
                    {
                        Toast.makeText(
                            getContext(),
                            R.string.fragment_paint_open_failure,
                            Toast.LENGTH_SHORT
                        ).show();
                        return;
                    }

                    Bitmap image = ImageIO.load(getContext().getContentResolver(), data.getData());
                    if (image == null)
                    {
                        Toast.makeText(
                            getContext(),
                            R.string.view_canvas_load_failure,
                            Toast.LENGTH_SHORT
                        ).show();
                        break;
                    }

                    presenter.openImage(image);
                }
                break;
            default:
                break;
        }
    }

    /*
     * Saving
     */

    @Override
    public void onRequestPermissionsResult (
        int requestCode,
        @NonNull String[] permissions,
        @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode)
        {
            case ImageIO.REQUEST_EXTERNAL:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    displaySaveDialog();
                }
                else
                {
                    Toast.makeText(
                        getContext(),
                        R.string.dialog_save_permission_failed,
                        Toast.LENGTH_SHORT
                    ).show();
                }
                break;
            default:
                break;
        }
    }

    private boolean detectInvalidPermissions ()
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        {
            return false;
        }

        int hasPermission = ContextCompat.checkSelfPermission(
            getContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        );

        if (hasPermission == PackageManager.PERMISSION_GRANTED)
        {
            return false;
        }

        requestPermissions(
            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
            ImageIO.REQUEST_EXTERNAL
        );
        return true;
    }

    private void displaySaveDialog()
    {
        DialogFragment fragment = presenter.saveDialog();
        fragment.show(getChildFragmentManager(), "SaveDialog");
    }

    @Override
    public void onSaveFinished (boolean isSuccessful)
    {
        int displayString = isSuccessful
            ? R.string.fragment_paint_save_success
            : R.string.fragment_paint_save_failure;

        Toast.makeText(
            getContext(),
            displayString,
            Toast.LENGTH_SHORT
        ).show();
    }
}
