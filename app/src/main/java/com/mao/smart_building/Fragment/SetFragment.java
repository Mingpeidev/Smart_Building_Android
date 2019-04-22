package com.mao.smart_building.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mao.smart_building.Activity.SmartsetActivity;
import com.mao.smart_building.Login.LoginActivity;
import com.mao.smart_building.R;

/**
 * Created by Mingpeidev on 2019/4/2.
 */

public class SetFragment extends Fragment {
    private Button exitBtn = null;
    private Button smartset_Btn=null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.set_layout, null);
        exitBtn = view.findViewById(R.id.exitBtn);
        smartset_Btn=view.findViewById(R.id.samrtset_Btn);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        smartset_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SmartsetActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
}
