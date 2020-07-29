package com.example.mobiltestcase;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.mobiltestcase.fragments.FragmentChat;
import com.example.mobiltestcase.fragments.FragmentGet;
import com.example.mobiltestcase.fragments.FragmentNote;
import com.example.mobiltestcase.fragments.FragmentProfil;
import com.example.mobiltestcase.fragments.FragmentSend;
import com.example.mobiltestcase.fragments.TabAdapter;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_main);
        ViewPager viewPager = findViewById(R.id.pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentChat(), "");
        adapter.addFragment(new FragmentSend(), "");
        adapter.addFragment(new FragmentProfil(), "");
        adapter.addFragment(new FragmentGet(), "");
        adapter.addFragment(new FragmentNote(), "");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        int[] tabIcons = {
                R.drawable.event_chat,
                R.drawable.event_send,
                R.drawable.event_user,
                R.drawable.event_get,
                R.drawable.event_note
        };
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);


    }

}