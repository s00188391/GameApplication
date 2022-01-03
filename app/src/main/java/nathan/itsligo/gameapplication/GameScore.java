package nathan.itsligo.gameapplication;

public class GameScore implements Comparable<GameScore>
{
    private int _id;
    private String _name;
    private int _score;

    public GameScore(){}
    public GameScore(int id,String name, int score)
    {
        this._id = id;
        this._name = name;
        this._score = score;
    }

    public GameScore(String name, int score)
    {
        this._name = name;
        this._score = score;
    }

    public int getID() { return this._id; }
    public void setID(int id) { this._id = id; }

    public String getName() { return this._name; }
    public void setName(String name) { this._name = name; }

    public int getScore(){return this._score;}
    public void setScore(int score){this._score = score;}


    @Override
    public int compareTo(GameScore other)
    {

        if(getScore() < other.getScore())
            return 1;
        else if(getScore() == other.getScore())
            return 0;
        else return -1;
    }
}