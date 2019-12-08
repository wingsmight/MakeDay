package com.wingsmight.makeday.Growth.Skills;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.wingsmight.makeday.AnimatedExpandableListView.AnimatedExpandableListView;
import com.wingsmight.makeday.R;
import com.wingsmight.makeday.SavingSystem.SaveLoad;
import com.wingsmight.makeday.TabName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


public class SkillsTabFragment extends Fragment implements ExpandableListView.OnGroupExpandListener
{
    TabName tabName = TabName.SKILLS;
    private ExpandableListView expandableListView;
    private SkillsTabAdapter expandableListAdapter;
    private int lastExpandedPosition = -1;
    ArrayList<GenericSkill> genericSkills;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_skills, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        //Add button
        FloatingActionButton addButton = view.findViewById(R.id.addSkillButton);
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

        expandableListView = view.findViewById(R.id.expandable_lst);
        expandableListAdapter = new SkillsTabAdapter(getContext(), genericSkills);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(this);

        //ActionBar
        setHasOptionsMenu(true);

        //Drag n drop
//        ItemTouchHelper helper =  new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0)
//        {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target)
//            {
//                int positionDragged = dragged.getAdapterPosition();
//                int positionTarget = target.getAdapterPosition();
//
//                Collections.swap(genericSkills, positionDragged, positionTarget);
//
//                expandableListAdapter.notifyDataSetChanged();;
//
//                return false;
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction)
//            {
//
//            }
//        });
//        //helper.attachToRecyclerView(expandableListView);

        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_GROUP)
                {
                    int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                    int childPosition = ExpandableListView.getPackedPositionChild(id);

                    // You now have everything that you would as if this was an OnChildClickListener()
                    // Add your logic here.

                    //include this:
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                    view.startDrag(null, shadowBuilder, genericSkills.get(position)/*.getItemAtPosition(position)*/, 0);
                    // Return true as we are handling the event.
                    return true;
                }

                return false;
            }
        });
        expandableListView.setOnDragListener(new AdapterView.OnDragListener()
        {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                int action = event.getAction();
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        //layoutParams = (RelativeLayout.LayoutParams)v.getLayoutParams();
                        Log.i("msg", "Action is DragEvent.ACTION_DRAG_STARTED");

                        // Do nothing
                        break;

                    case DragEvent.ACTION_DRAG_ENTERED:
                        Log.i("msg", "Action is DragEvent.ACTION_DRAG_ENTERED");
                        int x_cord = (int) event.getX();
                        int y_cord = (int) event.getY();
                        break;

                    case DragEvent.ACTION_DRAG_EXITED:
                        Log.i("msg", "Action is DragEvent.ACTION_DRAG_EXITED");
                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();

                        break;

                    case DragEvent.ACTION_DRAG_LOCATION:
                        Log.i("msg", "Action is DragEvent.ACTION_DRAG_LOCATION");
                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        Log.i("msg", Integer.toString(x_cord) + "," + Integer.toString(y_cord));
                        int nPointToPosition = expandableListView.pointToPosition(x_cord,y_cord);
                        if(expandableListView.getItemAtPosition(nPointToPosition)!= null)
                        {

                            // THE FUN PART IS HERE!
                            // ******this is the header list number******
                            int ngroupPosition = expandableListView.getPackedPositionGroup(expandableListView.getExpandableListPosition(nPointToPosition));

                            // ******this is the child position******
                            int nchildPosition = expandableListView.getPackedPositionChild(expandableListView.getExpandableListPosition(nPointToPosition));

                        }
                        break;

                    case DragEvent.ACTION_DRAG_ENDED:
                        Log.i("msg", "Action is DragEvent.ACTION_DRAG_ENDED");
                        // This is where I added some activities....
                        GenericSkill draggedGenericSkill = genericSkills.get(3);
                        GenericSkill draggedOnGenericSkill = genericSkills.get(0);
                        genericSkills.set(0, draggedGenericSkill);
                        genericSkills.set(3, draggedOnGenericSkill);

                        expandableListAdapter.notifyDataSetChanged();
                        break;

                    case DragEvent.ACTION_DROP:
                        Log.i("msg", "ACTION_DROP event");
                        // This is also a good place, place with it and see what you want to do
                        break;

                    default:
                        break;
                }
                //return value
                return true;
            }
        });//end DragListener
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.edit);
        if(item!=null)
        {
            item.setVisible(true);
            item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
            {
                @Override
                public boolean onMenuItemClick(MenuItem item)
                {
                    startEditMode();

                    return true;
                }
            });
        }
    }

    private void startEditMode()
    {

    }

    private void backgroundSave(){
        Thread backgroundThread = new Thread() {
            @Override
            public void run()
            {
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
        }, 1000);
    }

    private void AddSkillFromInput(TextInputEditText inputEditText)
    {
        String newSkillName = inputEditText.getText().toString();

        if(!newSkillName.equals(""))
        {
            GenericSkill newGenericSkill = new GenericSkill(expandableListAdapter.getGroupCount() + 1, newSkillName, SkillCheckType.NOONECHECK, null);
            //genericSkills.add(newGenericSkill);
            expandableListAdapter.addGenericSkill(newGenericSkill);
        }

        popupDialog.dismiss();
    }
}
