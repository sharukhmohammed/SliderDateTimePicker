package sharukh.sliderdtpicker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

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

    public static SliderDateTimePicker newInstance(String title) {

        Bundle args = new Bundle();
        args.putString(INPUT_TITLE, title);
        SliderDateTimePicker fragment = new SliderDateTimePicker();
        fragment.setArguments(args);
        return fragment;
    }

    public void setOnDateTimeSetListener(OnDateTimeSetListener onDateTimeSetListener) {
        if (onDateTimeSetListener != null) {
            this.dateTimeSetListener = onDateTimeSetListener;
        }
    }

    public void setStartDate(@Nullable Date startDate) {
        this.startDate = startDate;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_date_time_picker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*Init Vars*/
        if (getArguments() != null) {
            fragTitle = getArguments().getString(INPUT_TITLE);
        }

        /*Init Views*/
        //Set Title
        TextView title = view.findViewById(R.id.frag_title);
        title.setText(fragTitle);

        start_time = view.findViewById(R.id.frag_start_time);
        end_time = view.findViewById(R.id.frag_end_time);

        setCal = Calendar.getInstance();

        /* If no startDate is provided, disable from this hour */
        final Calendar disableBeforeCal = Calendar.getInstance();
        disableBeforeCal.set(Calendar.MINUTE, 0);
        disableBeforeCal.set(Calendar.SECOND, 0);
        if (startDate == null) {
            disableBeforeCal.add(Calendar.HOUR_OF_DAY, 1);
        } else {
            disableBeforeCal.setTime(startDate);
        }

        /* ends after 2 years from now */
        Calendar endCal = Calendar.getInstance();
        endCal.add(Calendar.YEAR, 2);


        final TimeAdapter adapter = new TimeAdapter(disableBeforeCal.get(Calendar.HOUR_OF_DAY), new TimeAdapter.OnTimeSelectedListener() {
            @Override
            public void onTimeSelected(int hourOfTheDay) {
                setCal.set(Calendar.HOUR_OF_DAY, hourOfTheDay);
                paintTimes();
            }
        });

        final RecyclerView time_recycler = view.findViewById(R.id.frag_time_picker);
        time_recycler.setLayoutManager(new CenterLayoutManger(getContext(),LinearLayoutManager.HORIZONTAL,false));
        time_recycler.setAdapter(adapter);

        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(time_recycler);

        //Scrolling to current time
        time_recycler.smoothScrollToPosition(setCal.get(Calendar.HOUR_OF_DAY));

        //Date Picker Init
        HorizontalCalendar date_picker = new HorizontalCalendar.Builder(view, R.id.frag_date_picker)
                .range(disableBeforeCal, endCal)
                .datesNumberOnScreen(5)
                .build();

        date_picker.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                setCal.set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH));
                setCal.set(Calendar.MONTH, date.get(Calendar.MONTH));
                setCal.set(Calendar.YEAR, date.get(Calendar.YEAR));
                setCal.set(Calendar.MINUTE, 0);
                setCal.set(Calendar.SECOND, 0);

                adapter.setDisableBefore(setCal);

                time_recycler.smoothScrollToPosition(setCal.get(Calendar.HOUR_OF_DAY));

                paintTimes();

            }
        });

        //Do these for the first time
        date_picker.selectDate(disableBeforeCal, false);
        adapter.setDisableBefore(disableBeforeCal);
        time_recycler.smoothScrollToPosition(setCal.get(Calendar.HOUR_OF_DAY));

        view.findViewById(R.id.frag_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dateTimeSetListener != null)
                    dateTimeSetListener.onDateTimeSelected(setCal);
                else
                    Toast.makeText(getContext(), "dateTimeSetListener not implemented", Toast.LENGTH_SHORT).show();
                dismissAllowingStateLoss();
            }
        });

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

    public interface OnDateTimeSetListener {
        void onDateTimeSelected(Calendar selectedDateTime);
    }


}
