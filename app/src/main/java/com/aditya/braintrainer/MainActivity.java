package com.aditya.braintrainer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.security.spec.ECField;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    static int i = 1;

    int HighScore;
    int totalQuestions;
    String save;
    //creating a variable to store data while writing
    private static final int WRITE_EXTERNAL_STORAGE_CODE = 1;

    MediaPlayer mediaPlayer; //mediaplayer will play the time sound when the count down timer becomes 0

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

    private List<String> fileList = new ArrayList<String>();

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

    //Array List for storing scores
    static ArrayList<String> scoreList = new ArrayList<>();

    //this function will allow us to set up menue item on the app title bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        //onCreateOptionsMenu(Menu menu object)
        //menuInflater.inflate(R.menu.menue xml file name, menu object);
        menuInflater.inflate(R.menu.saved_game_score, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //this method will get called when someone selects an item from the menue
    //onOptionsItemSelected(@NonNull MenuItem item //this returns the selected menue item id from the menue )
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        //item.getItemId() will get the item id selected by the user
        switch (item.getItemId()){
            case R.id.score:
                Log.i("item","setting selected");
                Intent intent = new Intent(MainActivity.this,SavedScoreActivity.class);
                startActivity(intent);
                //return true; works same as a break statement it prevents the fall through of the switch case
                return true;
            default:
                return false;
        }
    }

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

    //storing high score int he file system you need to go to android manifest xml file
    //there inside the application tag write (android:requestLegacyExternalStorage="true")
    //permission denied problem will be solved
    public void scorestore() //it will store the high score
    {
        //saving scores
        HighScore = score;
        totalQuestions = TotalQuesionsAsked;

        save = "Correct Answers = "+HighScore + "\n Numbers of questions attempted = "+totalQuestions;
        if(ScoreCounter.getText().toString().equals("score"))
        {
            Toast.makeText(MainActivity.this, "File not saved score is 0/0", Toast.LENGTH_SHORT).show();
        }
        else{ //data entry will take place
            //android version above marshmallow will require to ask for user permission to access files
            //go to manifest file and type the following under the package name
            //<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"></uses-permission>

            if(Build.VERSION.SDK_INT>Build.VERSION_CODES.M)  //checking our bhuild version of the os greater than marshmallow version
            {
                if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED)
                {
                    String [] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

                    //show popup for runtime permissions
                    requestPermissions(permissions, WRITE_EXTERNAL_STORAGE_CODE);
                }
                else{
                    //permission already granted; hence save data
                    saveToTxtFile(save);
                }
            }

        }
    }

    //checking permission and requesting permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode)
        {
            case WRITE_EXTERNAL_STORAGE_CODE:{
                // if request is canceled then result arrays are empty
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    saveToTxtFile(save);
                }
                else
                {
                    Toast.makeText(this, "Storage permission required", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //actually saving in game data onto the file system by creating a directory and a txt file
    private void saveToTxtFile(String save) {
        //get current Time for file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());

        try{
            //path to storage in the file system
            //File path = Environment.getExternalStorageDirectory();
            File path = Environment.getExternalStorageDirectory();
            //create folder name "Brain_Trainer_game"
            File dir = new File(path+"/Brain_Trainer_game/");
            dir.mkdirs();

            //File name
            String Filename = "Brain_Trainer_saved_score_" + timeStamp + ".txt";

            //creating new file
            File file = new File(dir, Filename);

            //FileWriter class s used to store characters in file
            FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fileWriter);
            //save = String text data to be saved
            bw.write(save);
            bw.close();

            //Showing file name that was just created
            Toast.makeText(this,Filename + "is saved\n" +dir, Toast.LENGTH_LONG).show();
                //saving data of scores in the shared system preferences
                //adding data  to the array list
                String saved = (i + ". " + "Correct Answers = " + Integer.toString(score) + "\n" + "Total Questions Asked = " + Integer.toString(TotalQuesionsAsked) + "\n");
                SavedScoreActivity.savedScore.add(saved);
                SavedScoreActivity.arrayAdapter.notifyDataSetChanged();
                scoreList.add(i + ". " + "Correct Answers = " + Integer.toString(score) + "\n" + "Total Questions Asked = " + Integer.toString(TotalQuesionsAsked) + "\n");
                i++;
                SharedPreferences sharedPreferences = this.getSharedPreferences("com.aditya.braintrainer", Context.MODE_PRIVATE);
                try {
                    sharedPreferences.edit().putString("scoreList", ObjectSerializer.serialize(scoreList)).apply();
                } catch (Exception e) {
                    e.printStackTrace();
            }
        }
        catch (Exception e)
        {
            //if anything goes wrong
            Toast.makeText(this,e.getMessage(), Toast.LENGTH_SHORT).show();
        }
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

                //play sound when count down timer becomes 0
                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.timeout_ringtone);
                mediaPlayer.start();

                //saving the score in brain trainer folder
                scorestore();
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

        //setting the high score to the score textView
        ScoreCounter.setText(HighScore+"/"+totalQuestions);

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