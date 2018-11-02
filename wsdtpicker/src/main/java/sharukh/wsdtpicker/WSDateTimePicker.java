package sharukh.wsdtpicker;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class WSDateTimePicker extends BottomSheetDialogFragment {

    /* Private Fields
     * */
    private static final String TAG = "BottomSlidingDateTime";
    private static final String INPUT_TITLE = "Title";
    private Calendar selectedCal;
    private OnDateTimeSetListener dateTimeSetListener;

    /* Public Usage Fields
     * */
    public static String fragTitle = "";

    public static WSDateTimePicker newInstance(String title) {

        Bundle args = new Bundle();
        args.putString(INPUT_TITLE, title);
        WSDateTimePicker fragment = new WSDateTimePicker();
        fragment.setArguments(args);
        return fragment;
    }

    public void setOnDateTimeSetListener(OnDateTimeSetListener onDateTimeSetListener) {
        if (onDateTimeSetListener != null) {
            this.dateTimeSetListener = onDateTimeSetListener;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_date_time_picker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            fragTitle = getArguments().getString(INPUT_TITLE);
        }

        final TextView selected_date_time = view.findViewById(R.id.frag_title);
        selected_date_time.setText(fragTitle);

        selectedCal = Calendar.getInstance();

        /* starts after 1 hour from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.HOUR, 1);
        startDate.set(Calendar.MINUTE, 0);
        startDate.set(Calendar.SECOND, 0);

        /* ends after 2 years from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.YEAR, 2);

        HorizontalCalendar date_picker = new HorizontalCalendar.Builder(view, R.id.frag_date_picker)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .build();


        final TimeAdapter adapter = new TimeAdapter(selectedCal.get(Calendar.HOUR_OF_DAY), new TimeAdapter.OnTimeSelectedListener() {
            @Override
            public void onTimeSelected(int hourOfTheDay) {
                selectedCal.set(Calendar.HOUR_OF_DAY, hourOfTheDay);
            }
        });

        final RecyclerView time_recycler = view.findViewById(R.id.frag_time_picker);
        time_recycler.setAdapter(adapter);

        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(time_recycler);

        date_picker.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                selectedCal = (Calendar) date.clone();

                int today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

                if (selectedCal.get(Calendar.DAY_OF_MONTH) == today) {
                    adapter.setDisableBefore(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
                } else {
                    adapter.setDisableBefore(-1);
                }

                int hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                time_recycler.smoothScrollToPosition(hourOfDay);

            }
        });

        //Calling this for first time
        date_picker.selectDate(startDate, false);


        time_recycler.smoothScrollToPosition(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));


        Button done_button = view.findViewById(R.id.frag_done);
        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateTimeSetListener.onDateTimeSelected(selectedCal);
                dismissAllowingStateLoss();
            }
        });


    }


    private void initVars() {

    }

    private void initViews() {

    }


    public interface OnDateTimeSetListener {
        void onDateTimeSelected(Calendar selectedDateTime);
    }


}
