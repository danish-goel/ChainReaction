package com.ai.chainreaction.android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
                num_rows.setText(progressChanged+"");
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
                num_columns.setText(progressChanged + "");
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

    public void sendFile(View v)
    {
        String fileName="myFile.txt";
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),fileName);
        Log.d("asdasd",file.getAbsolutePath()+"");
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        startActivity(intent);
//        File fileTo = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName);
//        try {
//            copy(file, fileTo);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

}
