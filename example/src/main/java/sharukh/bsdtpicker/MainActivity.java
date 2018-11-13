package sharukh.bsdtpicker;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import sharukh.sliderdtpicker.SliderDateTimePicker;

public class MainActivity extends AppCompatActivity {
    private SimpleDateFormat sdf = new SimpleDateFormat("d MMM, h aa", Locale.ENGLISH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TextView textView = findViewById(R.id.tutorial_text);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SliderDateTimePicker.newInstance()
                        .setOnDateTimeSetListener((new SliderDateTimePicker.OnDateTimeSetListener() {
                            @Override
                            public void onDateTimeSelected(final Calendar startTime) {

                                SliderDateTimePicker.newInstance()
                                        .setStartDate(startTime.getTime())
                                        .setOnDateTimeSetListener(new SliderDateTimePicker.OnDateTimeSetListener() {
                                            @Override
                                            public void onDateTimeSelected(Calendar endTime) {

                                                textView.setText(sdf.format(startTime.getTime()) + " ---to--- " + sdf.format(endTime.getTime()));

                                            }
                                        })
                                        .setStartLabel("Some Kinda Label")
                                        .setEndLabel("Whatever")
                                        .setTimeTextColor(ContextCompat.getColor(getApplicationContext(), R.color.ongoing_dark))
                                        .setSelectedDateBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.somedrawble))
                                        .setSelectedTimeBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.somedrawble2))
                                        .setDoneButtonBackground(ContextCompat.getDrawable(getApplicationContext(), R.color.failure_dark))
                                        .show(getSupportFragmentManager(), "Your wish");
                            }
                        }))
                        .setStartLabel("Start Time")
                        .show(getSupportFragmentManager(), "Your wish");


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
