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
        Button start = (Button) findViewById(R.id.startButton);
        start.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.startButton) {
            Intent i=new Intent(this,GameScreen.class);
            startActivity(i);
        }
    }
}
