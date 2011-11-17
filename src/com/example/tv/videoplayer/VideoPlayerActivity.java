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
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

// The VideoPlayerActivity which sets up a video view, attaches a MediaControler to it.
// Communication is via private intent.  This could use the MediaPlayer API's but that would
// add a lot of complexity.

public class VideoPlayerActivity extends Activity
        implements AudioManager.OnAudioFocusChangeListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener {
    public static final String TAG = "VPActivity";

    public VideoView mVideoView = null;
    private LayoutParams mDefaultVideoViewSize;

    // Handle AudioFocus issues
    @Override
    public void onAudioFocusChange(int focusChange) {
        switch(focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                Log.i(TAG, "AF Gain");
                if (mVideoView != null)
                    mVideoView.resume();
                break;

            case AudioManager.AUDIOFOCUS_LOSS:
                Log.i(TAG, "AF Loss");
                if (mVideoView != null)
                    mVideoView.stopPlayback();
                mVideoView = null;
                this.finish(); // Let's move on.
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                Log.i(TAG, "AF Transient");
                if (mVideoView != null)
                    mVideoView.pause();
                break;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.i(TAG, "done.");
        mVideoView = null;
        this.finish();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.e(TAG, "IO Error e=" + what + " x=" + extra);
        return false; // Will call onCompletion
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Request Audio Focus
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = am.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            Log.e(TAG, "Can't get AudioFocus " + result);
            this.finish(); // Just give up.
        }

        setContentView(R.layout.details);
        final Intent i = getIntent();
        final Bundle b = i.getBundleExtra("com.example.tv.videoplayer");

        ((TextView) findViewById(R.id.videoTitle)).setText(b.getString("title"));
        ((TextView) findViewById(R.id.videoSubTitle)).setText(b.getString("subtitle"));
        ((TextView) findViewById(R.id.videoDescription)).setText(b.getString("description"));

        mVideoView = (VideoView) findViewById(R.id.videoView1);
        mVideoView.setVideoPath(b.getString("source"));
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnErrorListener(this);

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
