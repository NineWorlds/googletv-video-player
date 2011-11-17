// Copyright 2011 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.example.tv.videoplayer;

import com.example.google.tv.leftnavbar.LeftNavBar;
import com.example.google.tv.leftnavbar.LeftNavBarService;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import java.util.Random;

// Our main activity, it shows how to setup the actionBar, and change the gridView.  It also has
// the code to activate the VideoPlayerActivity.

// This is sample code.  It could work as an app, but in a real app, you'll probably wish to have
// a bit more caching of data, and error handling.  The goal was to provide a simple elegant
// example to help inspire you to create great Google TV applications.

// If you decide to make your own App from this, you should change the package name.  And the name
// of the private intent.  VERY IMPORTANT

public class Style1Activity extends DataActivity {
    public static final String TAG = "VideoPlayer";
    public String[] mCategories = null;
    public LeftNavBar mBar;
    public static final int[] mIcons = {
            R.drawable.icon01,
            R.drawable.icon02, R.drawable.icon03, R.drawable.icon04, R.drawable.icon05
    };
    public final Random mRand = new Random(); // Hopefully in your app,
                                              // you won't be using
                                              // random icons.

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBar = (LeftNavBarService.instance()).getLeftNavBar((Activity) this);
        mBar.setDisplayUseLogoEnabled(true);

        // Note - I'm showing this so that my UI is pushed down a bit and it
        // does a partial reveal
        // on screen. Thus giving the user an indicator that scrolling is
        // possible.

        mBar.setTitle(R.string.app_name);
        mBar.showOptionsMenu(false);
        // mBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
        setContentView(R.layout.style1);
    }

    public void onDataComplete() {

        mCategories = getCategories();

        boolean isfirst = true;
        for (String name : mCategories) {
            mBar.addTab(mBar.newTab().setText(name).setIcon(mIcons[mRand.nextInt(mIcons.length)])
                    .setTabListener(new ActionBar.TabListener() {

                        @Override
                        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
                        }

                        @Override
                        public void onTabSelected(Tab tab, FragmentTransaction ft) {
                            selectCategory(tab.getText().toString());
                        }

                        @Override
                        public void onTabReselected(Tab tab, FragmentTransaction ft) {

                        }
                    }), isfirst);
            isfirst = false;
        }
        mBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    }

    public void selectCategory(final String category) {

        final GridView gridview = (GridView) findViewById(R.id.gridView1);
        gridview.setAdapter(new GridAdapter(this, category));

        gridview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View v, final int position,
                    final long id) {
                Video vid = mShows.get(category).get(position);

                final Bundle b = vid.getAsBundle();
                final Intent i = new Intent("com.example.tv.videoplayer");
                i.putExtra("com.example.tv.videoplayer", b);
                startActivity(i);
            }
        });

    }

}
