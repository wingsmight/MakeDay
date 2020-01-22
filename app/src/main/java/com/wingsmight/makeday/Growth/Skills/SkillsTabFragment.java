package com.wingsmight.makeday.Growth.Skills;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wingsmight.makeday.DragNDropExpandableListView.DragListener;
import com.wingsmight.makeday.DragNDropExpandableListView.DragNDropListView;
import com.wingsmight.makeday.DragNDropExpandableListView.DropListener;
import com.wingsmight.makeday.DragNDropExpandableListView.RemoveListener;
import com.wingsmight.makeday.GoalNotification;
import com.wingsmight.makeday.MainActivity;
import com.wingsmight.makeday.R;
import com.wingsmight.makeday.SavingSystem.SaveLoad;
import com.wingsmight.makeday.TabName;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class SkillsTabFragment extends Fragment implements ExpandableListView.OnGroupExpandListener
{
    public static TabName tabName = TabName.SKILLS;
    private ExpandableListView expandableListView;
    private ExpandableListView nonCheckedView;
    private SkillsTabAdapter expandableListAdapter;
    private NonCheckedSkillsAdapter nonCheckedListAdapter;
    ArrayList<GenericSkill> genericSkills;
    ArrayList<GenericSkill> nonCheckedGenericSkills = new ArrayList();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_skills, container, false);
    }

    FloatingActionButton addButton;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        //Add button
        addButton = view.findViewById(R.id.addSkillButton);
        addButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AddSkill(view);
            }
        });

        //Expandable list
        genericSkills = SaveLoad.load(tabName);//initGenericSkills();
        for(int i = 0; i < genericSkills.size(); i++)
        {
            if (genericSkills.get(i).getCheckType() != SkillCheckType.ALLCHECK)
            {
                nonCheckedGenericSkills.add(genericSkills.remove(i));
                i--;
            }
        }

        nonCheckedListAdapter = new NonCheckedSkillsAdapter(getActivity(), this, nonCheckedGenericSkills);

        expandableListView = view.findViewById(android.R.id.list);
        expandableListAdapter = new SkillsTabAdapter(getActivity(), this, genericSkills, nonCheckedListAdapter);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(this);

        for (GenericSkill genericSkill : genericSkills)
        {
            genericSkill.setIsExpanded(false);
        }

        expandableListView.setIndicatorBounds(-1000, 0);

        //ActionBar
        setHasOptionsMenu(true);

        //Drag n drop
        if (expandableListView instanceof DragNDropListView)
        {
            ((DragNDropListView) expandableListView).setDropListener(mDropListener);
            ((DragNDropListView) expandableListView).setRemoveListener(mRemoveListener);
            ((DragNDropListView) expandableListView).setDragListener(mDragListener);
            ((DragNDropListView) expandableListView).setOnGroupExpandListener(mOnExpandListener);
            ((DragNDropListView) expandableListView).setOnGroupCollapseListener(mOnCollapseListener);
        }

        nonCheckedView = view.findViewById(R.id.NonCheckedList);
        nonCheckedView.setVisibility(View.GONE);
        nonCheckedListAdapter.addSkillTabAdapter(expandableListAdapter);
        nonCheckedView.setAdapter(nonCheckedListAdapter);
    }

    private int lastExpandedPosition = -1;
    private ExpandableListView.OnGroupExpandListener mOnExpandListener= new ExpandableListView.OnGroupExpandListener()
    {
        @Override
        public void onGroupExpand(int groupPosition)
        {
            genericSkills.get(groupPosition).setIsExpanded(true);

            if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition)
            {
                expandableListView.collapseGroup(lastExpandedPosition);
            }
            lastExpandedPosition = groupPosition;
        }
    };

    private ExpandableListView.OnGroupCollapseListener mOnCollapseListener= new ExpandableListView.OnGroupCollapseListener()
    {
        @Override
        public void onGroupCollapse(int groupPosition)
        {
            genericSkills.get(groupPosition).setIsExpanded(false);
        }
    };


    private DropListener mDropListener =
            new DropListener() {
                public void onDrop(int from, int to) {

                    if (expandableListAdapter instanceof SkillsTabAdapter) {
                        ((SkillsTabAdapter)expandableListAdapter).onDrop(from, to);
                        expandableListView.invalidateViews();
                    }
                }

                public void onDropChild(int from, int to) {

                    if (expandableListAdapter instanceof SkillsTabAdapter) {
                        ((SkillsTabAdapter)expandableListAdapter).onDropChild(from, to);
                        expandableListView.invalidateViews();
                    }
                }

                @Override
                public void onSwap(int from, int to, ListView listView, int prevPosition)
                {

                    if (expandableListAdapter instanceof SkillsTabAdapter) {
                        ((SkillsTabAdapter)expandableListAdapter).onSwap(from, to, listView, prevPosition);
                        expandableListView.invalidateViews();
                    }
                }

                @Override
                public void onSwapChild(int from, int to, ListView listView, int prevPosition)
                {

                    if (expandableListAdapter instanceof SkillsTabAdapter) {
                        ((SkillsTabAdapter)expandableListAdapter).onSwapChild(from, to, listView, prevPosition);
                        expandableListView.invalidateViews();
                    }
                }

                @Override
                public void onStartDrag(int from, ListView listView)
                {
                    if (expandableListAdapter instanceof SkillsTabAdapter) {
                        ((SkillsTabAdapter)expandableListAdapter).onStartDrag(from, listView);
                        expandableListView.invalidateViews();
                    }
                }
            };

    private RemoveListener mRemoveListener =
            new RemoveListener() {
                public void onRemove(int which) {
                    if (expandableListAdapter instanceof SkillsTabAdapter) {
                        ((SkillsTabAdapter)expandableListAdapter).onRemove(which);
                        expandableListView.invalidateViews();
                    }
                }
            };

    public void remove(final int index)
    {
        Animation anim = AnimationUtils.loadAnimation(
                getActivity(), android.R.anim.slide_out_right
        );
        anim.setDuration(350);

        expandableListView.getChildAt(index - expandableListView.getFirstVisiblePosition()).startAnimation(anim);
        expandableListView.collapseGroup(index);

        new Handler().postDelayed(new Runnable() {

            public void run() {

//                FavouritesManager.getInstance().remove(
//                        FavouritesManager.getInstance().getTripManagerAtIndex(index)
//                );
                //populateList();
                expandableListAdapter.update();

            }

        }, anim.getDuration());
    }

    public void removeNonChecked(final int index)
    {
        Animation anim = AnimationUtils.loadAnimation(
                getActivity(), android.R.anim.slide_out_right
        );
        anim.setDuration(350);

        nonCheckedView.getChildAt(index - nonCheckedView.getFirstVisiblePosition()).startAnimation(anim);
        nonCheckedView.collapseGroup(index);

        new Handler().postDelayed(new Runnable() {

            public void run()
            {
                nonCheckedListAdapter.update();

            }

        }, anim.getDuration());
    }
    int prevEmptySpacePosition = -1;
    private DragListener mDragListener =
            new DragListener() {

                int backgroundColor = MainActivity.GetContext().getResources().getColor(R.color.colorBackground);
                int defaultBackgroundColor;

                public void onDrag(int x, int y, ListView listView)
                {

                }

                public void onStartDrag(View itemView)
                {
                    prevEmptySpacePosition = -1;
//                for(int i = 0; i < genericSkills.size(); i++)
//                {
//                    getExpandableListView().collapseGroup(i);
//                }

                    defaultBackgroundColor = itemView.getDrawingCacheBackgroundColor();
                    itemView.setBackgroundColor(backgroundColor);

                    int layoutId = itemView.getId();

                    if(layoutId == R.id.rowGenericSkillLayout)
                    {
                        CheckBox checkBox = itemView.findViewById(R.id.genericSkillCheckBox);
                        if(checkBox != null)
                        {
                            checkBox.setVisibility(View.VISIBLE);
                        }

                        TextView skillText = itemView.findViewById(R.id.genericSkillName);
                        if(skillText != null)
                        {
                            skillText.setVisibility(View.VISIBLE);
                            skillText.setTextColor(getResources().getColor(android.R.color.tab_indicator_text));
                        }

                        ImageView deleteGenericSkill = itemView.findViewById(R.id.deleteGenericSkill);
                        if(deleteGenericSkill != null)
                        {
                            deleteGenericSkill.setVisibility(View.GONE);
                        }
                    }
                    else if(layoutId == R.id.rowSkillLayout)
                    {
                        CheckBox checkBox = itemView.findViewById(R.id.skillCheckBox);
                        if(checkBox != null)
                        {
                            checkBox.setVisibility(View.VISIBLE);
                        }

                        TextView skillText = itemView.findViewById(R.id.skillName);
                        if(skillText != null)
                        {
                            skillText.setVisibility(View.VISIBLE);
                            skillText.setTextColor(getResources().getColor(android.R.color.tab_indicator_text));
                        }
                    }
                }

                public void onStopDrag(View itemView)
                {
                    if(itemView == null) return;

                    itemView.setVisibility(View.VISIBLE);
                    itemView.setBackgroundColor(defaultBackgroundColor);

                    if(itemView.getId() == R.id.rowGenericSkillLayout)
                    {
                        CheckBox checkBox = itemView.findViewById(R.id.genericSkillCheckBox);
                        checkBox.setVisibility(View.VISIBLE);

                        TextView skillText = itemView.findViewById(R.id.genericSkillName);
                        skillText.setVisibility(View.VISIBLE);
                        skillText.setTextColor(getResources().getColor(android.R.color.tab_indicator_text));

                        ImageView deleteGenericSkill = itemView.findViewById(R.id.deleteGenericSkill);
                        if(deleteGenericSkill != null)
                        {
                            deleteGenericSkill.setVisibility(View.VISIBLE);
                        }
                    }
                    else if(itemView.getId() == R.id.rowSkillLayout)
                    {
                        CheckBox checkBox = itemView.findViewById(R.id.skillCheckBox);
                        checkBox.setVisibility(View.VISIBLE);

                        TextView skillText = itemView.findViewById(R.id.skillName);
                        skillText.setVisibility(View.VISIBLE);
                        skillText.setTextColor(getResources().getColor(android.R.color.tab_indicator_text));

                        ImageView deleteSkill = itemView.findViewById(R.id.deleteSkill);
                        if(deleteSkill != null)
                        {
                            deleteSkill.setVisibility(View.VISIBLE);
                        }
                    }
                }

            };

    public void removeChild(final int groupPosition, final int childPosition)
    {
        Animation anim = AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_out_right);
        anim.setDuration(350);
        expandableListAdapter.getChildView(groupPosition, childPosition, childPosition == expandableListAdapter.getChildrenCount(groupPosition) - 1,
                null, (ViewGroup) expandableListView).startAnimation(anim);

        new Handler().postDelayed(new Runnable() {

            public void run()
            {
                ((SkillsTabAdapter)expandableListAdapter).update();

            }

        }, anim.getDuration());
    }

    public void removeNonCheckedChild(final int groupPosition, final int childPosition)
    {
        Animation anim = AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_out_right);
        anim.setDuration(350);
        nonCheckedListAdapter.getChildView(groupPosition, childPosition, childPosition == nonCheckedListAdapter.getChildrenCount(groupPosition) - 1,
                null, (ViewGroup) nonCheckedView).startAnimation(anim);

        new Handler().postDelayed(new Runnable() {

            public void run()
            {
                nonCheckedListAdapter.update();

            }

        }, anim.getDuration());
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu)
    {
        final MenuItem itemEdit = menu.findItem(R.id.edit);
        final MenuItem itemSave = menu.findItem(R.id.save);
        if(itemEdit!=null)
        {
            itemEdit.setVisible(!isEditMode);
            itemEdit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
            {
                @Override
                public boolean onMenuItemClick(MenuItem item)
                {
                    itemEdit.setVisible(false);
                    itemSave.setVisible(true);

                    startEditMode();

                    return true;
                }
            });
        }

        if(itemSave!=null)
        {
            itemSave.setVisible(isEditMode);
            itemSave.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
            {
                @Override
                public boolean onMenuItemClick(MenuItem item)
                {
                    itemEdit.setVisible(true);
                    itemSave.setVisible(false);

                    saveEditMode();

                    return true;
                }
            });
        }
    }

    public static boolean isEditMode = false;
    private void startEditMode()
    {
        isEditMode = true;
        expandableListAdapter.update();
        nonCheckedView.setVisibility(View.VISIBLE);
        addButton.hide();
    }

    private void saveEditMode()
    {
        isEditMode = false;
        expandableListAdapter.update();
        nonCheckedView.setVisibility(View.GONE);

        Toast.makeText(getActivity(), "Измения сохранены", Toast.LENGTH_SHORT);
        addButton.show();
    }

    private void backgroundSave(){
        Thread backgroundThread = new Thread() {
            @Override
            public void run()
            {
                genericSkills.addAll(nonCheckedGenericSkills);
                SaveLoad.save(tabName, genericSkills);
            }
        };
        backgroundThread.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        backgroundSave();
    }

    @Override
    public void onGroupExpand(int groupPosition) {
        if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition)
        {
            expandableListView.collapseGroup(lastExpandedPosition);
        }
        lastExpandedPosition = groupPosition;
    }


    View dialogView;
    AlertDialog popupDialog;
    private void AddSkill(View view)
    {
        //Show popup
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        dialogView = getLayoutInflater().inflate(R.layout.add_skill_dialog, null);
        builder.setView(dialogView);
        popupDialog = builder.create();
        popupDialog.show();

        ImageButton sendButton = dialogView.findViewById(R.id.dialog_like_bt);
        sendButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TextInputEditText inputEditText = ((View)v.getParent()).findViewById(R.id.newGoalInput);
                AddSkillFromInput(inputEditText);
            }
        });

        //Enter button -> add goal
        final TextInputEditText inputEditText = dialogView.findViewById(R.id.newGoalInput);
        inputEditText.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                TextInputEditText inputEditText = ((View)v.getParent()).findViewById(R.id.newGoalInput);
                AddSkillFromInput(inputEditText);

                return true;
            }
        });

        //Show keyboard
        new Handler().postDelayed(new Runnable() {
            public void run() {

                inputEditText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN , 0, 0, 0));
                inputEditText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP , 0, 0, 0));
            }
        }, 500);
    }

    private void AddSkillFromInput(TextInputEditText inputEditText)
    {
        String newSkillName = inputEditText.getText().toString();

        if(!newSkillName.equals(""))
        {
            GenericSkill newGenericSkill = new GenericSkill(newSkillName, SkillCheckType.ALLCHECK, null);
            //genericSkills.add(newGenericSkill);
            expandableListAdapter.addGenericSkill(newGenericSkill);
        }

        popupDialog.dismiss();
    }
}
