package com.example.dimitrios.NoteCards;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dimitrios.swipe_testing.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class Create_Category extends AppCompatActivity {
    SharedPreferences mSettings;
    SharedPreferences.Editor mEditor;
    GsonBuilder gsonb;
    private Gson mGson;
    private String setting_name = "my_cards_test";
    String my_key, old_key;
    HashMap<String, ArrayList<NoteCard>> mCards_collection;//= new HashMap<String, ArrayList<NoteCard>>();
    Button create_b;
    ArrayList<NoteCard> noteCards;
    ArrayList<NoteCard> cOLD;
    EditText my_et;
    ArrayList<String> keys = new ArrayList<String>();
    boolean modify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__category);
        final Bundle extras = getIntent().getExtras();
        noteCards = new ArrayList<NoteCard>();
        //noteCards.add(turkey);
        mSettings = getSharedPreferences("my_cards_test", Context.MODE_PRIVATE);
        mEditor = mSettings.edit();
        gsonb = new GsonBuilder();
        mGson = gsonb.create();
        create_b = (Button)findViewById(R.id.commit_button);
        my_et = (EditText)findViewById(R.id.my_category);
        //mCards_collection = new HashMap<String, ArrayList<NoteCard>>();

        if(extras != null)
        {
            modify = extras.getBoolean("modify");
            if(modify)
            {
                load();
                //load();
                my_key = extras.getString("key");
                old_key = extras.getString("key");
                cOLD = new ArrayList<NoteCard>();
                cOLD = mCards_collection.get(my_key);
                my_et.setText(my_key);
            }
        }

        create_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                my_key = my_et.getText().toString();
                //finish();
                //Toast.makeText(getBaseContext(),"a" + my_key + "a",Toast.LENGTH_LONG).show();
                if(!my_key.isEmpty())
                {

                    load();
                    if(!keys.isEmpty())
                    {
                        //Toast.makeText(getBaseContext(), "a" + my_key + "a", Toast.LENGTH_LONG).show();

                        ArrayList<NoteCard> temp = new ArrayList<NoteCard>();
                        temp = mCards_collection.get(my_key);

                        if(temp != null)
                        {
                            Toast.makeText(getBaseContext(), "This category already exists", Toast.LENGTH_LONG).show();

                        }
                        else {

                            if(modify)
                            {
                                mCards_collection.remove(old_key);
                                mCards_collection.put(my_key,cOLD);
                                String writearray = mGson.toJson(mCards_collection);
                                mEditor.putString(setting_name, writearray);
                                mEditor.apply();
                                setResult(RESULT_OK);
                                finish();
                            }
                            else
                            {
                                //mCards_collection = new HashMap<String, ArrayList<NoteCard>>();
                                mCards_collection.put(my_key,noteCards);
                                String writearray = mGson.toJson(mCards_collection);
                                mEditor.putString(setting_name, writearray);
                                mEditor.apply();
                                setResult(RESULT_OK);
                                finish();
                            }
                        }
                    }
                    else
                    {
                        //Toast.makeText(getBaseContext(),"Nothing was entered for category name",Toast.LENGTH_LONG).show();
                        try{
                            mCards_collection = new HashMap<String, ArrayList<NoteCard>>();
                            mCards_collection.put(my_key,noteCards);
                            String writearray = mGson.toJson(mCards_collection);
                            mEditor.putString(setting_name, writearray);
                            mEditor.apply();
                            setResult(RESULT_OK);
                        }
                        catch (Exception e){
                            if(noteCards == null)
                            {
                                Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
                                System.out.println(e.toString());
                            }
                            else
                            {
                                Toast.makeText(getBaseContext(), "asdaddsdaadds", Toast.LENGTH_LONG).show();
                            }
                        }

                        finish();
                    }

                }
                else
                {
                    //Toast.makeText(this,"Nothing was entered for category name",Toast.LENGTH_LONG).show();
                    finish();
                }

            }
        });
    }

    private void load(){
        String loadValue = mSettings.getString(setting_name, "");
        Type type = new TypeToken<HashMap<String,ArrayList<NoteCard>>>(){}.getType();
        mCards_collection = mGson.fromJson(loadValue,type);
        if(!(mCards_collection == null))
        {
            for(String item: mCards_collection.keySet())
            {
                keys.add(item);
            }
        }

    }
}
