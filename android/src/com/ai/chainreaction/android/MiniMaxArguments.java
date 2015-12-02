package com.ai.chainreaction.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by danishgoel on 12/2/15.
 */
public class MiniMaxArguments extends Activity implements View.OnClickListener
{
    static int firstdepth=2;
    static int firstheuristics=0;
    static boolean firstpruning=true;
    static int seconddepth=2;
    static int secondheuristics=0;
    static boolean secondpruning=true;
    TextView depthView;
    Button done;
    int side;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.minimax_arguments);

        Intent in = getIntent();
        Bundle b = in.getExtras();
        side = b.getInt("side");

        CheckBox chkIos = (CheckBox) findViewById(R.id.pruning);
        depthView=(TextView)findViewById(R.id.depth_view);
        done=(Button)findViewById(R.id.minimaxargs);
        done.setOnClickListener(this);

        chkIos.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    if(side==0) {
                        firstpruning = true;
                    }
                    else {
                        secondpruning = true;
                    }
                } else {
                    if(side==0) {
                        firstpruning = false;
                    }
                    else
                    {
                        secondpruning = false;
                    }
                }

            }
        });

        SeekBar depth = (SeekBar) findViewById(R.id.depth);

        depth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChanged = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                depthView.setText(progressChanged+"");
                if (side == 0) {
                    MiniMaxArguments.this.firstdepth = progressChanged;
                } else {
                    MiniMaxArguments.this.seconddepth = progressChanged;
                }
            }
        });

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                RadioButton radioButton = (RadioButton) findViewById(checkedId);
                String heuris = radioButton.getText().toString();
                if (heuris.equalsIgnoreCase("piecewise")) {
                    if (side == 0) {
                        firstheuristics = 0;
                    }
                    else
                    {
                        secondheuristics = 0;
                    }
                } else if (heuris.equalsIgnoreCase("chains")) {
                    if (side == 0) {
                        firstheuristics = 1;
                    }
                    else
                    {
                        secondheuristics=1;
                    }
                }

            }
        });


    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.minimaxargs)
        {
            finish();
        }

    }
}
