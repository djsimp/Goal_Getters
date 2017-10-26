package org.truthdefender.goalgetters.goals;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.NumberPicker;

import org.truthdefender.goalgetters.R;

/**
 * Created by dj on 10/18/17.
 */

public class ReportProgressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_progress_layout);
        NumberPicker np =(NumberPicker) findViewById(R.id.number_picker);

        np.setMinValue(0);
        np.setMaxValue(1000000);
        np.setWrapSelectorWheel(false);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_report_progress);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}