package com.aditya.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Double wrongAnswers; //stores wrong answers that is to be inserted in rest of the choice boxes

    DecimalFormat df = new DecimalFormat(); //formatting fraction numbers

    Button button3;
    TextView sumTextView;
    Double answer; //stores the sum of two randome numbers

    //it helps store the correct answers inside the array randomely
    int LocationofcorrectAnswer;

    //choices initialization
    TextView choice0;
    TextView choice1;
    TextView choice2;
    TextView choice3;

    //creating an Array List that will hold the potential correct answers that will later be fed to choice boxes
    ArrayList<Double> answers= new ArrayList<Double>();

    //this array will hold + - * / operators
    ArrayList<String> operator= new ArrayList<String>();

    //Initalizing the textView12 that will tell us whether the answer is correct or not
    TextView textView12;

    //initializing wrong and right image respectively
    ImageView wrongImage;
    ImageView rightImage;

    //initializing ScoreCounter
    TextView ScoreCounter;
    int score=0;//creating a score variable that will keep track of the score
    int TotalQuesionsAsked=0; //keeps track of total numbers of questions asked by the system

    //initializing the timer textView
    TextView timer;

    Boolean TimerIsActive =false; //this booloean will keep track of whether the timer is active or not

    //initializing count down timer
    CountDownTimer countDownTimer;

    public void start(View view) //this function is responsible for handling starting and play again functions in the game
    { //this function will reset the score counter then the button is pressed
        button3.setVisibility(View.INVISIBLE); //it will set the button3 invisible when the button is clicked
        //starts the count down timer
        Count_Down_timer();
        //setting up the textView12 that shows right or wrong to be invisible
        textView12.setVisibility(View.INVISIBLE);
        //setting the ImageView to be INVISIBLE on the press the button
        rightImage.animate().rotation(360).alpha(0).setDuration(900);
        wrongImage.animate().rotation(720).alpha(0).setDuration(900);

        //calling new questions function
        newQuestions();

        //setting the score to 0
        score = 0;
        //setting Total Questions Asked to zero
        TotalQuesionsAsked = 0;
        //setting the score counter textView to 0/0
        ScoreCounter.setText(score+"/"+TotalQuesionsAsked);

        //Enabeling the choice buttons inorder to make them register user taps again
        choice0.setEnabled(true);
        choice1.setEnabled(true);
        choice2.setEnabled(true);
        choice3.setEnabled(true);
    }

    public void chooseAnswer(View view)
    {
        Log.i("choice",view.getTag().toString());
        //setting up Tag in the UI xml file of the application allows us to set onClockListener on multiple components
        //we just need to track the tag related to that component and we are good to go!
        //This just makes our life way more easier

        //view.getTag() gets the tag of the ui component that has been tapped by the user with the help of view function
        //view.getTag().toString() converts the tag from integer to string
       // Integer.toString(LocationofcorrectAnswer); this is the method to convert the integer variable defined by the user
      //view.getTag().toString(); this is the method used when we have to convert the value from integer to string returned by a predefined function
        //String 1.equals(String 2); tells us whether the string 1 == string 2 or not
        if(Integer.toString(LocationofcorrectAnswer).equals(view.getTag().toString()))
        {
            Log.i("correct ans","yes");
            textView12.setText("Right...");
            textView12.setVisibility(View.VISIBLE); ////setting up the textView12 that shows right or wrong to be visible

            //updating animation to happy face
            rightImage.animate().rotation(360).alpha(1).setDuration(900);
            wrongImage.animate().rotation(720).alpha(0).setDuration(900);
            score++; //incrementing score by 1 if the answer is right
        }
        else {
            Log.i("correct ans","No");
            textView12.setText("Wrong...");
            textView12.setVisibility(View.VISIBLE); ////setting up the textView12 that shows right or wrong to be visible

            //updating animation to sad face
            rightImage.animate().rotation(720).alpha(0).setDuration(900);
            wrongImage.animate().rotation(360).alpha(1).setDuration(900);
        }
        TotalQuesionsAsked++; //incrementing total number of questions asked by the system
        ScoreCounter.setText(score+"/"+TotalQuesionsAsked); //it will set the text to the ScoreCounter TextView

        //calling NewQuestions method
        newQuestions();

        //reset countdown timer
        resetCountDownTimer();

        //starting countdown timer
        Count_Down_timer();
    }

    //function for countdown timer
    public void Count_Down_timer()
    {
        //creating a countDown timer
        //30100 = 30 seconds count down timer will start at 30seconds
        //countdown timer will be updated in every 1000 = 1 second


        countDownTimer = new CountDownTimer(15100,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                //calculating time left in seconds
                int secondsleft= (int)millisUntilFinished/1000;
                //updating timer TextView
                timer.setText(secondsleft+"S");
            }

            @Override
            public void onFinish() {
                String message = "Time out!";
                //updating animation to sad face
                rightImage.animate().rotation(720).alpha(0).setDuration(900);
                wrongImage.animate().rotation(360).alpha(1).setDuration(900);
                textView12.setText(message);
                textView12.setVisibility(View.VISIBLE);

                //setting up the button3 text from start to play again
                String playAgain = "Play Again";
                button3.setText(playAgain);
                button3.setVisibility(View.VISIBLE); //making the button3 visible again

                //Disableing choice boxes
                //it will disable their functionality to register touch from the user
                choice0.setEnabled(false);
                choice1.setEnabled(false);
                choice2.setEnabled(false);
                choice3.setEnabled(false);
            }
        }.start();
    }

    //reset CountDown timer function
    public void resetCountDownTimer() //function for resetting the countDownTimer
    {
        countDownTimer.cancel(); //this will stop the timer
        TimerIsActive =false;
    }

    public void newQuestions() //this function is responsible of updating the questions and answers in the choice
    {
        //to ensure that the question have summision of two randome numbers we have to call randome function in our project
        Random random = new Random(); //randome number generator
        int a =random.nextInt(99); //this sets a of a randome number between 0 to 20
        int b =random.nextInt(99);

        //creating Strings of operator so that we can use .equals() function to compare to strings
        String add = "+";
        String subtract = "-";
        String Multiply = "*";
        String Division = "/";

        //populating operator array with symbols + - * /
        operator.add("+");
        operator.add("-");
        operator.add("*");
        operator.add("/");

        //randomizing the chosen operator
        Random RandomOperatorLocation = new Random(); //generates random operator location in inside the operator array
        int positionOfTheOperator = RandomOperatorLocation.nextInt(4); //this will store the position of the operator that is randomely generated

        //now using if else to determine the operation to performed here
        if(operator.get(positionOfTheOperator).equals(add))
        {
            Double doublea = new Double(a); //converting int a to double a typecasting
            Double doubleb = new Double(b); //converting int b to double b typecasting
            answer = doublea+doubleb;
            sumTextView.setText(a + " + " + b);
        }
        else if(operator.get(positionOfTheOperator).equals(subtract))
        {
            Double doublea = new Double(a); //converting int a to double a typecasting
            Double doubleb = new Double(b); //converting int b to double b typecasting
            answer = doublea-doubleb;
            sumTextView.setText(a + " - " + b);
        }
        else if(operator.get(positionOfTheOperator).equals(Multiply))
        {
            Double doublea = new Double(a); //converting int a to double a typecasting
            Double doubleb = new Double(b); //converting int b to double b typecasting
            answer = doublea*doubleb;
            sumTextView.setText(a + " X " + b);
        }
        else if(operator.get(positionOfTheOperator).equals(Division))
        {
            Double doublea = new Double(a); //converting int a to double a typecasting
            Double doubleb = new Double(b); //converting int b to double b typecasting
            answer = doublea/doubleb;
            sumTextView.setText(a + " / " + b);
        }

        //determining which choice will have the correct answer by using random generator
        Random randomeChoiceGenerator = new Random();
        LocationofcorrectAnswer = randomeChoiceGenerator.nextInt(4); //this will store the answer randomely in one of the four choices
        //4 is the limit to which the randome numbers will be generated

        //clearing the array before repopulating our array of answers
        answers.clear(); //it will clear our array
        //Note if you don't clear the array answer then the choices blocks will not get updated

        //using for loop to populate the array of answers
        for(int i=0;i<4;i++)
        {
            //generating randome numbers that is to be stored in the remaining choices inside the answers arrayList
            Random randomeAnswerGenerator = new Random();
            if(i==LocationofcorrectAnswer)
            {
                answers.add(answer); //adding correct answer inside of an array
            }
            else
            {
                //using for loop to convert the integer into decimal number
                for (int n = 0; n < 4000; n++) {
                    wrongAnswers = randomeAnswerGenerator.nextInt(400) / 10.0;
                }

                while(wrongAnswers == answer) //this will prevent any correct answers to be stored in more than one choice
                {
                    for (int n = 0; n < 4000; n++) {
                        wrongAnswers = randomeAnswerGenerator.nextInt(4000) / 10.0;
                    }

                }

                //answers.add(); will store the numbers inside the answers arrayList
                answers.add(wrongAnswers); //40 is the limit to which the randome numbers will be generated
            }
        }

        df.setMaximumFractionDigits(2); //setting up the maximum number of decimal places

        //formatting decimal numbers upto 2 places from the point i.e 0.23
        //converting from double to string
        String ans1 = df.format(answers.get(0));
        String ans2 = df.format(answers.get(1));
        String ans3 = df.format(answers.get(2));
        String ans4 = df.format(answers.get(3));

        //here we will set the answers to our choices
        choice0.setText(ans1); //get(index of answers array)
        choice1.setText(ans2);
        choice2.setText(ans3);
        choice3.setText(ans4);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button3 = findViewById(R.id.button3);
        sumTextView = findViewById(R.id.sumTextView);

        //Linking all the choices to the code here
        choice0 = findViewById(R.id.choice0);
        choice1 = findViewById(R.id.choice1);
        choice2 = findViewById(R.id.choice2);
        choice3 = findViewById(R.id.choice3);

        //setting up the wrong and right image
        wrongImage = findViewById(R.id.wrongImage);
        rightImage = findViewById(R.id.rightImage);

        textView12 = findViewById(R.id.textView12); //textView12 will tell us whether we chose right choice or not

        //setting up the ScoreCounter
        ScoreCounter = findViewById(R.id.ScoreCounter);

        //setting up the timer textView
        timer = findViewById(R.id.TimerTextView);

        //calling newQuestion method that we just created here
        newQuestions();

        //disableing choice boxes when the application first starts
        choice0.setEnabled(false);
        choice1.setEnabled(false);
        choice2.setEnabled(false);
        choice3.setEnabled(false);

        //setting up the devinfoTextView
        TextView devinfo = (TextView)findViewById(R.id.DevinfoTextView);
        devinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Activity2.class);
                startActivity(intent);
            }
        });
    }
}