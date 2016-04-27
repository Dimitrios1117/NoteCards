package com.example.dimitrios.NoteCards;

/**
 * Created by Dimitrios on 2/20/2016.
 */
public class NoteCard {
    protected String Question;
    protected String Answer;

    public NoteCard(String q, String a){
        this.Question = q;
        this.Answer = a;
    }

    public String getQuestion(){
        return this.Question;
    }
    public void setQuestion(String q){
        this.Question = q;
    }
    public String getAnswer(){
        return this.Answer;
    }
    public void setAnswer(String a){
        this.Answer = a;
    }
}
