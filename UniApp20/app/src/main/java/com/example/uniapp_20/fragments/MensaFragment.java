package com.example.uniapp_20.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uniapp_20.R;
import com.example.uniapp_20.utils.FrontEngine;
import com.example.uniapp_20.utils.SessionManager;
import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MensaFragment extends Fragment implements Serializable{


    private ViewPager firstViewPager;
    private TabLayout tabLayout;


    public MensaFragment() {
        // Required empty public constructor
    }

    public static MensaFragment newInstance(String param1, String param2) {
        MensaFragment fragment = new MensaFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        SessionManager.getInstance(getContext()).setScreen("MensaFragment");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_mensa, container, false);
        //getActivity().getActionBar().setTitle("Mensa Menu");

        firstViewPager = view.findViewById(R.id.mensa_pager);

        tabLayout = view.findViewById(R.id.mensa_tab_layout);
        tabLayout.setupWithViewPager(firstViewPager);

        setupViewPager(firstViewPager);

        return view;
    }


    private void setupViewPager(ViewPager viewPager) {
        MensaFragment.ViewPagerAdapter adapter = new MensaFragment.ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new FirstMenuFragment());
        adapter.addFragment(new SecondMenuFragment());
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }

    }

    // the create options menu with a MenuInflater to have the menu from your fragment
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.action_favorite).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }


}
