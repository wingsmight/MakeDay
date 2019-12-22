package com.wingsmight.makeday;

import com.wingsmight.makeday.Growth.Goals.Goal;
import com.wingsmight.makeday.Growth.Skills.GenericSkill;
import com.wingsmight.makeday.Growth.Skills.Skill;
import com.wingsmight.makeday.Growth.Skills.SkillCheckType;
import com.wingsmight.makeday.Tracker.Emotions.EmotionEvent;
import com.wingsmight.makeday.Tracker.Event;
import com.wingsmight.makeday.Tracker.RowDayModel;
import com.wingsmight.makeday.Tracker.TimeTracking.TimeTrackingDay;

import java.util.ArrayList;
import java.util.Random;

public class TabInitialization
{
    public static ArrayList<RowDayModel> initMyDays()
    {
        ArrayList<RowDayModel> days = new ArrayList<>();
        String[] doneDeals = {
                "дело 1",
                "дело 2"
        };
        String[] undoneDeals = {
                "дело 3"
        };
        RowDayModel rowDayModel = new RowDayModel(16, 5, 2019, doneDeals, undoneDeals, "ноября");
        days.add(rowDayModel);

        String[] doneDeals2 = {
                "дело 1"
        };
        String[] undoneDeals2 = {
                "дело 2",
                "дело 3"
        };
        RowDayModel rowDayModel2 = new RowDayModel(15, 4, 2019, doneDeals2, undoneDeals2, "ноября");
        days.add(rowDayModel2);

        days.add(rowDayModel2);
        days.add(rowDayModel2);
        days.add(rowDayModel2);
        days.add(rowDayModel2);
        days.add(rowDayModel2);
        days.add(rowDayModel2);

        return days;
    }


    public static ArrayList<GenericSkill> initGenericSkills()
    {
        ArrayList<GenericSkill> genericSkills = new ArrayList<>();

        ArrayList<Skill> subSkills = new ArrayList<>();
        subSkills.add(new Skill("Успехи", true));
        subSkills.add(new Skill("Идеи", true));
        subSkills.add(new Skill("Осознания / инсайты", true));
        genericSkills.add(new GenericSkill("Дневник Успехов", SkillCheckType.ALLCHECK, subSkills));

        subSkills = new ArrayList<>();
        subSkills.add(new Skill("Поднавык 1", true));
        subSkills.add(new Skill("Поднавык 2", true));
        genericSkills.add(new GenericSkill("Навык 2", SkillCheckType.ALLCHECK, subSkills));

        return genericSkills;
    }

    public static ArrayList<Goal> initGoals()
    {
        ArrayList<Goal> goals = new ArrayList<>();
        for (int i = 1; i < 12; i++)
        {
            goals.add(new Goal("Цель " + i + "...", true));
        }

        return goals;
    }

    public static ArrayList<TimeTrackingDay<Event>> initTimeTrackingTab() {
        ArrayList<TimeTrackingDay<Event>> itemList = new ArrayList<>();
        for (int i=1; i<10; i++) {
            TimeTrackingDay<Event> item = new TimeTrackingDay<Event>(i, i < 6 ? i : i - 5, 2019, buildSubItemList(), 11);
            itemList.add(item);
        }
        return itemList;
    }

    private static ArrayList<Event> buildSubItemList() {
        ArrayList<Event> subItemList = new ArrayList<>();
        Random random = new Random();
        int randomCount = random.nextInt(7) + 3;
        for (int i=1; i < randomCount; i++) {
            Event subItem = new Event(i, 11, 2019, i, 0, i+1, 0, "Пример");
            subItemList.add(subItem);
        }

        return subItemList;
    }

    public static ArrayList<TimeTrackingDay<EmotionEvent>> initEmotionsTab() {
        ArrayList<TimeTrackingDay<EmotionEvent>> itemList = new ArrayList<>();
        for (int i=1; i<10; i++) {
            TimeTrackingDay<EmotionEvent> item = new TimeTrackingDay<EmotionEvent>(i, i < 6 ? i : i - 5, 2019, buildSubItemListEmotion(), 12);
            itemList.add(item);
        }
        return itemList;
    }

    private static ArrayList<EmotionEvent> buildSubItemListEmotion() {
        ArrayList<EmotionEvent> subItemList = new ArrayList<>();
        Random random = new Random();
        int randomCount = random.nextInt(7) + 3;
        int randomEmotion;
        for (int i=1; i < randomCount; i++) {
            randomEmotion = random.nextInt(6) - 3;
            EmotionEvent subItem = new EmotionEvent(i, 11, 2019, i, 0, i+1, 0, String.valueOf(randomEmotion));
            subItemList.add(subItem);
        }
        return subItemList;
    }
}
