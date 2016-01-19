/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.facebook.presto.elasticsearch.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lucianmatei on 19/01/16.
 */
public class JsonParser
{
    private ArrayList<String> leaves = new ArrayList<String>();

    public List<String> getListJson(JSONObject json)
            throws JSONException
    {
        listJSONObject("", json);
        return leaves;
    }

    private void listObject(String parent, Object data)
            throws JSONException
    {
        if (data instanceof JSONObject) {
            listJSONObject(parent, (JSONObject) data);
        }
        else if (data instanceof JSONArray) {
            listJSONArray(parent, (JSONArray) data);
        }
        else {
            listPrimitive(parent, data);
        }
    }

    private void listJSONObject(String parent, JSONObject json)
            throws JSONException
    {
        Iterator it = json.keys();
        while (it.hasNext()) {
            String key = (String) it.next();
            Object child = json.get(key);
            String childKey = parent.isEmpty() ? key : parent + "." + key;
            listObject(childKey, child);
        }
    }

    private void listJSONArray(String parent, JSONArray json)
            throws JSONException
    {
        for (int i = 0; i < json.length(); i++) {
            Object data = json.get(i);
            listObject(parent, data);
        }
    }

    private void listPrimitive(String parent, Object obj)
    {
        leaves.add(parent + ":" + obj.toString());
    }
}
