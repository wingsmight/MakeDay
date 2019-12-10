package com.wingsmight.makeday.DragNDropExpandableListView;

import android.app.ActionBar;
import android.app.ExpandableListActivity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.wingsmight.makeday.Growth.Skills.GenericSkill;
import com.wingsmight.makeday.Growth.Skills.SkillCheckType;
import com.wingsmight.makeday.Growth.Skills.SkillsTabAdapter;
import com.wingsmight.makeday.Growth.Skills.SkillsTabFragment;
import com.wingsmight.makeday.MainActivity;
import com.wingsmight.makeday.R;
import com.wingsmight.makeday.SavingSystem.SaveLoad;
import com.wingsmight.makeday.TabName;

import java.util.ArrayList;

public class DragNDropListActivity extends ExpandableListActivity
{
	ArrayList<GenericSkill> genericSkills;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dragndroplistview);
        Intent intent = getIntent();

        genericSkills = (ArrayList<GenericSkill>)intent.getSerializableExtra(SkillsTabFragment.EXTRA_SKILLS);

        for (GenericSkill genericSkill : genericSkills)
        {
            genericSkill.setIsExpanded(false);
        }

        setListAdapter(new SkillsTabAdapter(this, genericSkills));
        ExpandableListView listView = getExpandableListView();
        listView.setIndicatorBounds(-1000, 0);

        if (listView instanceof DragNDropListView)
        {
        	((DragNDropListView) listView).setDropListener(mDropListener);
        	((DragNDropListView) listView).setRemoveListener(mRemoveListener);
        	((DragNDropListView) listView).setDragListener(mDragListener);
        	((DragNDropListView) listView).setOnGroupExpandListener(mOnExpandListener);
        	((DragNDropListView) listView).setOnGroupCollapseListener(mOnCollapseListener);
        }

//        ActionBar toolbar = getActionBar();
//        toolbar.show();
//        toolbar.setTitle(getResources().getString(R.string.Skills));
//
//        toolbar.setDisplayHomeAsUpEnabled(true);
//        toolbar.setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        Intent resultIntent = new Intent();
        resultIntent.putExtra(SkillsTabFragment.EXTRA_SKILLS, (ArrayList<GenericSkill>)genericSkills);

        setResult(RESULT_OK, resultIntent);
        finish();
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
                getExpandableListView().collapseGroup(lastExpandedPosition);
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
        	ExpandableListAdapter adapter = getExpandableListAdapter();
        	if (adapter instanceof SkillsTabAdapter) {
        		((SkillsTabAdapter)adapter).onDrop(from, to);
                getExpandableListView().invalidateViews();
        	}
        }

        public void onDropChild(int from, int to) {
            ExpandableListAdapter adapter = getExpandableListAdapter();
            if (adapter instanceof SkillsTabAdapter) {
                ((SkillsTabAdapter)adapter).onDropChild(from, to);
                getExpandableListView().invalidateViews();
            }
        }

            @Override
            public void onSwap(int from, int to, ListView listView, int prevPosition)
            {
                ExpandableListAdapter adapter = getExpandableListAdapter();
                if (adapter instanceof SkillsTabAdapter) {
                    ((SkillsTabAdapter)adapter).onSwap(from, to, listView, prevPosition);
                    getExpandableListView().invalidateViews();
                }
            }

            @Override
            public void onSwapChild(int from, int to, ListView listView, int prevPosition)
            {
                ExpandableListAdapter adapter = getExpandableListAdapter();
                if (adapter instanceof SkillsTabAdapter) {
                    ((SkillsTabAdapter)adapter).onSwapChild(from, to, listView, prevPosition);
                    getExpandableListView().invalidateViews();
                }
            }

            @Override
            public void onStartDrag(int from, ListView listView)
            {
                ExpandableListAdapter adapter = getExpandableListAdapter();
                if (adapter instanceof SkillsTabAdapter) {
                    ((SkillsTabAdapter)adapter).onStartDrag(from, listView);
                    getExpandableListView().invalidateViews();
                }
            }
        };
    
    private RemoveListener mRemoveListener =
        new RemoveListener() {
        public void onRemove(int which) {
			ExpandableListAdapter adapter = getExpandableListAdapter();
        	if (adapter instanceof SkillsTabAdapter) {
        		((SkillsTabAdapter)adapter).onRemove(which);
                getExpandableListView().invalidateViews();
        	}
        }
    };

    public void remove(final int index)
    {
        Animation anim = AnimationUtils.loadAnimation(
                DragNDropListActivity.this, android.R.anim.slide_out_right
        );
        anim.setDuration(350);
        getExpandableListView().getChildAt(index).startAnimation(anim);
        getExpandableListView().collapseGroup(index);

        new Handler().postDelayed(new Runnable() {

            public void run() {

//                FavouritesManager.getInstance().remove(
//                        FavouritesManager.getInstance().getTripManagerAtIndex(index)
//                );
                //populateList();
                ((SkillsTabAdapter)getExpandableListAdapter()).update();

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
        Animation anim = AnimationUtils.loadAnimation(DragNDropListActivity.this, android.R.anim.slide_out_right);
        anim.setDuration(350);
        getExpandableListAdapter().getChildView(groupPosition, childPosition, childPosition == getExpandableListAdapter().getChildrenCount(groupPosition) - 1,
                null, (ViewGroup) this.findViewById(android.R.id.list)).startAnimation(anim);

        new Handler().postDelayed(new Runnable() {

            public void run()
            {
                ((SkillsTabAdapter)getExpandableListAdapter()).update();

            }

        }, anim.getDuration());
    }
}