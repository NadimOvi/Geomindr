package com.example.harish.geomindr.activity.reminder.view;

import android.content.Context;
import android.database.Cursor;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.harish.geomindr.R;
import com.example.harish.geomindr.database.DatabaseHelper;
import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static com.github.clans.fab.FloatingActionButton.SIZE_MINI;

public class ReminderRecyclerAdapter extends RecyclerView.Adapter<ReminderRecyclerAdapter.ViewHolder> {
    // List containing all the reminder in the database.
    public static List<Reminder> reminderList;
    // Context from which the class 'ReminderRecyclerAdapter' is called.
    private Context context;
    private SparseBooleanArray selectedRecyclerViewItems;
    private Vibrator vibratorService;
    // Total number of reminder in the database.
    private int totalRemNum;
    // Current reminder.
    // It is used for providing custom padding to the reminders displayed on the app inside CardView.
    private int currentRemNum;


    public ReminderRecyclerAdapter(Context context) {
        this.context = context;
        this.selectedRecyclerViewItems = new SparseBooleanArray();

        vibratorService = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        reminderList = new ArrayList<>();

        // Creating an object of DatabaseHelper class.
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);

        // Retrieving all records from the database.
        Cursor res = databaseHelper.getAllRecords();

        totalRemNum = res.getCount();
        currentRemNum = 1;

        // Iterating through the retrieved records.
        while(res.moveToNext()) {
            // Add the data to the 'reminderList' list.
            reminderList.add(new Reminder(res.getInt(0), res.getInt(1), res.getInt(2),
                    res.getString(3), res.getString(4), res.getString(5), res.getString(6),
                    res.getString(7), res.getString(8), res.getDouble(9), res.getDouble(10)));
        }

        res.close();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reminder_view, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Reminder reminder = reminderList.get(position);
        holder.title.setText(reminder.getTitle());
        holder.time.setText(reminder.getLocation());
        holder.description.setText(reminder.getDescription());

        // Set custom image on the FloatingActionButton on CardView displaying the reminder
        // If taskIf = 1, then it is a facebook task reminder. So, set the image accordingly.
        if (reminder.getTaskId() == 1) {
            holder.mFABIcon.setImageResource(R.drawable.ic_fab_facebook);
        }
        // If taskIf = 2, then it is a alarm task reminder. So, set the image accordingly.
        else if (reminder.getTaskId() == 2) {
            holder.mFABIcon.setImageResource(R.drawable.ic_alarm_white_24dp);
        }

        holder.itemView.setActivated(selectedRecyclerViewItems.get(position, false));
    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }

    // Methods for implementing Selection using contextActionMode

    public void toggleSelection(int position) {
        if (selectedRecyclerViewItems.get(position, false)) {
            selectedRecyclerViewItems.delete(position);
        } else {
            vibratorService.vibrate(50);
            clearSelections();
            selectedRecyclerViewItems.put(position, true);
        }

        notifyItemChanged(position);
    }

    public void clearSelections() {
        selectedRecyclerViewItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedRecyclerViewItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(selectedRecyclerViewItems.size());
        for (int i = 0; i < selectedRecyclerViewItems.size(); i++) {
            items.add(selectedRecyclerViewItems.keyAt(i));
        }

        return items;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title, time, description;
        private FloatingActionButton mFABIcon;

        ViewHolder(View view) {
            super(view);

            // Setting ActionButton attributes displayed on the CardView displaying reminder.
            mFABIcon = (FloatingActionButton) view.findViewById(R.id.fab_icon);
            mFABIcon.setButtonSize(SIZE_MINI);
            mFABIcon.setColorNormalResId(R.color.darkGray);
            mFABIcon.setColorPressedResId(R.color.darkGray);
            mFABIcon.setColorRippleResId(R.color.darkGray);

            // If it is the first reminder, then
            // give a top padding of 10dp and bottom padding of 5dp.
            if (currentRemNum == 1) {
                float scale = view.getResources().getDisplayMetrics().density;
                int paddingTop = (int) (10*scale + 0.5f);
                int paddingBottom = (int) (5*scale + 0.5f);
                view.setPadding(0, paddingTop, 0, paddingBottom);
                title = (TextView) view.findViewById(R.id.rem_title);
                time = (TextView) view.findViewById(R.id.rem_time);
                description = (TextView) view.findViewById(R.id.rem_description);
                currentRemNum++;
            }
            // If it is the last reminder, then
            // give a top padding of 5dp and bottom padding of 10dp.
            else if (currentRemNum == totalRemNum) {
                float scale = view.getResources().getDisplayMetrics().density;
                int paddingTop = (int) (5*scale + 0.5f);
                int paddingBottom = (int) (10*scale + 0.5f);
                view.setPadding(0, paddingTop, 0, paddingBottom);
                title = (TextView) view.findViewById(R.id.rem_title);
                time = (TextView) view.findViewById(R.id.rem_time);
                description = (TextView) view.findViewById(R.id.rem_description);
            }
            // Else, give a top and bottom padding of 5dp.
            else {
                float scale = view.getResources().getDisplayMetrics().density;
                int paddingTop = (int) (5*scale + 0.5f);
                int paddingBottom = (int) (5*scale + 0.5f);
                view.setPadding(0, paddingTop, 0, paddingBottom);
                title = (TextView) view.findViewById(R.id.rem_title);
                time = (TextView) view.findViewById(R.id.rem_time);
                description = (TextView) view.findViewById(R.id.rem_description);
                currentRemNum++;
            }
        }
    }
}