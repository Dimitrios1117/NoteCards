package com.example.dimitrios.NoteCards;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.dimitrios.swipe_testing.R;

public class  Add_Card_Activity extends AppCompatActivity {

    EditText question, answer;
    Button commit;
    String Question = "", Answer = "";
    int cardPosition = 0;
    boolean modify = false;
    //ArrayList<NoteCard> noteCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__card_);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            //noteCards= (ArrayList<NoteCard>)getIntent().getSerializableExtra("noteCards");
            //cardPosition = extras.getInt("position");
            modify = extras.getBoolean("modify");
            Question = extras.getString("question");
            Answer = extras.getString("answer");

            commit = (Button)findViewById(R.id.c_button);
            question = (EditText)findViewById(R.id.editText);
            answer = (EditText)findViewById(R.id.editText2);

            question.setText(Question);
            answer.setText(Answer);

        }
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (question.toString() != "" && answer.toString() != "") {
                    if (modify) {

                        Intent i = new Intent();
                        i.putExtra("question", question.getText().toString());
                        i.putExtra("answer", answer.getText().toString());
                        i.putExtra("modify", true);
                        setResult(RESULT_OK, i);
                        finish();
                    } else {
                        if (question.getText().toString().equals("") || answer.getText().toString().equals("")) {
                            finish();
                        } else {
                            Intent i = new Intent();
                            i.putExtra("question", question.getText().toString());
                            i.putExtra("answer", answer.getText().toString());
                            i.putExtra("modify", false);
                            setResult(RESULT_OK, i);
                            finish();
                        }
                    }
                }
            }
        });
    }
}