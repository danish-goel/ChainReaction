package com.ai.chainreaction.android;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by danishgoel on 12/1/15.
 */
public class Settings extends Activity
{
    private SeekBar row;
    private SeekBar column;
    TextView num_rows,num_columns;
    int row_count,column_count;
    public final String PREFS_NAME = "MyPrefsFile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        row = (SeekBar) findViewById(R.id.row_change);
        column = (SeekBar) findViewById(R.id.column_change);

        num_rows=(TextView)findViewById(R.id.num_rows);
        num_columns=(TextView)findViewById(R.id.num_columns);

        row.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChanged = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                num_rows.setText(progressChanged+"");
                row_count=progressChanged;
                setvalues("rows", row_count);
            }
        });

        column.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChanged = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                num_columns.setText(progressChanged + "");
                column_count=progressChanged;
                setvalues("columns",column_count);
            }
        });

    }


    void setvalues(String key,int value) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("key",value);
        // Commit the edits!
        editor.commit();
    }

}
