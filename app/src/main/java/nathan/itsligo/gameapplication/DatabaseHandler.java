package nathan.itsligo.gameapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "scoreManager";
    private static final String TABLE_GAMESCORES = "gamescores";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_SCORE = "score";

    public DatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_GAMESCORES_TABLE = "CREATE TABLE " + TABLE_GAMESCORES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_SCORE + " TEXT" + ")";
        db.execSQL(CREATE_GAMESCORES_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAMESCORES);
        onCreate(db);
    }

    public void emptyGamescores()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAMESCORES);

        onCreate(db);
    }

    void addGamescore(GameScore gameScore)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, gameScore.getName()); // Name
        values.put(KEY_SCORE, gameScore.getScore()); // Score

        db.insert(TABLE_GAMESCORES, null, values);
        db.close();
    }


    GameScore getGamescore(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_GAMESCORES, new String[] { KEY_ID,
                        KEY_NAME, KEY_SCORE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        GameScore gameScore = new GameScore(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), Integer.parseInt(cursor.getString(2)));

        return gameScore;
    }

    public List<GameScore> getAllGameScores()
    {
        List<GameScore> gamescoreList = new ArrayList<GameScore>();

        String selectQuery = "SELECT  * FROM " + TABLE_GAMESCORES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                GameScore gameScore = new GameScore();
                gameScore.setID(Integer.parseInt(cursor.getString(0)));
                gameScore.setName(cursor.getString(1));
                gameScore.setScore(Integer.parseInt(cursor.getString(2)));

                gamescoreList.add(gameScore);
            } while (cursor.moveToNext());
        }

        return gamescoreList;
    }


    // code to update the single contact
    public int updateGamescore(GameScore gameScore)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, gameScore.getName());
        values.put(KEY_SCORE, gameScore.getScore());

        return db.update(TABLE_GAMESCORES, values, KEY_ID + " = ?",
                new String[] { String.valueOf(gameScore.getID()) });
    }


    public void deleteGamescore(GameScore gameScore)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GAMESCORES, KEY_ID + " = ?",
                new String[] { String.valueOf(gameScore.getID()) });
        db.close();
    }

    public int getGamescoresCount() {
        int count = 0;
        String countQuery = "SELECT  * FROM " + TABLE_GAMESCORES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();

        return count;
    }

    public boolean isScoreInTop5(int score)
    {
        List<GameScore> top5List = getTop5Scores();

        if(top5List.size() < 5)
            return true;

        for(int i = 0; i < top5List.size(); i++)
        {
            if(score >= top5List.get(i).getScore())
            {
                return true;
            }
        }

        return false;
    }

    public List<GameScore> getTop5Scores()
    {
        List<GameScore> top5ScoreList;

        List<GameScore> allGameScores = getAllGameScores();

        Collections.sort(allGameScores);

        if(allGameScores.size() >= 5)
            top5ScoreList = allGameScores.subList(0,5);
        else
            top5ScoreList = allGameScores;


        return top5ScoreList;
    }
}