package com.wingsmight.makeday.MyDays;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wingsmight.makeday.R;
import com.wingsmight.makeday.Tracker.RowDayModel;

import java.util.ArrayList;

public class MyDaysAdapter extends RecyclerView.Adapter<MyDaysAdapter.MyDaysViewHolder>
{
    private ArrayList<RowDayModel> rowDayModelArrayList;
    private Context context;

    public MyDaysAdapter(Context context, ArrayList<RowDayModel> rowDayModelArrayList)
    {
        this.rowDayModelArrayList = rowDayModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyDaysViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_day_feed, viewGroup, false);
        MyDaysViewHolder viewHolder = new MyDaysViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyDaysViewHolder viewHolder, int i)
    {
        final RowDayModel rowDayModel = rowDayModelArrayList.get(i);

        viewHolder.date.setText(rowDayModel.getDay() + " " + rowDayModel.getMonth());
        viewHolder.doList.setText(rowDayModel.getDoneString());
        viewHolder.undoList.setText(rowDayModel.getUndoneString());
    }

    @Override
    public int getItemCount() {
        return rowDayModelArrayList.size();
    }

    public class MyDaysViewHolder extends  RecyclerView.ViewHolder
    {
        TextView date, doList, undoList;

        public MyDaysViewHolder(@NonNull View itemView)
        {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            doList = itemView.findViewById(R.id.doList);
            undoList = itemView.findViewById(R.id.undoList);
        }
    }
}
