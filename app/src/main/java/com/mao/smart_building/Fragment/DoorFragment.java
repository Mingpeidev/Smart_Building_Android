package com.mao.smart_building.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mao.smart_building.Adapter.RecyclerViewAdapter;
import com.mao.smart_building.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mingpeidev on 2019/4/2.
 */

public class DoorFragment extends Fragment {
    private RecyclerView recyclerView;
    private Button record;

    private List<String> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.door_layout, null);

        recyclerView = view.findViewById(R.id.recycleview_all);
        record = view.findViewById(R.id.record);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        String[] a = {"16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32"};
        if (list.isEmpty()) {
            for (int i = 0; i < a.length; i++) {
                list.add(a[i]);
            }
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());//瀑布流
        recyclerView.setLayoutManager(linearLayoutManager);

        final RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(list);
        recyclerView.setAdapter(recyclerViewAdapter);//绑定适配器

        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));//添加分割线

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String[] b = {"6", "7", "1", "1", "0", "1", "2", "3", "4", "5", "26", "27", "28", "29", "30", "31", "32"};

                for (int i = 0; i < b.length; i++) {
                    list.add(b[i]);
                }
                recyclerViewAdapter.refreshData(list);
            }
        });
    }
}
