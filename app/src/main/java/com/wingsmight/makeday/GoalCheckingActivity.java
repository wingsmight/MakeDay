package com.wingsmight.makeday;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.wingsmight.makeday.Growth.Goals.GoalsTabAdapter;
import com.wingsmight.makeday.MyDays.MyDaysFragment;
import com.wingsmight.makeday.SavingSystem.SaveLoad;

import java.util.Calendar;
import java.util.Date;

public class GoalCheckingActivity extends AppCompatActivity
{
    private static AlertDialog alertDialog;
    private static String goalLabel;

    private static MyDaysFragment myDaysFragment;

    private Date day;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        day = Calendar.getInstance().getTime();
        show();
    }

    public static void setEveningSkillNotify(Context context, Calendar eveningDate)
    {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        MyDaysFragment myDaysFragmentTest = ((MyDaysFragment) (fragmentManager.findFragmentByTag("1")));
        if (myDaysFragmentTest != null)
        {
            myDaysFragment = myDaysFragmentTest;
        }

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, GoalNotificationReceiver.class);

        int requestCode = 1011;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent,0);

        //Устанавливаем интервал срабатывания в 1 минуту
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, eveningDate.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void closeWindow()
    {
        this.finish();
    }

    public void show()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = createDialogView(this);
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
                //setEveningSkillNotify(context);
                closeWindow();
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = alertDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        alertDialog.show();

//        HandlerThread handlerThread = new HandlerThread("background-thread");
//        handlerThread.start();
//        Handler notifyHandler = new Handler(handlerThread.getLooper());
//        notifyHandler.removeCallbacksAndMessages(null);
//        notifyHandler.post(new Runnable() {
//            public void run() {
//                alertDialog.show();
//            }
//        });
    }

    private int goalIndex = 0;
    private View createDialogView(final Context context)
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
                myDaysFragment.addDoneDealToDay(day, goalLabel);
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

                myDaysFragment.backgroundSave();
            }
        });

        Button noButton = dialogView.findViewById(R.id.noAnswer);
        noButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                myDaysFragment.addUndoneDealToDay(day, goalLabel);
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

                myDaysFragment.backgroundSave();
            }
        });

        Button almostButton = dialogView.findViewById(R.id.almostAnswer);
        almostButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                myDaysFragment.addAlmostDealToDay(day, goalLabel);
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

                myDaysFragment.backgroundSave();
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
                alertDialog.dismiss();
            }
        });

        return dialogView;
    }
}
