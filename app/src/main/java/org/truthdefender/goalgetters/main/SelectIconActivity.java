package org.truthdefender.goalgetters.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import org.truthdefender.goalgetters.R;

/**
 * Created by dj on 10/20/17.
 */

public class SelectIconActivity extends AppCompatActivity {

    private Integer[] mThumbIds = {
            R.drawable.african, R.drawable.afro, R.drawable.asian, R.drawable.asian_1,
            R.drawable.avatar, R.drawable.avatar_1, R.drawable.avatar_2, R.drawable.avatar_3,
            R.drawable.bellboy, R.drawable.bellgirl, R.drawable.chicken, R.drawable.cooker,
            R.drawable.cooker_1, R.drawable.diver, R.drawable.diver_1, R.drawable.doctor,
            R.drawable.doctor_1, R.drawable.farmer, R.drawable.firefighter, R.drawable.firefighter_1,
            R.drawable.florist, R.drawable.florist_1, R.drawable.gentleman, R.drawable.hindu,
            R.drawable.hindu_1, R.drawable.hipster, R.drawable.horse, R.drawable.jew,
            R.drawable.man, R.drawable.man_1, R.drawable.mechanic, R.drawable.mechanic_1,
            R.drawable.monk, R.drawable.musician, R.drawable.musician_1, R.drawable.muslim,
            R.drawable.muslim_1, R.drawable.nerd, R.drawable.nerd_1, R.drawable.ninja,
            R.drawable.nun, R.drawable.nurse, R.drawable.nurse_1, R.drawable.photographer,
            R.drawable.pilot, R.drawable.policeman, R.drawable.policewoman, R.drawable.priest,
            R.drawable.rapper, R.drawable.rapper_1, R.drawable.selector, R.drawable.stewardess,
            R.drawable.surgeon, R.drawable.surgeon_1, R.drawable.telemarketer, R.drawable.telemarketer_1,
            R.drawable.waiter, R.drawable.waitress, R.drawable.woman, R.drawable.woman_1,
            R.drawable.woman_2
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_icon);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent intent = new Intent(SelectIconActivity.this, CreateAccountActivity.class);
                intent.putExtra("imageTag", mThumbIds[position]);
                startActivity(intent);
                finish();
            }
        });

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_select_icon);
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

    private class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return mThumbIds.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(120, 120));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageResource(mThumbIds[position]);
            return imageView;
        }
    }
}


