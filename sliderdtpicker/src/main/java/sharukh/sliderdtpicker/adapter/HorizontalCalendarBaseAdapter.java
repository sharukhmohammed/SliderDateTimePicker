package sharukh.sliderdtpicker.adapter;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

import sharukh.sliderdtpicker.HorizontalCalendar;
import sharukh.sliderdtpicker.HorizontalCalendarView;
import sharukh.sliderdtpicker.HorizontalLayoutManager;
import sharukh.sliderdtpicker.model.CalendarItemStyle;
import sharukh.sliderdtpicker.utils.HorizontalCalendarListener;
import sharukh.sliderdtpicker.utils.HorizontalCalendarPredicate;
import sharukh.sliderdtpicker.utils.Utils;

/**
 * Base class for all adapters for {@link HorizontalCalendarView HorizontalCalendarView}
 *
 * @author Mulham-Raee
 * @since v1.3.0
 */
public abstract class HorizontalCalendarBaseAdapter<VH extends DateViewHolder, T extends Calendar> extends RecyclerView.Adapter<VH> {

    private final int itemResId;
    final HorizontalCalendar horizontalCalendar;
    private final HorizontalCalendarPredicate disablePredicate;
    private final int cellWidth;
    private CalendarItemStyle disabledItemStyle;

    Calendar startDate;
    int itemsCount;

    HorizontalCalendarBaseAdapter(int itemResId, final HorizontalCalendar horizontalCalendar, Calendar startDate, Calendar endDate, HorizontalCalendarPredicate disablePredicate) {
        this.itemResId = itemResId;
        this.horizontalCalendar = horizontalCalendar;
        this.disablePredicate = disablePredicate;
        this.startDate = startDate;
        if (disablePredicate != null) {
            this.disabledItemStyle = disablePredicate.style();
        }

        cellWidth = Utils.calculateCellWidth(horizontalCalendar.getContext(), horizontalCalendar.getNumberOfDatesOnScreen());
        itemsCount = calculateItemsCount(startDate, endDate);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(itemResId, parent, false);

        final VH viewHolder = createViewHolder(itemView, cellWidth);
        viewHolder.itemView.setOnClickListener(new MyOnClickListener(viewHolder));
        viewHolder.itemView.setOnLongClickListener(new MyOnLongClickListener(viewHolder));

        return viewHolder;
    }

    protected abstract VH createViewHolder(View itemView, int cellWidth);

    public abstract T getItem(int position);

    @Override
    public int getItemCount() {
        return itemsCount;
    }

    public boolean isDisabled(int position) {
        if (disablePredicate == null) {
            return false;
        }
        Calendar date = getItem(position);
        return disablePredicate.test(date);
    }


    void applyStyle(VH viewHolder, Calendar date, int position) {
        int selectedItemPosition = horizontalCalendar.getSelectedDatePosition();

        if (disablePredicate != null) {
            boolean isDisabled = disablePredicate.test(date);
            viewHolder.itemView.setEnabled(!isDisabled);
            if (isDisabled && (disabledItemStyle != null)) {
                applyStyle(viewHolder, disabledItemStyle);
                viewHolder.selectionView.setVisibility(View.INVISIBLE);
                return;
            }
        }

        // Selected Day
        if (position == selectedItemPosition) {
            applyStyle(viewHolder, horizontalCalendar.getSelectedItemStyle());
            viewHolder.selectionView.setVisibility(View.VISIBLE);
        }
        // Unselected Days
        else {
            applyStyle(viewHolder, horizontalCalendar.getDefaultStyle());
            viewHolder.selectionView.setVisibility(View.INVISIBLE);
        }
    }

    private void applyStyle(VH viewHolder, CalendarItemStyle itemStyle) {
        viewHolder.textTop.setTextColor(itemStyle.getColorTopText());
        viewHolder.textMiddle.setTextColor(itemStyle.getColorMiddleText());
        viewHolder.textBottom.setTextColor(itemStyle.getColorBottomText());

        if (Build.VERSION.SDK_INT >= 16) {
            viewHolder.itemView.setBackground(itemStyle.getBackground());
        } else {
            viewHolder.itemView.setBackgroundDrawable(itemStyle.getBackground());
        }
    }

    public void update(Calendar startDate, Calendar endDate, boolean notify) {
        this.startDate = startDate;
        itemsCount = calculateItemsCount(startDate, endDate);
        if (notify) {
            notifyDataSetChanged();
        }
    }

    protected abstract int calculateItemsCount(Calendar startDate, Calendar endDate);

    private class MyOnClickListener implements View.OnClickListener {
        private final RecyclerView.ViewHolder viewHolder;

        MyOnClickListener(RecyclerView.ViewHolder viewHolder) {
            this.viewHolder = viewHolder;
        }

        @Override
        public void onClick(View v) {
            int position = viewHolder.getAdapterPosition();
            if (position == -1)
                return;

            horizontalCalendar.getCalendarView().setSmoothScrollSpeed(HorizontalLayoutManager.SPEED_SLOW);
            horizontalCalendar.centerCalendarToPosition(position);
        }
    }

    private class MyOnLongClickListener implements View.OnLongClickListener {
        private final RecyclerView.ViewHolder viewHolder;

        MyOnLongClickListener(RecyclerView.ViewHolder viewHolder) {
            this.viewHolder = viewHolder;
        }

        @Override
        public boolean onLongClick(View v) {
            HorizontalCalendarListener calendarListener = horizontalCalendar.getCalendarListener();
            if (calendarListener == null) {
                return false;
            }

            int position = viewHolder.getAdapterPosition();
            Calendar date = getItem(position);

            return calendarListener.onDateLongClicked(date, position);
        }
    }
}
