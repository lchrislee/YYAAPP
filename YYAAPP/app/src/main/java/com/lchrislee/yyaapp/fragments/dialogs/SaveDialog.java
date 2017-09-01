package com.lchrislee.yyaapp.fragments.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lchrislee.yyaapp.R;

public class SaveDialog extends DialogFragment
{

    public interface SaveDetailsFinalized
    {
        void OnSave(@NonNull String fileName);
    }

    private EditText name;

    private SaveDetailsFinalized saveListener;

    public static
    @NonNull
    SaveDialog newInstance()
    {
        SaveDialog dialogFragment = new SaveDialog();
        Bundle arguments = new Bundle();
        dialogFragment.setArguments(arguments);
        return dialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog (Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(R.string.dialog_save_title);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Null is valid because builder will claim the dialog as parent for inflated view.
        final View internalView = inflater.inflate(R.layout.dialog_save, null);

        name = internalView.findViewById(R.id.dialog_save_name_input);

        builder.setView(internalView);

        builder.setPositiveButton(
            R.string.dialog_save_positive,
            new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick (DialogInterface dialogInterface, int i)
                {
                    String fileName = name.getText().toString();

                    if (!validate(fileName))
                    {
                        Toast.makeText(
                            getContext(),
                            R.string.dialog_save_invalid_name,
                            Toast.LENGTH_SHORT
                        ).show();
                        return;
                    }

                    if (saveListener != null)
                    {
                        saveListener.OnSave(fileName);
                        SaveDialog.this.getDialog().dismiss();
                    }
                }
            });

        builder.setNeutralButton(
            R.string.dialog_save_cancel,
            new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick (DialogInterface dialogInterface, int i)
                {
                    SaveDialog.this.getDialog().dismiss();
                }
            });

        return builder.create();
    }

    private boolean validate(@NonNull String name)
    {
        if (name.length() == 0)
        {
            return false;
        }

        for (int i = 0; i < name.length(); ++i)
        {
            if (!Character.isLetterOrDigit(name.charAt(i)))
            {
                return false;
            }
        }

        return true;
    }

    public void setSaveListener (@NonNull SaveDetailsFinalized saveListener)
    {
        this.saveListener = saveListener;
    }
}
