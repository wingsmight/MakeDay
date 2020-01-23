package com.wingsmight.makeday;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.wingsmight.makeday.Growth.Goals.GoalsTabAdapter;
import com.wingsmight.makeday.Growth.Goals.GoalsTabFragment;
import com.wingsmight.makeday.Growth.GrowthFragment;
import com.wingsmight.makeday.MyDays.MyDaysAdapter;
import com.wingsmight.makeday.MyDays.MyDaysFragment;
import com.wingsmight.makeday.SavingSystem.SaveLoad;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class GoalNotification
{
    private static AlertDialog alertDialog;
    private static Handler notifyHandler;
    private static String goalLabel;

    public static void setEveningSkillNotify(Context context)
    {
        //show notification at evening
        long currentMilliseconds = Calendar.getInstance().getTimeInMillis();
        Date eveningDate = Calendar.getInstance().getTime();

        String eveningTime = SaveLoad.loadString(SaveLoad.defaultSavingPath, "eveningTime", "22:00");
        int eveningHour = Integer.valueOf(eveningTime.substring(0, eveningTime.indexOf(':')));
        int eveningMinutes = Integer.valueOf(eveningTime.substring(eveningTime.indexOf(':') + 1));

        eveningDate.setHours(eveningHour);
        eveningDate.setMinutes(eveningMinutes);

        long eveningMilliseconds = eveningDate.getTime();

        long millisecondsToNotify = eveningMilliseconds - currentMilliseconds;
        if(millisecondsToNotify < 0)
        {
            millisecondsToNotify += 24 * 60 * 60 * 1000;
        }

        GoalNotification.show(context, millisecondsToNotify);
    }

    public static void show(final Context context, final long millisecondsToNotify)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = createDialogView(context);
        if(dialogView == null) return;

        builder.setView(dialogView);
        alertDialog = builder.create();
        alertDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialog.getWindow().setGravity(Gravity.TOP);

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
        {
            @Override
            public void onDismiss(DialogInterface dialog)
            {
                goalIndex = 0;
                setEveningSkillNotify(context);
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = alertDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        notifyHandler = new Handler();
        notifyHandler.removeCallbacksAndMessages(null);
        notifyHandler.postDelayed(new Runnable() {
            public void run() {
                alertDialog.show();
            }
        }, millisecondsToNotify);
    }

    private static int goalIndex = 0;
    private static View createDialogView(final Context context)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View dialogView = inflater.inflate(R.layout.goal_notiffication, null);

        if(GoalsTabAdapter.goals == null)
        {
            GoalsTabAdapter.goals = SaveLoad.load(TabName.GOALS);
            for(int i = 0; i < GoalsTabAdapter.goals.size(); i++)
            {
                if (!GoalsTabAdapter.goals.get(i).getIsChecked())
                {
                    GoalsTabAdapter.goals.remove(i);
                    i--;
                }
            }

            if(GoalsTabAdapter.goals == null) return null;
        }
        if(GoalsTabAdapter.goals.size() > goalIndex)
        {
            goalLabel = GoalsTabAdapter.goals.get(goalIndex).getName();
        }
        else
        {
            return null;
        }

        final TextView goalText = dialogView.findViewById(R.id.goalText);
        goalText.setText(goalLabel);


        Button yesButton = dialogView.findViewById(R.id.yesAnswer);
        yesButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                MyDaysFragment myDaysFragment = ((MyDaysFragment)(fragmentManager.findFragmentByTag("1")));

                myDaysFragment.addDoneDealToToday(goalLabel);
                goalIndex++;
                if(goalIndex < GoalsTabAdapter.goals.size())
                {
                    goalLabel = GoalsTabAdapter.goals.get(goalIndex).getName();

                    goalText.setText(goalLabel);
                }
                else
                {
                    alertDialog.dismiss();
                }
            }
        });

        Button noButton = dialogView.findViewById(R.id.noAnswer);
        noButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                MyDaysFragment myDaysFragment = ((MyDaysFragment)(fragmentManager.findFragmentByTag("1")));

                myDaysFragment.addUndoneDealToToday(goalLabel);
                goalIndex++;
                if(goalIndex < GoalsTabAdapter.goals.size())
                {
                    goalLabel = GoalsTabAdapter.goals.get(goalIndex).getName();

                    goalText.setText(goalLabel);
                }
                else
                {
                    alertDialog.dismiss();
                }
            }
        });

        Button almostButton = dialogView.findViewById(R.id.almostAnswer);
        almostButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                MyDaysFragment myDaysFragment = ((MyDaysFragment)(fragmentManager.findFragmentByTag("1")));

                myDaysFragment.addAlmostDealToToday(goalLabel);
                goalIndex++;
                if(goalIndex < GoalsTabAdapter.goals.size())
                {
                    goalLabel = GoalsTabAdapter.goals.get(goalIndex).getName();

                    goalText.setText(goalLabel);
                }
                else
                {
                    alertDialog.dismiss();
                }
            }
        });

        TextView skipButton = dialogView.findViewById(R.id.skipButton);
        skipButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                goalIndex++;
                if(goalIndex < GoalsTabAdapter.goals.size())
                {
                    goalLabel = GoalsTabAdapter.goals.get(goalIndex).getName();

                    goalText.setText(goalLabel);
                }
                else
                {
                    alertDialog.dismiss();
                }
            }
        });

        Button doneButton = dialogView.findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                goalIndex = 0;

                alertDialog.dismiss();
            }
        });

        return dialogView;
    }
}
