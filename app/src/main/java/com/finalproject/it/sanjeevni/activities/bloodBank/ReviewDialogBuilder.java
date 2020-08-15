package com.finalproject.it.sanjeevni.activities.bloodBank;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.finalproject.it.sanjeevni.R;

public class ReviewDialogBuilder extends AlertDialog.Builder {

    private RadioGroup yes_no;
    private RadioButton selected_optionBtn,yesBtn;
    private EditText review;
    private Button done;
    private String selectedOption,reviewText;
    final private View customView;


    public ReviewDialogBuilder(Context context ){
        super(context);

        customView=View.inflate(context,
                R.layout.layout,null);
        setView(customView);
        review=customView.findViewById(R.id.review);
        yes_no=customView.findViewById(R.id.yes_no_group);
        yesBtn=customView.findViewById(R.id.yes_btn);
        yesBtn.setChecked(true);
        setTitle("Just One Step...");


    }

    public String getSelectedOption(){
        selected_optionBtn=customView.findViewById(yes_no.getCheckedRadioButtonId());
        selectedOption=selected_optionBtn.getText().toString();
        return selectedOption;
    }

    public  String getReviewText(){
        reviewText=review.getEditableText().toString().trim();
        return  this.reviewText;
    }

}
