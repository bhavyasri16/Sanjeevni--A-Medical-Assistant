package com.finalproject.it.sanjeevni.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.finalproject.it.sanjeevni.R;

public class Confirm_dr_dialog extends AppCompatDialogFragment {

    private Confirm_dr_dialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        final LayoutInflater inflator= getActivity().getLayoutInflater();
        final View view= inflator.inflate(R.layout.dr_register_confirm,null);
        builder.setView(view);
        /*setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast toast=Toast.makeText(getContext(),"You Will Receive Confirmation Mail Shortly.",Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.getView().setAlpha(1);
                        toast.show();
                    }
                });*/
        Button yes=(Button) view.findViewById(R.id.yes_btn);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast=Toast.makeText(getContext(),"You Will Receive Confirmation Mail Shortly.",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.getView().setAlpha(1);
                toast.show();
                dismiss();
            }
        });
        Button no=(Button) view.findViewById(R.id.no_btn);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                dismiss();
            }
        });
        return builder.create();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (Confirm_dr_dialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement Comfirm_dr_dialogListener");
        }
    }

    public interface Confirm_dr_dialogListener{

    }

}
