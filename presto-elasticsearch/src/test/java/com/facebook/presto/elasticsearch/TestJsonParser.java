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
package com.facebook.presto.elasticsearch;

// http://stackoverflow.com/questions/26183948/output-list-of-all-paths-to-leaf-nodes-in-a-json-document-in-java

import com.facebook.presto.elasticsearch.util.JsonParser;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class TestJsonParser
{
    private TestJsonParser()
    {
    }

    public static void main(String[] args)
            throws JSONException
    {
        String data = "{\"store\":{\"book\":[{\"category\":\"reference\",\"author\":\"NigelRees\",\"title\":\"SayingsoftheCentury\",\"price\":8.95},{\"category\":\"fiction\",\"author\":\"HermanMelville\",\"title\":\"MobyDick\",\"isbn\":\"0-553-21311-3\",\"price\":8.99},],\"bicycle\":{\"color\":\"red\",\"price\":19.95}},\"expensive\":10}";
        JSONObject json = new JSONObject(data);
        System.out.println(json.get("store"));
        //System.out.println(json.toString(2));
        List<String> leaves = (new JsonParser()).getListJson(json);

        for (String s : leaves) {
            System.out.println(s);
            System.out.println(".....");
        }
    }
}
