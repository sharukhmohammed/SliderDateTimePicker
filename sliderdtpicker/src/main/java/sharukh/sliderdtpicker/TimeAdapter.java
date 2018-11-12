package sharukh.sliderdtpicker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.Holder> {

    public static final int DISABLE_NOTHING = -1;
    private final ArrayList<String> times = new ArrayList<String>() {{
        add("12 AM");
        add("1 AM");
        add("2 AM");
        add("3 AM");
        add("4 AM");
        add("5 AM");
        add("6 AM");
        add("7 AM");
        add("8 AM");
        add("9 AM");
        add("10 AM");
        add("11 AM");
        add("12 PM");
        add("1 PM");
        add("2 PM");
        add("3 PM");
        add("4 PM");
        add("5 PM");
        add("6 PM");
        add("7 PM");
        add("8 PM");
        add("9 PM");
        add("10 PM");
        add("11 PM");
    }};

    private int selectedPos;
    private int disableBefore = -1;
    private OnTimeSelectedListener onTimeSelected;
    private Context context;

    TimeAdapter(int selectedPos, OnTimeSelectedListener onTimeSelectedListener) {

        if (onTimeSelectedListener != null)
            this.onTimeSelected = onTimeSelectedListener;
        else
            throw new IllegalStateException("Not implemented");

        this.selectedPos = selectedPos;
    }

    void setDisableBefore(Calendar setCal, @Nullable Date startDate) {

        Calendar    disableBeforeCal = Calendar.getInstance();
        if(startDate!=null)
        {
            disableBeforeCal.setTime(startDate);
        }

        int thisDay = disableBeforeCal.get(Calendar.DAY_OF_MONTH);
        int thisMonth = disableBeforeCal.get(Calendar.MONTH);
        int thisYear = disableBeforeCal.get(Calendar.YEAR);

        int thisHour = disableBeforeCal.get(Calendar.HOUR_OF_DAY);

        if (setCal.get(Calendar.DAY_OF_MONTH) == thisDay && setCal.get(Calendar.MONTH) == thisMonth && setCal.get(Calendar.YEAR) == thisYear) {
            this.disableBefore = thisHour;
            if (selectedPos < thisHour) {
                setCal.set(Calendar.HOUR_OF_DAY, thisHour + 1);
                this.selectedPos = thisHour + 1;
            } else {
                setCal.set(Calendar.HOUR_OF_DAY, selectedPos);
            }
        } else {
            this.disableBefore = DISABLE_NOTHING;
        }
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        return new Holder(
                LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_time, viewGroup, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int pos) {
        holder.timeText.setText(times.get(pos));

        if (pos > disableBefore) {
            if (pos == selectedPos) {
                holder.itemView.setBackground(ContextCompat.getDrawable(context, R.drawable.gradient_primary));
                holder.timeText.setTextColor(ContextCompat.getColor(context, R.color.white));
            } else {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                holder.timeText.setTextColor(ContextCompat.getColor(context, R.color.text_dark));
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedPos = holder.getAdapterPosition();
                    onTimeSelected.onTimeSelected(holder.getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
        } else {
            holder.timeText.setTextColor(ContextCompat.getColor(context, R.color.text_medium));
            holder.itemView.setBackground(ContextCompat.getDrawable(context, R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return times.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView timeText;

        Holder(@NonNull View itemView) {
            super(itemView);
            timeText = itemView.findViewById(R.id.item_time_text);
        }
    }

    interface OnTimeSelectedListener {
        void onTimeSelected(int hourOfTheDay);
    }


}
