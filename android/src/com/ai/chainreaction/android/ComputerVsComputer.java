package com.ai.chainreaction.android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by danishgoel on 11/28/15.
 */
public class ComputerVsComputer extends Activity implements View.OnClickListener {

    public final String PREFS_NAME = "MyPrefsFile";
    int firstChoice = 0;
    int secondChoice = 0;
    TextView firstPlayer;
    TextView secondPlayer;
    String[] values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.computer_vs_computer);

        final ListView firstList = (ListView) findViewById(R.id.firstList);
        final ListView secondList = (ListView) findViewById(R.id.secondList);


        firstPlayer=(TextView)findViewById(R.id.player1);
        secondPlayer=(TextView)findViewById(R.id.player2);
        Button done = (Button) findViewById(R.id.done);
        done.setOnClickListener(this);

    values = new String[]{
            "Human",
            "Random",
                "MiniMax",
                "Greedy"
        };

        ArrayAdapter<String> firstAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        ArrayAdapter<String> secondAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        // Assign adapter to ListView
        firstList.setAdapter(firstAdapter);
        secondList.setAdapter(secondAdapter);
        firstList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        secondList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // ListView Item Click Listener
        firstList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                firstChoice = position;
                firstPlayer.setText(values[position]);

            }

        });

        // ListView Item Click Listener
        secondList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                secondChoice = position;
                secondPlayer.setText(values[position]);


            }

        });


    }

    public void setAlgoChoice(int choice, String selectedAlgoName) {
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        if(choice==1)
        {
            editor.putString("firstAlgoName", selectedAlgoName);
        }
        else if (choice==2)
        {
            editor.putString("secondAlgoName", selectedAlgoName);
        }
        // Commit the edits!
        editor.commit();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.done) {
            Log.d("ab",values[firstChoice]);
            Log.d("ab",values[secondChoice]);
            setAlgoChoice(1,values[firstChoice]);
            setAlgoChoice(2,values[secondChoice]);
            Intent i = new Intent(this, GameScreen.class);
            startActivity(i);
        }
    }
}
