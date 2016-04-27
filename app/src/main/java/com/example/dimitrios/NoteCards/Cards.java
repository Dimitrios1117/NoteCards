package com.example.dimitrios.NoteCards;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dimitrios.swipe_testing.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;


public class Cards extends AppCompatActivity {
    //GestureDetectorCompat gDetect;
    ArrayList<NoteCard> noteCards = new ArrayList<NoteCard>();
    int cardPosition = 0;
    Boolean modified = false;
    String question = "";
    String answer = "";
    String key = "";
    Button q_button, a_button, previous_button, next_button,delete_button, modify_button, add_button;
    SharedPreferences mSettings;
    SharedPreferences.Editor mEditor;
    GsonBuilder gsonb;
    private Gson mGson;
    HashMap<String, ArrayList<NoteCard>> cards_collection = new HashMap<String, ArrayList<NoteCard>>();
    //SparseArray<ArrayList<NoteCard>> cards_collection = new SparseArray<>();
    //NoteCard asd = new NoteCard("asd","asd");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //cardPosition = 0;
        mSettings = getSharedPreferences("my_cards_test", Context.MODE_PRIVATE);
        mEditor = mSettings.edit();
        gsonb = new GsonBuilder();
        mGson = gsonb.create();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);


        q_button = (Button) findViewById(R.id.Question);
        a_button = (Button) findViewById(R.id.button2);
        next_button = (Button)findViewById(R.id.n_button);
        previous_button = (Button)findViewById(R.id.p_button);
        delete_button = (Button)findViewById(R.id.delete_card);
        modify_button = (Button)findViewById(R.id.Modify_card);
        add_button = (Button)findViewById(R.id.add_card);
        Bundle extras = getIntent().getExtras();

        key = extras.getString("key");
        //NoteCard temp = new NoteCard("asd","ddddddd");
        //noteCards.add(temp);
        getstarted("my_cards_test");


        q_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (a_button.getVisibility() == View.VISIBLE) {
                    a_button.setVisibility(View.GONE);
                } else {
                    a_button.setVisibility(View.VISIBLE);
                }
            }
        });

        a_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(a_button.getVisibility() == View.VISIBLE){
                    a_button.setVisibility(View.GONE);
                }
                else{
                    a_button.setVisibility(View.VISIBLE);
                }
            }
        });

        next_button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(!noteCards.isEmpty()){
                    if(a_button.getVisibility() == View.VISIBLE){
                        a_button.setVisibility(View.GONE);
                    }
                    cardPosition++;

                    if(noteCards.size() -1 < cardPosition){
                        cardPosition = 0;
                    }
                    q_button.setText(noteCards.get(cardPosition).getQuestion());
                    a_button.setText(noteCards.get(cardPosition).getAnswer());
                }
            }
        });

        previous_button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(!noteCards.isEmpty()){
                    if(a_button.getVisibility() == View.VISIBLE){
                        a_button.setVisibility(View.GONE);
                    }
                    cardPosition--;
                    if(cardPosition < 0){
                        cardPosition = noteCards.size()-1;
                    }
                    q_button.setText(noteCards.get(cardPosition).getQuestion());
                    a_button.setText(noteCards.get(cardPosition).getAnswer());
                }

            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!noteCards.isEmpty()){
                    buttondialog();
                }

            }
        });

        modify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(noteCards.size() != 0){
                    //modify_button.setVisibility(View.VISIBLE);
                    Intent i = new Intent(getBaseContext(), Add_Card_Activity.class);
                    //i.putExtra("noteCards", noteCards);
                    //i.putExtra("position",cardPosition);
                    i.putExtra("question", noteCards.get(cardPosition).getQuestion());
                    i.putExtra("answer", noteCards.get(cardPosition).getAnswer());
                    i.putExtra("modify",true);
                    startActivityForResult(i,0);
                }
            }
        });
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), Add_Card_Activity.class);
                //i.putExtra("noteCards", noteCards);
                //i.putExtra("position",cardPosition);
                i.putExtra("modify",false);
                startActivityForResult(i,0);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Retrieve data in the intent

        //Bundle extras = getIntent().getExtras();

        if(resultCode == RESULT_OK && requestCode == 0){
            Bundle extras = data.getExtras();
            if(extras != null){
                //noteCards = (ArrayList<NoteCard>)getIntent().getSerializableExtra("noteCards");
                question = extras.getString("question");
                answer = extras.getString("answer");
                modified = extras.getBoolean("modify");

                if(modified){
                    noteCards.get(cardPosition).setQuestion(question);
                    noteCards.get(cardPosition).setAnswer(answer);
                    save("my_cards_test");
                    q_button.setText(noteCards.get(cardPosition).getQuestion());
                    a_button.setText(noteCards.get(cardPosition).getAnswer());
                }
                else{
                    NoteCard temp = new NoteCard(question,answer);
                    noteCards.add(temp);
                    save("my_cards_test");
                    q_button.setText(noteCards.get(cardPosition).getQuestion());
                    a_button.setText(noteCards.get(cardPosition).getAnswer());
                }
            }

        }

    }

    private void getstarted(String setting_name){
        load(setting_name);

        if(noteCards.isEmpty()){
            Toast.makeText(this, "No previous cards found.", Toast.LENGTH_LONG).show();
            q_button.setText(getResources().getString(R.string.default_question));
            a_button.setText(getResources().getString(R.string.default_answer));
        }
        else{
            q_button.setText(noteCards.get(cardPosition).getQuestion());
            a_button.setText(noteCards.get(cardPosition).getAnswer());

        }
    }

    private void save(String setting_name){
        //String writearray = mGson.toJson(noteCards);
        cards_collection.put(key,noteCards);
        String writearray = mGson.toJson(cards_collection);
        mEditor.putString(setting_name, writearray);
        mEditor.commit();

    }

    private void load(String setting_name){
        /*String loadValue = mSettings.getString(setting_name, "");
        Type type = new TypeToken<List<NoteCard>>(){}.getType();
        noteCards = mGson.fromJson(loadValue,type);*/
        String loadValue = mSettings.getString(setting_name, "");
        Type type = new TypeToken<HashMap<String,ArrayList<NoteCard>>>(){}.getType();
        cards_collection = mGson.fromJson(loadValue,type);
        noteCards = cards_collection.get(key);

    }

    private void buttondialog(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Are you Sure you want to delete card?.");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        noteCards.remove(cardPosition);
                        modified = true;
                        cardPosition = 0;

                        if(noteCards.size() >= 1){
                            q_button.setText(noteCards.get(cardPosition).getQuestion());
                            a_button.setText(noteCards.get(cardPosition).getAnswer());
                            //a_button.setVisibility(View.GONE);
                            save("my_cards_test");
                        }
                        else{
                            q_button.setText(getResources().getString(R.string.default_question));
                            a_button.setText(getResources().getString(R.string.default_answer));
                            save("my_cards_test");
                        }

                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

}