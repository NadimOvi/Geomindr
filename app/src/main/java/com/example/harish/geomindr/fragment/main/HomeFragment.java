package com.example.harish.geomindr.fragment.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.harish.geomindr.R;
import com.example.harish.geomindr.activity.reminder.view.ReminderRecyclerAdapter;
import com.example.harish.geomindr.activity.tbr.alarm.AlarmTask;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

public class HomeFragment extends Fragment implements RecyclerView.OnItemTouchListener, View.OnClickListener{
    RecyclerView taskReminderList;
    ReminderRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Setting the FloatingActionMenu and FloatingActionButtons.
        final FloatingActionMenu addReminderFAM = (FloatingActionMenu)
                view.findViewById(R.id.add_reminder);
        final FloatingActionButton fabTBR = (FloatingActionButton)
                view.findViewById(R.id.fab_tbr);
        final FloatingActionButton fabEBR = (FloatingActionButton)
                view.findViewById(R.id.fab_ebr);
        final FloatingActionButton fabTBRFacebook = new FloatingActionButton(getContext());
        final FloatingActionButton fabTBRAlarm = new FloatingActionButton(getContext());
        final FloatingActionButton fabTBRMessage = new FloatingActionButton(getContext());

        // RecyclerViewObject.
        taskReminderList = (RecyclerView) view.findViewById(R.id.reminder_list);

        // Setting the 'Facebook Task' FloatingActionButton programmatically
        // because we will show it only if user clicks on 'Task Based Reminder' FloatingActionButton.
        fabTBRFacebook.setImageResource(R.drawable.ic_fab_facebook);
        fabTBRFacebook.setButtonSize(FloatingActionButton.SIZE_MINI);
        fabTBRFacebook.setLabelText("Facebook Task");
        fabTBRFacebook.setColorNormalResId(R.color.colorPrimaryDark);
        fabTBRFacebook.setColorPressedResId(R.color.colorPrimaryDark);
        fabTBRFacebook.setColorRippleResId(R.color.colorPrimaryDark);

        // Setting the 'Alarm Task' FloatingActionButton programmatically
        // because we will show it only if user clicks on 'Task Based Reminder' FloatingActionButton.
        fabTBRAlarm.setImageResource(R.drawable.ic_fab_alarm);
        fabTBRAlarm.setButtonSize(FloatingActionButton.SIZE_MINI);
        fabTBRAlarm.setLabelText("Alarm Task");
        fabTBRAlarm.setColorNormalResId(R.color.colorPrimaryDark);
        fabTBRAlarm.setColorPressedResId(R.color.colorPrimaryDark);
        fabTBRAlarm.setColorRippleResId(R.color.colorPrimaryDark);

        // Setting the 'Message Task' FloatingActionButton programmatically
        // because we will show it only if user clicks on 'Task Based Reminder' FloatingActionButton.
        fabTBRMessage.setImageResource(R.drawable.ic_textsms_white_24dp);
        fabTBRMessage.setButtonSize(FloatingActionButton.SIZE_MINI);
        fabTBRMessage.setLabelText("Message Task");
        fabTBRMessage.setColorNormalResId(R.color.colorPrimaryDark);
        fabTBRMessage.setColorPressedResId(R.color.colorPrimaryDark);
        fabTBRMessage.setColorRippleResId(R.color.colorPrimaryDark);

        // If the 'Task Based Reminder' FloatingActionButton is clicked, then
        // add or remove the above 3 buttons programmatically.
        fabTBR.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (fabTBRFacebook.getParent() == null
                        && fabTBRAlarm.getParent() == null
                        && fabTBRMessage.getParent() == null) {
                    addReminderFAM.addMenuButton(fabTBRFacebook, 0);
                    addReminderFAM.addMenuButton(fabTBRAlarm, 1);
                    addReminderFAM.addMenuButton(fabTBRMessage, 2);
                }
                else {
                    addReminderFAM.removeMenuButton(fabTBRFacebook);
                    addReminderFAM.removeMenuButton(fabTBRAlarm);
                    addReminderFAM.removeMenuButton(fabTBRMessage);
                }
            }
        });

        fabEBR.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });

        // Start 'FacebookTask' activity to add a new facebook task reminder.
        fabTBRFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Start 'AlarmTask' activity to add a new alarm task reminder.
        fabTBRAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AlarmTask.class);
                startActivity(intent);
            }
        });

        // Start 'MessageTask' activity to add a new message task reminder.
        fabTBRMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Setting up the RecyclerView with reminders present in the database.
        adapter = new ReminderRecyclerAdapter(getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        taskReminderList.setLayoutManager(mLayoutManager);
        taskReminderList.setItemAnimator(new DefaultItemAnimator());
        taskReminderList.setAdapter(adapter);
        taskReminderList.addOnItemTouchListener(this);
        adapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    @Override
    public void onClick(View v) {

    }
}