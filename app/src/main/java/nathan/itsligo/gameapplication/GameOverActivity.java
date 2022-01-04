package nathan.itsligo.gameapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameOverActivity extends AppCompatActivity {


    private TextView tvScoreInfo, tvRoundInfo, tvTop5ScoreNotification;
    private EditText etEnterName;
    private Button btnSubmitScore;
    private DatabaseHandler db;
    private int playerNameMaxLength = 15;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        db = new DatabaseHandler(this);


        tvScoreInfo = findViewById(R.id.tvScore);
        tvRoundInfo = findViewById(R.id.tvRound);
        tvTop5ScoreNotification = findViewById(R.id.tvTopScoreNotification);
        etEnterName = findViewById(R.id.etEnterName);
        btnSubmitScore = findViewById(R.id.btnSubmitScore);

        //Set text to
        tvScoreInfo.setText("Your score was " + GameInfo.playerScore);
        tvRoundInfo.setText("You made it to round " + GameInfo.roundNumber);

        //If score in top 5?
        if(db.isScoreInTop5(GameInfo.playerScore))
        {
            EnableScoreEntryUI();
        }
        else
        {
            tvTop5ScoreNotification.setEnabled(false);
            tvTop5ScoreNotification.setVisibility(View.GONE);
            DisableScoreEntryUI();
        }
    }

    public void OnPlayAgainClicked(View view)
    {
        GameInfo.Reset();
        Intent mainIntent = new Intent(view.getContext(),MainActivity.class);
        startActivity(mainIntent);
    }

    public void OnShowHighScoresClicked(View view)
    {
        Intent highScoreIntent = new Intent(view.getContext(),HighScores.class);
        startActivity(highScoreIntent);
    }

    public void OnSubmitScoreClicked(View view)
    {
        if(etEnterName.getText().toString() != "" && etEnterName.getText().toString().length() < playerNameMaxLength)
        {
            //Add score to database.
            db.addGamescore(new GameScore(etEnterName.getText().toString().trim(),GameInfo.playerScore));

            //Disable edittext and button.
            DisableScoreEntryUI();
        }
    }
    private void EnableScoreEntryUI()
    {
        tvTop5ScoreNotification.setEnabled(true);
        etEnterName.setEnabled(true);
        btnSubmitScore.setEnabled(true);
        tvTop5ScoreNotification.setVisibility(View.VISIBLE);
        etEnterName.setVisibility(View.VISIBLE);
        btnSubmitScore.setVisibility(View.VISIBLE);
    }

    private void DisableScoreEntryUI()
    {
        etEnterName.setEnabled(false);
        etEnterName.setVisibility(View.GONE);
        btnSubmitScore.setEnabled(false);
        btnSubmitScore.setVisibility(View.GONE);
    }
}