package com.example.www.geoquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private boolean answerIsTrue;
    private TextView answer_TextView;
    private Button showAnswer;
    private int triedIndex,cheatedIndex;

    public static final String EXTRA_ANSWER_SHOWN ="com.example.www.geoquiz.answer_shown";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        setAnswerShown(false);
        answerIsTrue = getIntent().getBooleanExtra(MainActivity.EXTRA_ANSWER_IS_TRUE,false);
        triedIndex = getIntent().getIntExtra("index",-1);
        answer_TextView = (TextView)findViewById(R.id.answer_textView);
        showAnswer = (Button)findViewById(R.id.show_answer);
    }
    protected void onShowAnswerClicked(View view){
        if(answerIsTrue){
            answer_TextView.setText(R.string.correct_Toast);
        }else{
            answer_TextView.setText(R.string.false_toast);
        }
        cheatedIndex = triedIndex;
        setAnswerShown(true);
    }
    private void setAnswerShown(boolean isAnswerShown){
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN,isAnswerShown);
        data.putExtra("cheated",cheatedIndex);
        setResult(RESULT_OK,data);
    }
}
