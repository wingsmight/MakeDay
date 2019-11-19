package com.wingsmight.makeday;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class MyDaysFragment extends Fragment
{
    RecyclerView recyclerView;
    ArrayList<RowDayModel> rowDayModels = new ArrayList<>();
    MyDaysAdapter myDaysAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_my_days, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        recyclerView = view.findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        myDaysAdapter = new MyDaysAdapter(view.getContext(), rowDayModels);
        recyclerView.setAdapter(myDaysAdapter);

        FillRecyclerView();
    }

    private void FillRecyclerView()
    {
        String[] doneDeals = {
                "дело 1",
                "дело 2"
        };
        String[] undoneDeals = {
                "дело 3"
        };
        RowDayModel rowDayModel = new RowDayModel(16, "ноября", 2019, doneDeals, undoneDeals);
        rowDayModels.add(rowDayModel);

        String[] doneDeals2 = {
                "дело 1"
        };
        String[] undoneDeals2 = {
                "дело 2",
                "дело 3"
        };
        RowDayModel rowDayModel2 = new RowDayModel(15, "ноября", 2019, doneDeals2, undoneDeals2);
        rowDayModels.add(rowDayModel2);

        rowDayModels.add(rowDayModel2);
        rowDayModels.add(rowDayModel2);
        rowDayModels.add(rowDayModel2);
        rowDayModels.add(rowDayModel2);
        rowDayModels.add(rowDayModel2);
        rowDayModels.add(rowDayModel2);
    }
}
