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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

// This code will load a JSON object from a URL then parse it, and notify the child activity
// when it's been done.  This is here, since it's easier to connect with the true context this
// way.  The AsyncTask is needed since you can't do networking on the main thread.

abstract class DataActivity extends Activity {
    abstract void onDataComplete(); // Called when we've got data

    public TreeMap<String, ArrayList<Video>> mShows = new TreeMap<String, ArrayList<Video>>();
    private JSONDownloader mJSONDownloader;

    private static final String LOG_TAG = "DataActivity";

    private class JSONDownloader extends AsyncTask<String, Void, String> {
        private String mUrl;

        @Override
        protected String doInBackground(final String... params) {

            // Your server may require you to have a specific userAgent, such as
            // emulating a browser

            final AndroidHttpClient client = AndroidHttpClient.newInstance("Android Google-TV");
            mUrl = params[0];
            final HttpGet getRequest = new HttpGet(mUrl);
            if (params.length > 1) {
                final String cookie = params[1];
                if (cookie != null) {
                    getRequest.setHeader("cookie", cookie);
                }
            }
            Log.d(LOG_TAG, "start of doInBackground");
            try {
                final HttpResponse response = client.execute(getRequest);
                final int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != HttpStatus.SC_OK) {
                    Log.w(LOG_TAG, "Error " + statusCode + " while retrieving json from "
                            + mUrl);
                    return null;
                }

                final HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream inputStream = null;
                    try {
                        inputStream = entity.getContent();
                        final BufferedReader r = new BufferedReader(new InputStreamReader(
                                inputStream));
                        final StringBuilder total = new StringBuilder();
                        String line;
                        while ((line = r.readLine()) != null) {
                            total.append(line);
                        }
                        inputStream.close();
                        client.close();
                        Log.d(LOG_TAG, "Finished - all is well");
                        return total.toString();

                    } finally {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        entity.consumeContent();
                    }
                }
            } catch (final IOException e) {
                getRequest.abort();
                Log.w(LOG_TAG, "I/O error while retrieving json from " + mUrl, e);
            } catch (final IllegalStateException e) {
                getRequest.abort();
                Log.w(LOG_TAG, "Incorrect URL: " + mUrl);
            } catch (final Exception e) {
                getRequest.abort();
                Log.w(LOG_TAG, "Error while retrieving json from " + mUrl, e);
            } finally {
                if (client != null)
                    client.close();
            }
            Log.d(LOG_TAG, "finished - some error.");
            return null;
        }

        @Override
        protected void onPostExecute(final String json) {
            Log.i(LOG_TAG, "decoding...");
            if (json == null)
                return;
            try {
                final URI base = new URI(mUrl);
                final JSONObject jso = new JSONObject(json);
                final JSONArray ja = jso.getJSONArray("categories");
                for (int i = 0; i < ja.length(); i++) {
                    final JSONObject list = ja.getJSONObject(i);
                    final ArrayList<Video> vidList = new ArrayList<Video>(10);
                    mShows.put(list.getString("name"), vidList);
                    final JSONArray videos = list.getJSONArray("videos");
                    for (int j = 0; j < videos.length(); j++) {
                        final JSONObject vids = videos.getJSONObject(j);
                        final JSONArray sources = vids.getJSONArray("sources");
                        try {
                            final Video v = new Video(base.resolve(vids.getString("thumb")),
                                    base.resolve(sources.getString(0)), vids.getString("title"),
                                    vids.getString("subtitle"), vids.getString("description"));
                            vidList.add(v);

                            // The rare case that I've got more than one
                            for (int k = 1; k < sources.length(); k++) {
                                v.mSource.add(base.resolve(sources.getString(k)));
                            }
                        } catch (IllegalArgumentException expected) {
                            Log.e(LOG_TAG, "Bad Json.");
                        }
                    }
                }
            } catch (final JSONException je) {
                Log.e(LOG_TAG, "An error in the JSON." + je.getMessage());
            } catch (final URISyntaxException se) {
                Log.e(LOG_TAG, "URI syntax error" + se.getMessage());
            }
            onDataComplete();
        }
    }

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        mJSONDownloader = new JSONDownloader();
        mJSONDownloader.execute(Configuration.dataProvider, null);
    }

    public String[] getCategories() {
        Set<String> ss = mShows.keySet();
        Object[] o = ss.toArray();
        int length = o.length;
        String[] t = new String[length];
        for (int i = 0; i < length; i++)
            t[i] = (String) o[i];
        return t;
    }

}
