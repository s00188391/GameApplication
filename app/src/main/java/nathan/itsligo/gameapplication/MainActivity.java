package nathan.itsligo.gameapplication;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //UI Elements.
    private TextView tvRoundInfo;
    private Button btnNorth, btnSouth, btnEast, btnWest,btnShowSequence;
    private Button[] buttons;


    //Showing Sequence To User.
    private boolean sequenceShownToUser;
    private int sequenceIndex = 0;
    private Handler sequenceHandler = new Handler();
    private Button buttonInSequence;
    private int millisecondsBetweenButtonFlashing = 1500;
    private int millisecondsToFlash = 500;
    private int millisecondsToGoToDefaultColour = 500;

    private int[] buttonColours = new int[4];
    private ArrayList<Integer> colourList = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find UI Elements.
        tvRoundInfo = findViewById(R.id.tvRoundInfo);
        btnNorth = findViewById(R.id.btnNorth);
        btnSouth = findViewById(R.id.btnSouth);
        btnEast = findViewById(R.id.btnEast);
        btnWest = findViewById(R.id.btnWest);
        buttons = new Button[]{btnNorth,btnSouth,btnEast,btnWest};
        GameInfo.totalNumberOfButtons = buttons.length;
        GameInfo.currentSequenceAmount = buttons.length;

        tvRoundInfo.setText("Round " + GameInfo.roundNumber);

        AddColoursToColourList(); //Add all colours from colours. xml to a list.

        //If at start of game, assign random colour to each button.
        if(GameInfo.roundNumber == 1)
        {
            for(int i = 0; i < buttons.length; i++)
            {
                int randomIndex = new Random().nextInt(colourList.size());
                int randomColour = colourList.get(randomIndex);

                buttonColours[i] = randomColour;
                AssignColourToButton(buttons[i],randomColour);

                colourList.remove(randomIndex); //List ensures that same colour isn't chosen more than once.
            }
        }
        else //Not at start of game - so round 2,3 etc. Don't get random colours -  Get colours from sequence activity.
        {
            buttonColours = getIntent().getIntArrayExtra("ButtonColours");
            for(int i = 0; i < buttons.length; i++)
            {
                AssignColourToButton(buttons[i],buttonColours[i]);
            }
        }

        if(GameInfo.IsAtStartOfGame()) //Player is just starting, add 4 to sequence.
        {
            GameInfo.AddRandomNumbersToSequence(GameInfo.currentSequenceAmount);
        }
    }

    private Runnable sequenceRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            buttonInSequence = buttons[GameInfo.sequence.get(sequenceIndex)];

            Runnable toWhiteRunnable = new Runnable() {
                public void run()
                {
                    final int defaultColour = GetColourOfButton(buttonInSequence);
                    ViewCompat.setBackgroundTintList(buttonInSequence, ColorStateList.valueOf(getResources().getColor(R.color.White)));

                    Runnable toDefaultRunnable = new Runnable() {
                        public void run()
                        {
                            ViewCompat.setBackgroundTintList(buttonInSequence, ColorStateList.valueOf(defaultColour));
                        }
                    };
                    sequenceHandler.postDelayed(toDefaultRunnable, millisecondsToGoToDefaultColour);

                } // end runnable
            };
            sequenceHandler.postDelayed(toWhiteRunnable, millisecondsToFlash);


            if(sequenceIndex + 1 < GameInfo.sequence.size())
            {
                sequenceHandler.postDelayed(this,millisecondsBetweenButtonFlashing);
                sequenceIndex++;
            }
            else
            {
                sequenceIndex = 0;
            }
        }
    };

    private void AddColoursToColourList()
    {
        int[] colours = getResources().getIntArray(R.array.colours); //Get all possible colours.
        for(int i = 0; i < colours.length; i++) //Add all possible colours to list.
        {
            colourList.add((Integer) colours[i]);
        }
    }

    private void AssignColourToButton(Button button,int colour)
    {
        ViewCompat.setBackgroundTintList(button, ColorStateList.valueOf(colour));
    }

    public void OnTrySequenceClicked(View view)
    {
        //Load activity to attempt sequence - pass over colours of buttons.

        Intent sequenceIntent = new Intent(view.getContext(),SequenceActivity.class);
        sequenceIntent.putExtra("ButtonColours",buttonColours);
        startActivity(sequenceIntent);

    }

    public void OnShowSequenceClicked(View view)
    {
        if(!sequenceShownToUser)
        {
            sequenceShownToUser = true;
            for(int i =0;i < GameInfo.sequence.size(); i++)
            {
                Log.i("HELLO",String.valueOf(GameInfo.sequence.get(i)));
            }

            sequenceRunnable.run();
        }
    }

    private int GetColourOfButton(Button button) { return button.getBackgroundTintList().getDefaultColor(); }
}