package com.example.harish.geomindr.fragment.main;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
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
    RecyclerView mTaskReminderList;
    ReminderRecyclerAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        final FloatingActionMenu addReminderFAM = (FloatingActionMenu)
                view.findViewById(R.id.addReminderFAM);
        final FloatingActionButton fabTBR = (FloatingActionButton)
                view.findViewById(R.id.fabTBR);
        final FloatingActionButton fabEBR = (FloatingActionButton)
                view.findViewById(R.id.fabEBR);
        final FloatingActionButton fabTBRFacebook = new FloatingActionButton(getContext());
        final FloatingActionButton fabTBRAlarm = new FloatingActionButton(getContext());
        final FloatingActionButton fabTBRMessage = new FloatingActionButton(getContext());

        mTaskReminderList = (RecyclerView) view.findViewById(R.id.taskReminderList);

        fabTBRFacebook.setImageResource(R.drawable.ic_fab_facebook);
        fabTBRFacebook.setButtonSize(FloatingActionButton.SIZE_MINI);
        fabTBRFacebook.setLabelText("Facebook Task");
        fabTBRFacebook.setColorNormalResId(R.color.colorPrimaryDark);
        fabTBRFacebook.setColorPressedResId(R.color.colorPrimaryDark);
        fabTBRFacebook.setColorRippleResId(R.color.colorPrimaryDark);

        fabTBRAlarm.setImageResource(R.drawable.ic_fab_alarm);
        fabTBRAlarm.setButtonSize(FloatingActionButton.SIZE_MINI);
        fabTBRAlarm.setLabelText("Alarm Task");
        fabTBRAlarm.setColorNormalResId(R.color.colorPrimaryDark);
        fabTBRAlarm.setColorPressedResId(R.color.colorPrimaryDark);
        fabTBRAlarm.setColorRippleResId(R.color.colorPrimaryDark);

        fabTBRMessage.setImageResource(R.drawable.ic_textsms_white_24dp);
        fabTBRMessage.setButtonSize(FloatingActionButton.SIZE_MINI);
        fabTBRMessage.setLabelText("Message Task");
        fabTBRMessage.setColorNormalResId(R.color.colorPrimaryDark);
        fabTBRMessage.setColorPressedResId(R.color.colorPrimaryDark);
        fabTBRMessage.setColorRippleResId(R.color.colorPrimaryDark);

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

        fabTBRFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        fabTBRAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AlarmTask.class);
                startActivity(intent);
            }
        });

        fabTBRMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mAdapter = new ReminderRecyclerAdapter(getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mTaskReminderList.setLayoutManager(mLayoutManager);
        mTaskReminderList.setItemAnimator(new DefaultItemAnimator());
        mTaskReminderList.setAdapter(mAdapter);
        mTaskReminderList.addOnItemTouchListener(this);
        mAdapter.notifyDataSetChanged();

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