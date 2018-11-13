package sharukh.sliderdtpicker;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import sharukh.sliderdtpicker.utils.HorizontalCalendarListener;
import sharukh.sliderdtpicker.utils.HorizontalSnapHelper;


public class SliderDateTimePicker extends BottomSheetDialogFragment {

    /* Private Fields
     * */
    private static final String TAG = "BottomSlidingDateTime";
    private static final String INPUT_TITLE = "Title";
    private Calendar setCal;
    private OnDateTimeSetListener dateTimeSetListener;
    private Date startDate;
    private SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d MMM, h aa", Locale.ENGLISH);

    private TextView start_time;
    private TextView end_time;

    /* Public Usage Fields
     * */
    public static String fragTitle = "";
    private Drawable selectedDateBackground;
    private Drawable selectedTimeBackground;
    private Drawable doneButtonBackground;
    private String startLabel, endLabel;
    private int timeTextColor = -1;
    private Context context;

    public static SliderDateTimePicker newInstance() {

        //Bundle args = new Bundle();
        //args.putString(INPUT_TITLE, title);
        SliderDateTimePicker fragment = new SliderDateTimePicker();
        //fragment.setArguments(args);
        return fragment;
    }

    public SliderDateTimePicker setOnDateTimeSetListener(OnDateTimeSetListener onDateTimeSetListener) {
        if (onDateTimeSetListener != null) {
            this.dateTimeSetListener = onDateTimeSetListener;
        }
        return this;
    }

    public SliderDateTimePicker setStartDate(@Nullable Date startDate) {
        this.startDate = startDate;
        return this;
    }

    public SliderDateTimePicker setSelectedDateBackground(@Nullable Drawable selectedDateBackground) {
        this.selectedDateBackground = selectedDateBackground;
        return this;
    }

    public SliderDateTimePicker setSelectedTimeBackground(@Nullable Drawable selectedTimeBackground) {
        this.selectedTimeBackground = selectedTimeBackground;
        return this;
    }

    public SliderDateTimePicker setDoneButtonBackground(@Nullable Drawable doneButtonBackground) {
        this.doneButtonBackground = doneButtonBackground;
        return this;
    }

    public SliderDateTimePicker setStartLabel(@Nullable String startLabel) {
        this.startLabel = startLabel;
        return this;
    }

    public SliderDateTimePicker setEndLabel(@Nullable String endLabel) {
        this.endLabel = endLabel;
        return this;
    }

    public SliderDateTimePicker setTimeTextColor(int color) {
        this.timeTextColor = color;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_date_time_picker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);

        context = getContext();

        /*Init Vars*/
        /*if (getArguments() != null) {
            fragTitle = getArguments().getString(INPUT_TITLE);
        }*/

        /*Init Views*/
        //Set Title
        /*TextView title = view.findViewById(R.id.frag_title);
        title.setText(fragTitle);*/

        defineDefaults();

        start_time = rootView.findViewById(R.id.frag_start_time);
        end_time = rootView.findViewById(R.id.frag_end_time);

        setCal = Calendar.getInstance();

        /* If no startDate is provided, disable from this hour */
        final Calendar disableBeforeCal = Calendar.getInstance();
        disableBeforeCal.set(Calendar.MINUTE, 0);
        disableBeforeCal.set(Calendar.SECOND, 0);
        if (startDate != null) {
            disableBeforeCal.setTime(startDate);
            setCal.setTime(startDate);
        }
        //Disabling from next hour from now or given startDate
        disableBeforeCal.add(Calendar.HOUR_OF_DAY, 1);
        setCal.add(Calendar.HOUR_OF_DAY, 1);


        /* ends after 2 years from now */
        Calendar endCal = Calendar.getInstance();
        endCal.add(Calendar.YEAR, 2);

        //Date Picker Init
        final HorizontalCalendar date_picker = new HorizontalCalendar.Builder(rootView, R.id.frag_date_picker)
                .range(disableBeforeCal, endCal)
                .datesNumberOnScreen(5)
                .configure()
                .selectedDateBackground(selectedDateBackground)
                .end()
                .build();

        final TimeAdapter adapter = new TimeAdapter(disableBeforeCal.get(Calendar.HOUR_OF_DAY), new TimeAdapter.OnTimeSelectedListener() {
            @Override
            public void onTimeSelected(int hourOfTheDay) {
                setCal.set(Calendar.HOUR_OF_DAY, hourOfTheDay);
                paintTimes();
                date_picker.refresh();
            }
        });

        final RecyclerView time_recycler = rootView.findViewById(R.id.frag_time_picker);
        time_recycler.setLayoutManager(new CenterLayoutManger(context, LinearLayoutManager.HORIZONTAL, false));
        time_recycler.setAdapter(adapter);
        adapter.setSelectedBackground(selectedTimeBackground);

        HorizontalSnapHelper snapHelper = new HorizontalSnapHelper();
        snapHelper.attachToRecyclerView(time_recycler);

        //Scrolling to current time
        time_recycler.smoothScrollToPosition(setCal.get(Calendar.HOUR_OF_DAY));

        date_picker.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                setCal.set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH));
                setCal.set(Calendar.MONTH, date.get(Calendar.MONTH));
                setCal.set(Calendar.YEAR, date.get(Calendar.YEAR));
                setCal.set(Calendar.MINUTE, 0);
                setCal.set(Calendar.SECOND, 0);

                adapter.setDisableBefore(setCal, startDate);

                time_recycler.smoothScrollToPosition(setCal.get(Calendar.HOUR_OF_DAY));

                paintTimes();
            }
        });

        //Do these for the first time
        date_picker.selectDate(setCal, false);
        adapter.setDisableBefore(disableBeforeCal, startDate);
        time_recycler.smoothScrollToPosition(setCal.get(Calendar.HOUR_OF_DAY));

        Button doneButton = rootView.findViewById(R.id.frag_done);
        doneButton.setBackgroundDrawable(doneButtonBackground);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dateTimeSetListener != null)
                    dateTimeSetListener.onDateTimeSelected(setCal);
                else
                    Log.e(TAG, "dateTimeSetListener not implemented");
                dismissAllowingStateLoss();
            }
        });


        TextView start_label = rootView.findViewById(R.id.frag_start_time_label);
        TextView end_label = rootView.findViewById(R.id.frag_end_time_label);

        start_label.setText(startLabel);
        end_label.setText(endLabel);

        start_time.setTextColor(timeTextColor);
        end_time.setTextColor(timeTextColor);

        paintTimes();

    }

    private void paintTimes() {
        if (startDate == null) {
            start_time.setText(sdf.format(setCal.getTime()));
        } else {
            start_time.setText(sdf.format(startDate));
            end_time.setText(sdf.format(setCal.getTime()));
        }
    }

    private void defineDefaults() {
        if (selectedDateBackground == null) {
            selectedDateBackground = ContextCompat.getDrawable(context, R.drawable.gradient_primary);
        }

        if (doneButtonBackground == null) {
            doneButtonBackground = ContextCompat.getDrawable(context, R.drawable.gradient_button);
        }

        if (timeTextColor == -1)
            timeTextColor = ContextCompat.getColor(context, R.color.text_primary);
    }

    public interface OnDateTimeSetListener {
        void onDateTimeSelected(Calendar selectedDateTime);
    }


}
