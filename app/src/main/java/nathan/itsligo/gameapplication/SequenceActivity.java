package nathan.itsligo.gameapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import java.util.ArrayList;

public class SequenceActivity extends AppCompatActivity implements SensorEventListener
{

    private Button btnNorth, btnSouth,btnEast,btnWest;
    private ArrayList<Button> buttons;

    int[] buttonColours;


    private SensorManager sensorManager;
    private Sensor aSensor;
    private Sensor mSensor;

    private float gravity[];
    private float magnetic[];
    private float accels[] = new float[3];
    private float mags[] = new float[3];
    private float[] values = new float[3];
    private float azimuth;
    private float pitch;
    private float roll;


    private Button selectedButton;
    private boolean buttonIsSelected;
    private int currentSequenceIndex = 0;
    private int indexOfSelectedButton;
    private int defaultColourOfButtonSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sequence);



        sensorManager = (SensorManager) getSystemService(Activity.SENSOR_SERVICE);
        aSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(this, aSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);


        btnNorth = findViewById(R.id.btnNorth);
        btnSouth = findViewById(R.id.btnSouth);
        btnEast = findViewById(R.id.btnEast);
        btnWest = findViewById(R.id.btnWest);
        buttons = new ArrayList<Button>(){};
        buttons.add(btnNorth);
        buttons.add(btnSouth);
        buttons.add(btnEast);
        buttons.add(btnWest);
        GameInfo.startingSequenceAmount = buttons.size();



        buttonColours = getIntent().getIntArrayExtra("ButtonColours");
        for(int i = 0; i < buttons.size();i++)
        {
            ViewCompat.setBackgroundTintList(buttons.get(i), ColorStateList.valueOf(buttonColours[i]));
        }

    }

    protected void onResume()
    {
        super.onResume();

        sensorManager.registerListener(this, aSensor,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, mSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause()
    {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }


    @Override
    public void onSensorChanged(SensorEvent event)
    {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_MAGNETIC_FIELD:
                mags = event.values.clone();
                break;
            case Sensor.TYPE_ACCELEROMETER:
                accels = event.values.clone();
                break;
        }

        if (mags != null && accels != null) {
            gravity = new float[9];
            magnetic = new float[9];
            SensorManager.getRotationMatrix(gravity, magnetic, accels, mags);
            float[] outGravity = new float[9];
            SensorManager.remapCoordinateSystem(gravity, SensorManager.AXIS_X,SensorManager.AXIS_Z, outGravity);
            SensorManager.getOrientation(outGravity, values);

            //Get rotation of phone.
            azimuth = Math.abs(values[0] * 57.2957795f);
            pitch = Math.abs(values[1] * 57.2957795f);
            roll = Math.abs(values[2] * 57.2957795f);
            mags = null;
            accels = null;



            if(PhoneTiltedNorth() && !buttonIsSelected)
            {
                OnButtonSelected(btnNorth);
            }
            else if(PhoneTiltedSouth() && !buttonIsSelected)
            {
                OnButtonSelected(btnSouth);
            }
            else if(PhoneTiltedEast() && !buttonIsSelected)
            {
                OnButtonSelected(btnEast);
            }
            else if(PhoneTiltedWest() && !buttonIsSelected)
            {
                OnButtonSelected(btnWest);
            }
            else if(PhoneIsFlat())
            {

                if(buttonIsSelected)
                {
                    //Reset button.
                    buttonIsSelected = false;
                    ViewCompat.setBackgroundTintList(buttons.get(indexOfSelectedButton), ColorStateList.valueOf(defaultColourOfButtonSelected));

                    //If button selected was correct in the sequence.
                    if(indexOfSelectedButton == GameInfo.sequence.get(currentSequenceIndex))
                    {
                        currentSequenceIndex++;



                        if(currentSequenceIndex == GameInfo.sequence.size())
                        {
                            GameInfo.GoToNextRound();
                            Intent mainIntent = new Intent( this,MainActivity.class);
                            mainIntent.putExtra("ButtonColours",buttonColours);
                            startActivity(mainIntent);
                        }
                    }
                    else //Got sequence wrong, load game over screen.
                    {
                        currentSequenceIndex = 0;

                        Intent gameOverIntent = new Intent( this,GameOverActivity.class);
                        startActivity(gameOverIntent);
                    }
                }
            }
        }
    }


    private void OnButtonSelected(Button buttonToSelect)
    {
        buttonIsSelected = true;


        indexOfSelectedButton = buttons.indexOf(buttonToSelect);
        selectedButton = buttons.get(indexOfSelectedButton);


        defaultColourOfButtonSelected = GetColourOfButton(selectedButton);
        ViewCompat.setBackgroundTintList(selectedButton, ColorStateList.valueOf(getResources().getColor(R.color.White)));
    }

    private int GetColourOfButton(Button button) { return button.getBackgroundTintList().getDefaultColor(); }


    private boolean PhoneTiltedNorth(){return azimuth > 90 && pitch < 80f && pitch > 60f && roll < 100f;}
    private boolean PhoneTiltedSouth(){return azimuth < 70f && pitch < 70f;}
    private boolean PhoneTiltedEast(){return azimuth > 120f && azimuth < 180f && pitch < 82f && roll < 35f ;}
    private boolean PhoneTiltedWest(){return azimuth < 110f && pitch < 85f && roll > 170f;}
    private boolean PhoneIsFlat(){return azimuth > 120 && pitch > 80;}



}