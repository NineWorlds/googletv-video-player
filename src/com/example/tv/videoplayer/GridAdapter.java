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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

// This class just manages our cells.

public class GridAdapter extends BaseAdapter {
    private final LayoutInflater mInflator;
    private ArrayList<Video> mVideos;
    private final ImageDownloader mDownload = new ImageDownloader();

    // references to our images

    public GridAdapter(final DataActivity c, final String category) {
        mInflator = c.getLayoutInflater();
        mVideos = c.mShows.get(category);
    }

    @Override
    public int getCount() {
        return mVideos.size();
    }

    @Override
    public Object getItem(final int position) {
        return null;
    }

    @Override
    public long getItemId(final int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        LinearLayout lg = (LinearLayout) convertView;
        ImageView imageView;
        if (lg == null) {
            lg = (LinearLayout) mInflator.inflate(R.layout.cell, null);
        }

        imageView = (ImageView) lg.getChildAt(0);
        mDownload.download(mVideos.get(position).mThumb.toString(), imageView);
        ((TextView) lg.getChildAt(1)).setText(mVideos.get(position).mTitle);
        ((TextView) lg.getChildAt(2)).setText(mVideos.get(position).mSubTitle);

        return lg;
    }
}
