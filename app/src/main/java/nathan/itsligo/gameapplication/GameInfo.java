package nathan.itsligo.gameapplication;

import java.util.ArrayList;
import java.util.Random;

public class GameInfo
{
    public static ArrayList<Integer> sequence = new ArrayList<>();


    public static int roundNumber = 1;


    public static int startingSequenceAmount;
    public static int currentSequenceAmount;
    private static int sequenceIncreaseAmount = 2;


    public static int playerScore = 0;
    private static int startingScoreToGivePerRound = 4;
    private static int currentScoreToGivePerRound = startingScoreToGivePerRound;

    public static int totalNumberOfButtons;

    public static void AddRandomNumbersToSequence(int amountToAdd)
    {
        Random rng = new Random();

        for(int i = 0; i < amountToAdd; i++)
        {
            sequence.add(rng.nextInt(totalNumberOfButtons));
        }
    }

    public static void GoToNextRound()
    {
        roundNumber++;
        playerScore += currentScoreToGivePerRound;

        currentScoreToGivePerRound += startingScoreToGivePerRound; //Score given to player will be 4, 8, 16, 32 etc.

        currentSequenceAmount += sequenceIncreaseAmount;
        AddRandomNumbersToSequence(sequenceIncreaseAmount);
    }

    public static void Reset()
    {
        sequence.clear();
        roundNumber = 1;
        playerScore = 0;
        currentScoreToGivePerRound = startingScoreToGivePerRound;
    }

    public static boolean IsAtStartOfGame(){return sequence.size() == 0;}
}