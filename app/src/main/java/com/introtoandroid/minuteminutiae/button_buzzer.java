package com.introtoandroid.minuteminutiae;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class button_buzzer extends AppCompatActivity {

    ImageButton playerOneButton;
    ImageButton playerTwoButton;
    TextView questionView;
    ArrayList<Integer> questionNums = new ArrayList<Integer>();
    Boolean penaltyValue;
    int n;
    int playerOneScore;
    int playerTwoScore;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.button_buzzer);
        Intent intent = getIntent();
        int maxNumQuestions = intent.getIntExtra("numOfQuestions", 5);
        penaltyValue = intent.getBooleanExtra("penaltyValue", false);
        //int maxNumQuestions = getResources().getInteger(R.integer.max_questions);


        Log.i("Buzzer", "Question Max: " + maxNumQuestions);

        playerOneButton = findViewById(R.id.imageButton);
        playerTwoButton = findViewById(R.id.imageButton2);
        questionView = findViewById(R.id.question_text_for_user);

        n = getQuestionNum();

        final int finalN1 = n;
        playerOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Buzzer", "Player One pressed button first");
                Intent i = new Intent(button_buzzer.this, questionScreenRotated.class);
                i.putExtra("QUESTION_NUMBER", n);
                i.putExtra("QUESTION_SIZE", questionNums.size());
                i.putExtra("PLAYER", "1");
                i.putExtra("PLAYERONESCORE", playerOneScore);
                i.putExtra("PLAYERTWOSCORE", playerTwoScore);
                startActivityForResult(i, 1);
            }
        });


        playerTwoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Buzzer", "Player Two pressed button first");
                Intent i = new Intent(button_buzzer.this, questionScreen.class);
                i.putExtra("QUESTION_NUMBER", n);
                i.putExtra("QUESTION_SIZE", questionNums.size());
                i.putExtra("PLAYER", "2");
                i.putExtra("PLAYERONESCORE", playerOneScore);
                i.putExtra("PLAYERTWOSCORE", playerTwoScore);
                i.putExtra("penaltyValue", penaltyValue);
                startActivityForResult(i, 1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 ){
            if (resultCode == RESULT_OK){
                //if player one answers correctly add a point
                if(data.getStringExtra("point").equals("PlayerTwo")){
                    playerTwoScore++;
                }else if(data.getStringExtra("point").equals("PlayerOne")){
                    playerOneScore++;
                }

                //If the wrong answer penalty is on, a player loses a point when answering wrong
                if(penaltyValue == true){
                    if(data.getStringExtra("wrong").equals("PlayerTwo")){
                        playerTwoScore--;
                    }
                    else if(data.getStringExtra("wrong").equals("PlayerOne")){
                        playerOneScore--;
                    }
                }

                Log.i("Buzzer", "return to buzzer method");
                Log.i("Buzzzer", Integer.toString(questionNums.size()));

                int newN = getQuestionNum();

            }else if(resultCode==RESULT_CANCELED){
                Log.i("Buzzer", "return to buzzer canceled");
            }
        }
    }

    public int getQuestionNum(){
        //method to set question text on screen and set value of n
        String [] questions = getResources().getStringArray(R.array.questions);
        Random rand = new Random();
        int q = questions.length;
        n = rand.nextInt(questions.length);
        Boolean inList = false;
        //loop that checks of chosen number has already been used
        for(int i = 0; i<questionNums.size(); i++){
            Log.i("QuestionLoop1", n + " " + questionNums.get(i));
            if(n == questionNums.get(i)){
                inList = true;
            }
        }
        //if number has already been choosen, pick another until all numbers have been used
        //go through loop if inList is true and questionNums size is not full
        while(inList && questionNums.size()!=q){
            n = rand.nextInt(questions.length);
            for(int i = 0; i<questionNums.size(); i++){
                Log.i("QuestionLoop2", n + " " + questionNums.get(i));
                if(n != questionNums.get(i)){
                    inList = false;
                }
            }

        }
        questionNums.add(n);
        //int questionNums[] = new int[0];
        //questionNums[questionNums.length] = n;
        questionView.setText(questions[n]);
        return n;
    }
}
