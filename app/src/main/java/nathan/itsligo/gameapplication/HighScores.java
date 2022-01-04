package nathan.itsligo.gameapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class HighScores extends AppCompatActivity
{

    Button btnPlayAgain;
    TextView tvHighScore1;
    TextView tvHighScore2;
    TextView tvHighScore3;
    TextView tvHighScore4;
    TextView tvHighScore5;
    TextView[] tvHighScoreTable;
    private int padLeftTotalAmount = 30;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);


        btnPlayAgain = findViewById(R.id.btnPlayAgain);
        tvHighScore1 = findViewById(R.id.tvHighScore1);
        tvHighScore2 = findViewById(R.id.tvHighScore2);
        tvHighScore3 = findViewById(R.id.tvHighScore3);
        tvHighScore4 = findViewById(R.id.tvHighScore4);
        tvHighScore5 = findViewById(R.id.tvHighScore5);
        tvHighScoreTable = new TextView[]{tvHighScore1,tvHighScore2,tvHighScore3,tvHighScore4,tvHighScore5};



        DatabaseHandler db = new DatabaseHandler(this);
        List<GameScore> top5Scores;
        top5Scores = db.getTop5Scores();

        for(int i = 0; i < top5Scores.size(); i++)
        {
            GameScore score = top5Scores.get(i);
            String startingText = (i + 1) + ". " + score.getName();
            tvHighScoreTable[i].setText(startingText + GenerateWhiteSpace(padLeftTotalAmount - startingText.length()) + score.getScore());
        }
    }

    public void OnPlayAgainClicked(View view)
    {
        GameInfo.Reset();
        Intent mainIntent = new Intent(view.getContext(),MainActivity.class);
        startActivity(mainIntent);
    }

    public String GenerateWhiteSpace(int length)
    {
        StringBuilder sb = new StringBuilder();
        while(sb.length() < length)
            sb.append(' ');

        return sb.toString();
    }
}