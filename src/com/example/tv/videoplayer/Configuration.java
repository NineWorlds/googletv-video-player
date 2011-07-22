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

public class Configuration {

    // URL of dynamic data - This URL will give a JSON feed that will list the
    // categories and videos in each category.

    // The response from this URL is a JSON object that has:
    //
    // { "categories": [ {
    // "name": "<category-Name>",
    // "videos": [ {
    // "sources": [
    // "<url of video>" <-- This app only uses the first one.
    // ],
    // "thumb": "<url of thumbnail image>",
    // "title": "<title>",
    // "subtitle": "<subtitle>",
    // "description": "Paragraph that describes the video once it's played."
    // }, ...
    // ], "

    // {"categories":[
    // {"name":"Technology",
    // "videos":[
    // {"sources":["http://commondatastorage.googleapis.com/gtv_template_assets/CF1-AppsMarketplace-Part5.mp4"],
    // "thumb":"images/thumbs/thumb13.jpg",
    // "title":"Campfire Part 5",
    // "subtitle":"Dev Events",
    // "description":"CF1 AppsMarketplace Part5"},
    // {"sources":["http://commondatastorage.googleapis.com/gtv_template_assets/CF1-AppsMarketplace-Part6.mp4"],
    // "thumb":"images/thumbs/thumb14.jpg",
    // "title":"Campfire Part 6",
    // "subtitle":"Dev Events",
    // "description":"CF1 AppsMarketplace Part6"}, ...
    // ]}]}

    public final static String dataProvider = "http://www.videws.com/gtv/videosources.php";

}
