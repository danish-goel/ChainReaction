package com.ai.chainreaction.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Danish on 17-Nov-15.
 */
public class Main extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button human = (Button) findViewById(R.id.humanGameButton);
        human.setOnClickListener(this);
        Button computer = (Button) findViewById(R.id.computerGameButton);
        computer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.computerGameButton) {
            Intent i=new Intent(this,ComputerVsComputer.class);
            startActivity(i);
        }
    }
}
