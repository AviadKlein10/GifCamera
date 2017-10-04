package avivaviad.gifcamera.custom.dialog;

/**
 * Created by Aviad on 03/10/2017.
 */

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import avivaviad.gifcamera.R;

public class DialogFragmentDelete extends DialogFragment {
    Button btnYes,btnNo;
    static String DialogBoxTitle;
    private int position;

    public void setPosition(int position) {
        this.position = position;
    }

    public interface YesNoDialogListener {
        void onFinishYesNoDialog(boolean state, int position);
    }

    //---empty constructor required
    public DialogFragmentDelete(){

    }
    //---set the title of the dialog window---
    public void setDialogTitle(String title) {
        DialogBoxTitle= title;
    }

    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState ) {

        View view= inflater.inflate(R.layout.alert_dialog_delete, container);
        //---get the Button views---
        btnYes = (Button) view.findViewById(R.id.btnYes);
        btnNo = (Button) view.findViewById(R.id.btnNo);

        // Button listener
        btnYes.setOnClickListener(btnListener);
        btnNo.setOnClickListener(btnListener);

        //---set the title for the dialog
        getDialog().setTitle(DialogBoxTitle);

        return view;
    }

    //---create an anonymous class to act as a button click listener
    private OnClickListener btnListener = new OnClickListener()
    {
        public void onClick(View v)
        {
            //---gets the calling activity---
            YesNoDialogListener activity = (YesNoDialogListener) getActivity();
            boolean state =
                    ((Button) v).getText().toString().equals("כן") ? true : false;
            activity.onFinishYesNoDialog(state,position);
            //---dismiss the alert---
            dismiss();
        }
    };
}