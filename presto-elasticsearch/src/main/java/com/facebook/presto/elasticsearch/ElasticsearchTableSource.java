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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by sprinklr on 03/07/15.
 */
public class ElasticsearchTableSource
{
    private String hostaddress;
    private int port;
    private String clusterName;
    private String index;
    private String type;

    @JsonCreator
    public ElasticsearchTableSource(
            @JsonProperty("hostaddress") String hostaddress,
            @JsonProperty("port") int port,
            @JsonProperty("clusterName") String clusterName,
            @JsonProperty("index") String index,
            @JsonProperty("type") String type)
    {
        this.hostaddress = checkNotNull(hostaddress, "hostaddress is null");
        this.port = checkNotNull(port, "port is null");
        this.clusterName = checkNotNull(clusterName, "clusterName is null");
        this.index = checkNotNull(index, "index is null");
        this.type = checkNotNull(type, "type is null");
    }

    @JsonProperty
    public String getHostaddress()
    {
        return hostaddress;
    }

    @JsonProperty
    public int getPort()
    {
        return port;
    }

    @JsonProperty
    public String getClusterName()
    {
        return clusterName;
    }

    @JsonProperty
    public String getIndex()
    {
        return index;
    }

    @JsonProperty
    public String getType()
    {
        return type;
    }
}
