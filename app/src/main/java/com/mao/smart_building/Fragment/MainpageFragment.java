package com.mao.smart_building.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mao.smart_building.R;

/**
 * Created by Mingpeidev on 2019/4/1.
 */

public class MainpageFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mainpage_layout, null);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
