package com.example.mmr.patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mmr.R;
import com.example.mmr.medic.MedicHome;
import com.example.mmr.medic.MedicInfoMap;
import com.example.mmr.shared.SharedModel;
import com.fxn.BubbleTabBar;
import com.fxn.OnBubbleClickListener;

import java.util.Calendar;
import java.util.List;

public class Home extends AppCompatActivity {

    BubbleTabBar bubbleTabBar;
    FragmentPagerAdapter adapterViewPager;
    PatientSessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //make it fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        sessionManager=new PatientSessionManager(this);
        //loadFragment(new HomeFragment());
        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager(),sessionManager);
        vpPager.setAdapter(adapterViewPager);
        bubbleTabBar = (BubbleTabBar) findViewById(R.id.bubbleTabBar);
        bubbleTabBar.getChildAt(0).setActivated(true);

        bubbleTabBar.addBubbleListener(new OnBubbleClickListener() {
            @Override
            public void onBubbleClick(int i) {
                //Fragment fragment;
                switch (i) {
                    case R.id.record: /*fragment=RecordFragment.newInstance(1,"page2")*/
                        vpPager.setCurrentItem(1);
                        break;
                    case R.id.calendar: /*fragment=CalendarFragment.newInstance(2,"page2")*/
                        vpPager.setCurrentItem(2);
                        break;
                    case R.id.profile: /*fragment=ProfileFragment.newInstance(3,"page2")*/
                        vpPager.setCurrentItem(3);
                        break;
                    default: /*fragment=HomeFragment.newInstance(0,"page2")*/
                        vpPager.setCurrentItem(0);
                }
                //loadFragment(fragment);
            }
        });

    }

    /*
    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
     */
    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 4;
        private PatientSessionManager sessionManager;

        public MyPagerAdapter(FragmentManager fragmentManager,PatientSessionManager sessionManager) {
            super(fragmentManager);
            this.sessionManager=sessionManager;
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return HomeFragment.newInstance(sessionManager.getCinPatient(),
                            sessionManager.getNomPatient(),
                            sessionManager.getPrenomPatient(),
                            sessionManager.getImgPatient());
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return RecordFragment.newInstance(1, "Page # 2");
                case 2: // Fragment # 1 - This will show SecondFragment
                    return CalendarFragment.newInstance(2, sessionManager.getCinPatient());
                default:
                    return ProfileFragment.newInstance(sessionManager.getCinPatient(),
                            sessionManager.getNomPatient(),
                            sessionManager.getPrenomPatient(),
                            sessionManager.getImgPatient());
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }

    @Override
    public void onBackPressed() {
        Dialog dialog = new Dialog(Home.this);
        dialog.setContentView(R.layout.dialog_logout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button logout = dialog.findViewById(R.id.logout);
        Button stay = dialog.findViewById(R.id.stay_connected);
        dialog.show();
        stay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Home.this.finishAffinity();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sessionManager.logout();
                Home.this.finishAffinity();
            }
        });
    }
}