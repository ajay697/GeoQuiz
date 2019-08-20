package com.example.www.geoquiz;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button mTrueButton, mFalseButton,mNextButton,mCheatButton;

    private TrueFalse[] mQuestionBank= new TrueFalse[]{
            new TrueFalse(R.string.question_oceans,true),
            new TrueFalse(R.string.question_africa,false),
            new TrueFalse(R.string.question_mideast,false),
            new TrueFalse(R.string.question_americas,true),
            new TrueFalse(R.string.question_asia,true)
    };
    private int currentIndex = 0;
    private int cheatedIndex;
    private static final String final_index = "index";
    public static final String EXTRA_ANSWER_IS_TRUE = "com.example.www.geoquiz.answer_is_true";
    private TextView questionTextView;
    private boolean mIsCheater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionTextView = (TextView)findViewById(R.id.ques_ans_textView);
        mTrueButton = (Button)findViewById(R.id.true_Button);
        mFalseButton = (Button)findViewById(R.id.false_Button);
        mNextButton = (Button)findViewById(R.id.next_button);
        mCheatButton = (Button)findViewById(R.id.cheat_button);
        questionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNextClick(v);
            }
        });
        if(savedInstanceState!=null){
            currentIndex = savedInstanceState.getInt(final_index,0);
        }
        mIsCheater = false;
        updateQuestion();
    }
    public void trueButtonClicked(View view){
        CheckAnswerTrue(true);
    }
    public void falseButtonClicked(View view){
        CheckAnswerTrue(false);
    }
    private void updateQuestion(){
        int question = mQuestionBank[currentIndex].getQuestion();
        questionTextView.setText(question);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data ==null){
            return;
        }else{
            mIsCheater = data.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN,false);
            cheatedIndex = data.getIntExtra("cheated",-1);
        }
    }
    public void onNextClick(View view){
        currentIndex = (currentIndex+1)%mQuestionBank.length;
        if(currentIndex==cheatedIndex){
             mNextButton.setEnabled(false);
             Toast.makeText(this,"Quiz completed.",Toast.LENGTH_SHORT).show();
        }
        mIsCheater = false;
        updateQuestion();
    }

    private void CheckAnswerTrue(boolean userAnswer){
        boolean answer = mQuestionBank[currentIndex].isQuestionTrue();
        int messageResId = 0;
        if(mIsCheater){
            messageResId = R.string.judgement_toast;
        }else{
            if(userAnswer==answer){
                messageResId = R.string.correct_Toast;
            }else{
                messageResId = R.string.false_toast;
            }
        }

        Toast.makeText(this,messageResId,Toast.LENGTH_SHORT).show();
    }
    public void onPrevClick(View view){
        if(currentIndex==0){
            currentIndex = mQuestionBank.length-1;
        }else{
            currentIndex = currentIndex-1;
        }
        updateQuestion();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(final_index,currentIndex);
    }
    public void OnCheatButtonClick(View view){

        Intent i = new Intent(MainActivity.this,CheatActivity.class);
        boolean answer_is_true = mQuestionBank[currentIndex].isQuestionTrue();
        i.putExtra(EXTRA_ANSWER_IS_TRUE,answer_is_true);
        i.putExtra("index",currentIndex);
        startActivityForResult(i,0);
    }
    public void onRestartClick(View view){
        currentIndex = 0;
        mIsCheater = false;
        cheatedIndex = -1;
        mNextButton.setEnabled(true);
        questionTextView.setText(mQuestionBank[currentIndex].getQuestion());
    }
}
