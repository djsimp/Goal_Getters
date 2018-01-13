package org.truthdefender.goalgetters.goals;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.truthdefender.goalgetters.R;
import org.truthdefender.goalgetters.model.SmartGoal;

import java.util.Date;
import java.util.HashMap;

public class CreateHabitFragment extends Fragment {
    private TextInputEditText habitText;
    private Spinner freqSpinner;
    private GridView freqGrid;
    private String[] dailyArray;
    private String[] monthlyArray;
    private String[] hourlyArray;
    private HashMap<String, Boolean> freqMap;
    private TextView targetPercent;
    private SeekBar targetSeekBar;

    public CreateHabitFragment() {
        dailyArray = new String[] { "Su", "M", "T", "W", "Th", "F", "Sa" };
        monthlyArray = new String[] { "1", "2", "3", "4", "5", "6", "7",
                "8", "9", "10", "11", "12", "13", "14",
                "15", "16", "17", "18", "19", "20", "21",
                "22", "23", "24", "25", "26", "27", "28",
                "29", "30", "31" };
        hourlyArray = new String[] { "1", "2", "3", "4", "5", "6", "7",
                "8", "9", "10", "11", "12", "13", "14",
                "15", "16", "17", "18", "19", "20", "21",
                "22", "23", "24" };
        freqMap = new HashMap<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_create_habit, container, false);

        habitText = v.findViewById(R.id.edittext_habit);
        freqSpinner = v.findViewById(R.id.freq_type_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.freq_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        freqSpinner.setAdapter(adapter);


        freqGrid = v.findViewById(R.id.freq_calendar);
        freqGrid.setAdapter(new FrequencyAdapter(getActivity(), dailyArray));

        freqSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch(adapterView.getItemAtPosition(i).toString()) {
                    case "Monthly":
                        freqMap = new HashMap<>();
                        freqGrid.setAdapter(new FrequencyAdapter(getActivity(), monthlyArray));
                        break;
                    case "Hourly":
                        freqMap = new HashMap<>();
                        freqGrid.setAdapter(new FrequencyAdapter(getActivity(), hourlyArray));
                        break;
                    default:
                        freqMap = new HashMap<>();
                        freqGrid.setAdapter(new FrequencyAdapter(getActivity(), dailyArray));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        targetPercent = v.findViewById(R.id.target_percent);
        targetSeekBar = v.findViewById(R.id.target_seek_bar);
        targetSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                targetPercent.setText(i + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return v;
    }

    public String getDescription() {
        return habitText.getText().toString();
    }

    public String getFrequencyType() {
        return freqSpinner.getSelectedItem().toString();
    }

    public HashMap<String, Boolean> getFrequencyMap() {
        return freqMap;
    }

    public float getSeekBarValue() {
        return targetSeekBar.getProgress();
    }
//
//    public void show() {
//        final Dialog npDialog = new Dialog(getContext());
//        npDialog.setTitle("Set Amount");
//        npDialog.setContentView(R.layout.numberpicker_layout);
//        Button setBtn = (Button) npDialog.findViewById(R.id.setBtn);
//        Button cnlBtn = (Button) npDialog.findViewById(R.id.CancelButton_NumberPicker);
//
//        final NumberPicker numberPicker = (NumberPicker) npDialog.findViewById(R.id.numberPicker);
//        numberPicker.setMaxValue(1000000);
//        numberPicker.setMinValue(0);
//        numberPicker.setWrapSelectorWheel(false);
//        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//            @Override
//            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//                // TODO Auto-generated method stub
//            }
//        });
//        setBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String number = String.valueOf(numberPicker.getValue());
//                amountText.setText(number);
//
//                npDialog.dismiss();
//            }
//        });
//
//        cnlBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                npDialog.dismiss();
//            }
//        });
//
//        npDialog.show();
//    }

    public class FrequencyAdapter extends BaseAdapter {
        private Context mContext;
        private String[] mOptions;

        public FrequencyAdapter(Context context, String[] options){
            this.mContext = context;
            this.mOptions = options;
        }

        @Override
        public int getCount() {
            return mOptions.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View gridView;

            if (convertView == null) {
                // get layout from xml file
                gridView = inflater.inflate(R.layout.layout_freq_item, null);


                // pull views
                TextView letterView = gridView.findViewById(R.id.freq_item_text);

                letterView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(freqMap.containsKey(((TextView)view).getText().toString())) {
                            freqMap.remove(((TextView)view).getText().toString());
                            view.setBackgroundResource(R.drawable.freq_item_unselected);
                            ((TextView)view).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        } else {
                            freqMap.put(((TextView)view).getText().toString(), true);
                            view.setBackgroundResource(R.drawable.freq_item_selected);
                            ((TextView)view).setTextColor(getResources().getColor(R.color.textColorWhite));
                        }
                    }
                });

                // set values into views
                letterView.setText(mOptions[position]);  // using dummy data for now

                letterView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            } else {
                gridView = convertView;
            }

            return gridView;
        }
    }
}
