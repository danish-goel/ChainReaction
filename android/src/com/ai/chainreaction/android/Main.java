package com.ai.chainreaction.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ai.chainreaction.Simulations.Simulate;

/**
 * Created by Danish on 17-Nov-15.
 */
public class Main extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button human = (Button) findViewById(R.id.settings);
        human.setOnClickListener(this);
        Button computer = (Button) findViewById(R.id.startGame);
        computer.setOnClickListener(this);
        Simulate s = new Simulate();
        s.main(null);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.startGame) {
            Intent i=new Intent(this,ComputerVsComputer.class);
            startActivity(i);
        }
        else if(v.getId()==R.id.settings)
        {
            Intent i=new Intent(this,Settings.class);
            startActivity(i);
        }
    }
}
