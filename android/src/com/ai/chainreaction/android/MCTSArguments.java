package com.ai.chainreaction.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by danishgoel on 12/2/15.
 */
public class MCTSArguments extends Activity implements View.OnClickListener{

    static int firstIterion=100;
    static int secondIteration=100;
    EditText iterations;
    int side;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mcts_arguments);

        Intent in = getIntent();
        Bundle b = in.getExtras();
        side = b.getInt("side");

 iterations=(EditText)findViewById(R.id.iterations);
        Button done=(Button)findViewById(R.id.done_mcts_arg);
        done.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.done_mcts_arg)
        {
            if(side==0) {
                firstIterion = Integer.parseInt(iterations.getText().toString());
            }
            else {
                secondIteration = Integer.parseInt(iterations.getText().toString());
            }
            finish();
        }

    }
}
