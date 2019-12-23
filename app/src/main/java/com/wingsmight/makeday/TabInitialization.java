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
        subSkills.add(new Skill("Чтение текстов", true));
        subSkills.add(new Skill("Просмотр видео", true));
        subSkills.add(new Skill("Прослушивание аудио", true));
        subSkills.add(new Skill("Работа с приложением", true));
        subSkills.add(new Skill("Занятие с преподавателем", true));
        genericSkills.add(new GenericSkill("Иностранный язык", SkillCheckType.ALLCHECK, subSkills));

        subSkills = new ArrayList<>();
        subSkills.add(new Skill("Молитва (за своё)", true));
        subSkills.add(new Skill("Молитва за других", true));
        subSkills.add(new Skill("Чтение Библии", true));
        subSkills.add(new Skill("Чтение духовной литературы", true));
        subSkills.add(new Skill("Просмотр видео", true));
        subSkills.add(new Skill("Прослушивание аудио", true));
        genericSkills.add(new GenericSkill("Христианские практики", SkillCheckType.ALLCHECK, subSkills));


        return genericSkills;
    }

    public static ArrayList<Goal> initGoals()
    {
        ArrayList<Goal> goals = new ArrayList<>();

        return goals;
    }

    public static ArrayList<TimeTrackingDay<Event>> initTimeTrackingTab()
    {
        ArrayList<TimeTrackingDay<Event>> itemList = new ArrayList<>();

        return itemList;
    }

    private static ArrayList<Event> buildSubItemList()
    {
        ArrayList<Event> subItemList = new ArrayList<>();

        return subItemList;
    }

    public static ArrayList<TimeTrackingDay<EmotionEvent>> initEmotionsTab()
    {
        ArrayList<TimeTrackingDay<EmotionEvent>> itemList = new ArrayList<>();

        return itemList;
    }

    private static ArrayList<EmotionEvent> buildSubItemListEmotion()
    {
        ArrayList<EmotionEvent> subItemList = new ArrayList<>();

        return subItemList;
    }
}
