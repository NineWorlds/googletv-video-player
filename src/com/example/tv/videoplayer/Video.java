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

import android.os.Bundle;

import java.net.URI;
import java.util.ArrayList;

// The purpose of this class is to hold basic information about a video, it provides a constructor
// for convenience to create an element, and a getAsBundle method that makes it easy to pass these
// to another activity for play back.

public class Video {
    public URI mThumb;
    public ArrayList<URI> mSource;
    public String mTitle;
    public String mSubTitle;
    public String mDescription;

    public Video(final URI thumb, final URI source, final String title, final String subtitle,
            final String description) {
        this.mThumb = thumb;
        this.mSource = new ArrayList<URI>(1);
        this.mSource.add(source);
        this.mTitle = title;
        this.mSubTitle = subtitle;
        this.mDescription = description;
    }

    public Bundle getAsBundle() {
        Bundle b = new Bundle();
        b.putString("title", mTitle);
        b.putString("subtitle", mSubTitle);
        b.putString("description", mDescription);
        b.putString("thumb", mThumb.toString());
        b.putString("source", mSource.get(0).toString());
        return b;
    }
}
