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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

// The VideoPlayerActivity which sets up a video view, attaches a MediaControler to it.
// Communication is via private intent.

public class VideoPlayerActivity extends Activity {
    public VideoView mVideoView;
    private LayoutParams mDefaultVideoViewSize;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.details);
        final Intent i = getIntent();
        final Bundle b = i.getBundleExtra("com.example.tv.videoplayer");

        ((TextView) findViewById(R.id.videoTitle)).setText(b.getString("title"));
        ((TextView) findViewById(R.id.videoSubTitle)).setText(b.getString("subtitle"));
        ((TextView) findViewById(R.id.videoDescription)).setText(b.getString("description"));

        mVideoView = (VideoView) findViewById(R.id.videoView1);
        mVideoView.setVideoPath(b.getString("source"));
        MediaController mc = new MediaController(this, true);
        mc.setMediaPlayer(mVideoView);
        mc.setAnchorView(mVideoView);
        mVideoView.setMediaController(mc);
        mVideoView.requestFocus();
        mVideoView.start();
        mDefaultVideoViewSize = mVideoView.getLayoutParams();

        // Make it so that we can click the video view and make it zoom
        // Note - this code isn't currently running, but if you were to get a
        // click it does the right thing.
        mVideoView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                LayoutParams lp = v.getLayoutParams();
                if (lp.width != LayoutParams.MATCH_PARENT) {
                    lp.width = LayoutParams.MATCH_PARENT;
                    lp.height = LayoutParams.MATCH_PARENT;
                } else {
                    lp = mDefaultVideoViewSize;
                }
                v.setLayoutParams(lp);
            }
        });
    }

}
