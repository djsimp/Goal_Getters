package org.truthdefender.goalgetters.goals;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import org.truthdefender.goalgetters.R;
import org.truthdefender.goalgetters.model.SmartGoal;

public class CreateSmartFragment extends Fragment {
    private TextInputEditText actionText;
    private TextView amountText;
    private TextInputEditText unitText;
    private Button amountButton;

    public CreateSmartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_create_smart, container, false);

        actionText = (TextInputEditText)v.findViewById(R.id.edittext_action);
        unitText = (TextInputEditText)v.findViewById(R.id.edittext_units);
        amountText = (TextView)v.findViewById(R.id.amount);
        amountButton = (Button)v.findViewById(R.id.number_picker_button);
        amountButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                show();
            }
        });

        return v;
    }

    public String getAction() {
        return actionText.getText().toString();
    }

    public String getAmount() {
        return amountText.getText().toString();
    }

    public String getUnit() {
        return unitText.getText().toString();
    }

    public void show() {
        final Dialog npDialog = new Dialog(getContext());
        npDialog.setTitle("Set Amount");
        npDialog.setContentView(R.layout.numberpicker_layout);
        Button setBtn = (Button) npDialog.findViewById(R.id.setBtn);
        Button cnlBtn = (Button) npDialog.findViewById(R.id.CancelButton_NumberPicker);

        final NumberPicker numberPicker = (NumberPicker) npDialog.findViewById(R.id.numberPicker);
        numberPicker.setMaxValue(1000000);
        numberPicker.setMinValue(0);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // TODO Auto-generated method stub
            }
        });
        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = String.valueOf(numberPicker.getValue());
                amountText.setText(number);

                npDialog.dismiss();
            }
        });

        cnlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                npDialog.dismiss();
            }
        });

        npDialog.show();
    }
}
