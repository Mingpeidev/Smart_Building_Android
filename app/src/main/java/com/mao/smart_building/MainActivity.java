package com.mao.smart_building;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mao.smart_building.Adapter.MyFragmentAdapter;
import com.mao.smart_building.Fragment.DoorFragment;
import com.mao.smart_building.Fragment.MainpageFragment;
import com.mao.smart_building.Fragment.SetFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mainBtn = null;
    private ImageView doorBtn = null;
    private ImageView setBtn = null;

    private TextView mainText = null;
    private TextView doorText = null;
    private TextView setText = null;

    private TextView title;

    private LinearLayout mainpage_layout;
    private LinearLayout door_layout;
    private LinearLayout set_layout;

    private LinearLayout view_layout;

    ViewPager mViewPager;

    MyFragmentAdapter myFragmentAdapter;
    FragmentManager mfragmentManager;

    String[] titleName = new String[]{"控制台", "门禁", "个人中心"};
    List<Fragment> mfragmentList = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        mfragmentManager = getSupportFragmentManager();
        setContentView(R.layout.activity_main);
        initFragmetList();
        myFragmentAdapter = new MyFragmentAdapter(mfragmentManager, mfragmentList);
        initView();
        initViewpager();
        // view_layout.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mainpage_layout:
                mViewPager.setCurrentItem(0);
                updateBottomLinearLayoutBackground(0);
                break;
            case R.id.door_layout:
                mViewPager.setCurrentItem(1);
                updateBottomLinearLayoutBackground(1);
                break;
            case R.id.set_layout:
                mViewPager.setCurrentItem(2);
                updateBottomLinearLayoutBackground(2);
                break;
            default:
                break;
        }
    }

    public void initFragmetList() {
        Fragment mainFragment = new MainpageFragment();
        Fragment doorFragment = new DoorFragment();
        Fragment setFragment = new SetFragment();

        mfragmentList.add(mainFragment);
        mfragmentList.add(doorFragment);
        mfragmentList.add(setFragment);

    }

    public void initView() {
        title = (TextView) findViewById(R.id.title_text);

        mainText = (TextView) findViewById(R.id.main_text);
        doorText = (TextView) findViewById(R.id.door_text);
        setText = (TextView) findViewById(R.id.set_text);

        mainBtn = (ImageView) findViewById(R.id.main_Btn);
        doorBtn = (ImageView) findViewById(R.id.door_Btn);
        setBtn = (ImageView) findViewById(R.id.set_Btn);

        mainpage_layout = (LinearLayout) findViewById(R.id.mainpage_layout);
        mainpage_layout.setOnClickListener(this);
        door_layout = (LinearLayout) findViewById(R.id.door_layout);
        door_layout.setOnClickListener(this);
        set_layout = (LinearLayout) findViewById(R.id.set_layout);
        set_layout.setOnClickListener(this);

        view_layout = (LinearLayout) findViewById(R.id.view_layout);

        mViewPager = (ViewPager) findViewById(R.id.pager);
    }

    public void initViewpager() {
        mViewPager.addOnPageChangeListener(new ViewPagetOnPagerChangedLisenter());
        mViewPager.setAdapter(myFragmentAdapter);
        mViewPager.setCurrentItem(0);
        title.setText(titleName[0]);
        updateBottomLinearLayoutBackground(0);


    }

    private void updateBottomLinearLayoutBackground(int x) {
        switch (x) {
            case 0:
                mainBtn.setBackgroundResource(R.drawable.main);
                doorBtn.setBackgroundResource(R.drawable.door1);
                setBtn.setBackgroundResource(R.drawable.user1);

                mainText.setTextColor(getResources().getColor(R.color.set));
                doorText.setTextColor(getResources().getColor(R.color.unset));
                setText.setTextColor(getResources().getColor(R.color.unset));
                break;
            case 1:
                mainBtn.setBackgroundResource(R.drawable.main1);
                doorBtn.setBackgroundResource(R.drawable.door);
                setBtn.setBackgroundResource(R.drawable.user1);

                mainText.setTextColor(getResources().getColor(R.color.unset));
                doorText.setTextColor(getResources().getColor(R.color.set));
                setText.setTextColor(getResources().getColor(R.color.unset));
                break;
            case 2:
                mainBtn.setBackgroundResource(R.drawable.main1);
                doorBtn.setBackgroundResource(R.drawable.door1);
                setBtn.setBackgroundResource(R.drawable.user);

                mainText.setTextColor(getResources().getColor(R.color.unset));
                doorText.setTextColor(getResources().getColor(R.color.unset));
                setText.setTextColor(getResources().getColor(R.color.set));
                break;
            default:
                break;
        }
    }

    class ViewPagetOnPagerChangedLisenter implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            title.setText(titleName[position]);
            updateBottomLinearLayoutBackground(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
