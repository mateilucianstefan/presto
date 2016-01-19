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

import com.facebook.presto.spi.ColumnHandle;
import com.facebook.presto.spi.ConnectorPartition;
import com.facebook.presto.spi.ConnectorPartitionResult;
import com.facebook.presto.spi.ConnectorSplit;
import com.facebook.presto.spi.ConnectorSplitManager;
import com.facebook.presto.spi.ConnectorSplitSource;
import com.facebook.presto.spi.ConnectorTableHandle;
import com.facebook.presto.spi.FixedSplitSource;
import com.facebook.presto.spi.TupleDomain;
import com.google.common.collect.ImmutableList;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.facebook.presto.elasticsearch.Types.checkType;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class ElasticsearchSplitManager
        implements ConnectorSplitManager
{
    private final String connectorId;
    private final ElasticsearchClient elasticsearchClient;

    @Inject
    public ElasticsearchSplitManager(ElasticsearchConnectorId connectorId, ElasticsearchClient elasticsearchClient)
    {
        this.connectorId = checkNotNull(connectorId, "connectorId is null").toString();
        this.elasticsearchClient = checkNotNull(elasticsearchClient, "client is null");
    }

    @Override
    public ConnectorPartitionResult getPartitions(ConnectorTableHandle tableHandle, TupleDomain<ColumnHandle> tupleDomain)
    {
        ElasticsearchTableHandle elasticsearchTableHandle = checkType(tableHandle, ElasticsearchTableHandle.class, "tableHandle");

        // elasticsearch connector has only one partition
        List<ConnectorPartition> partitions = ImmutableList.<ConnectorPartition>of(new ElasticsearchPartition(elasticsearchTableHandle.getSchemaName(), elasticsearchTableHandle.getTableName()));
        // elasticsearch connector does not do any additional processing/filtering with the TupleDomain, so just return the whole TupleDomain
        return new ConnectorPartitionResult(partitions, tupleDomain);
    }

    @Override
    public ConnectorSplitSource getPartitionSplits(ConnectorTableHandle tableHandle, List<ConnectorPartition> partitions)
    {
        checkNotNull(partitions, "partitions is null");
        checkArgument(partitions.size() == 1, "Expected one partition but got %s", partitions.size());
        ConnectorPartition partition = partitions.get(0);

        ElasticsearchPartition elasticsearchPartition = checkType(partition, ElasticsearchPartition.class, "partition");

        ElasticsearchTableHandle elasticsearchTableHandle = (ElasticsearchTableHandle) tableHandle;
        ElasticsearchTable table = elasticsearchClient.getTable(elasticsearchTableHandle.getSchemaName(), elasticsearchTableHandle.getTableName());
        // this can happen if table is removed during a query
        checkState(table != null, "Table %s.%s no longer exists", elasticsearchTableHandle.getSchemaName(), elasticsearchTableHandle.getTableName());

        List<ConnectorSplit> splits = new ArrayList<>();
        for (ElasticsearchTableSource uri : table.getSources()) {
            int clmsCount = table.getColumns().size();
            splits.add(new ElasticsearchSplit(connectorId, elasticsearchPartition.getSchemaName(), elasticsearchPartition.getTableName(), uri));
        }
        Collections.shuffle(splits);

        return new FixedSplitSource(connectorId, splits);
    }
}
