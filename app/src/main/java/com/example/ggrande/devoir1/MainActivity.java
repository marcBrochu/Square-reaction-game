package com.example.ggrande.devoir1;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {
    Button button;
    TextView numberOfTries;
    Chronometer time;
    private int nbOfTries = 5;
    private int counterOfTries;
    private  float start_time;
    ArrayList<Float> Times = new ArrayList<Float>(nbOfTries);
    final Handler handler = new Handler();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        numberOfTries = (TextView) findViewById(R.id.numberOfTries);
        time = (Chronometer) findViewById(R.id.time);

        initialiseGame();
    }


//    Initialisation avant le debut du jeu
    public void initialiseGame(){
        counterOfTries = 1;
        Times.clear();
        time.setVisibility(View.INVISIBLE);
        numberOfTries.setVisibility(View.INVISIBLE);
        button.setBackgroundColor(Color.LTGRAY);
        button.setText(getString(R.string.buttonStart));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                greyButton();
            }
        });
    }


//  Bouton permettant de demarer et continuer la partie
    public void greyButton(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                start_time = (float) 0.000;
                time.setText(String.format("%.3f", start_time));
                startGame();
            }
        }, 1 * 1500);
    }


    public void startGame(){
        setStringsVisible();
        gameGenerator();
    }


//   Rend visible le texte et le met a jour
    public void setStringsVisible(){
        button.setBackgroundColor(Color.LTGRAY);
        button.setEnabled(true);
        button.setText(getString(R.string.waitForYellow));
        numberOfTries.setText(getString(R.string.attempt) + " " + counterOfTries
                + " " + getString(R.string.of) + " " + nbOfTries);
        numberOfTries.setVisibility(View.VISIBLE);
        time.setVisibility(View.VISIBLE);
    }


//    Temps aleatoire et gestion des evenements
    public void gameGenerator(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickBeforeYellowButton();
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                yellowButton();
            }
        }, (long) (Math.random() * 4000) + 1000);
    }


    public void clickBeforeYellowButton(){
        handler.removeCallbacksAndMessages(null);
        button.setText(getString(R.string.failure));
        button.setBackgroundColor(Color.RED);
        button.setClickable(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                greyButton();

            }
        }, 1 * 1500);
    }


    public void yellowButton(){
        button.setText(getString(R.string.clickNow));
        button.setBackgroundColor(Color.YELLOW);

        Timer T = new Timer();
        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        time.setText(String.format("%.3f", start_time));
                        start_time = (float) (start_time + 0.001);
                    }


                });
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancel();
                        time.setText(String.format("%.3f", start_time));
                        Times.add(start_time);
                        button.setBackgroundColor(Color.GREEN);
                        button.setText(getString(R.string.success));
                        button.setClickable(false);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (Times.size() >= nbOfTries){
                                    finalResult();
                                }
                                else {
                                    counterOfTries++;
                                    greyButton();
                                }
                            }
                        }, 1 * 1500);
                    }
                });
            }
        },1,1);
    }


//    Dialogue de fin et reinitialisation du jeu
    private void finalResult() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle(getString(R.string.testComplete));
        float average = averageList();
        alertDialog.setMessage(getString(R.string.averageIs) + " " + String.format("%.3f", average));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        initialiseGame();
                    }
                });
        alertDialog.show();
    }


    private float averageList() {
        float sum = 0;
        if(!Times.isEmpty()) {
            for (float numbers : Times) {
                sum += numbers;
            }
            return sum / Times.size();
        }
        return sum;
    }
}
