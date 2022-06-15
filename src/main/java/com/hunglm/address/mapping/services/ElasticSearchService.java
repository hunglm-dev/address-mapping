package com.hunglm.address.mapping.services;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.CountRequest;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.IndexOperation;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import com.hunglm.address.mapping.configs.AddressDictionary;
import com.hunglm.address.mapping.constants.IndexName;
import com.hunglm.address.mapping.entities.ESDocument;
import com.hunglm.address.mapping.entities.Normalizeable;
import com.hunglm.address.mapping.entities.his.HisCity;
import com.hunglm.address.mapping.entities.his.HisDistrict;
import com.hunglm.address.mapping.entities.his.HisWard;
import com.hunglm.address.mapping.entities.ym.District;
import com.hunglm.address.mapping.entities.ym.Ward;
import com.hunglm.address.mapping.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ElasticSearchService {
    private final ElasticsearchClient client;
    private final AddressDictionary dictionary;

    public void deleteIndex(String indexName) throws IOException {
        if (client.indices().exists(r -> r.index(indexName)).value()) {
            DeleteIndexResponse deleteIndexResponse = client.indices().delete(
                    new DeleteIndexRequest.Builder()
                            .index(indexName)
                            .build()
            );
            log.info("DeleteResponse: {}", deleteIndexResponse.acknowledged());
        }
    }

    public <T extends ESDocument> void insert(String indexName, T val) throws IOException {
        client.index(new IndexRequest.Builder<T>()
                .index(indexName)
                .id(val.getId())
                .document(val)
                .build());
    }

    public <T extends ESDocument> void insertBulk(String index, List<T> values) throws IOException {
        List<BulkOperation> operations = new ArrayList<>();
        values.forEach(val -> operations.add(
                new BulkOperation(IndexOperation.of(doc -> doc.document(val).index(index).id(val.getId()))))
        );
        client.bulk(new BulkRequest.Builder().operations(operations).build());
    }

    public long countDocs(String indexName) throws IOException {
        return client.count(new CountRequest.Builder()
                .index(indexName)
                .build()).count();
    }

    public <T> HitsMetadata<T> search(String indexName, String searchVal, Class<T> clzz) throws IOException {
        List<Query> shouldList = new ArrayList<>();
        shouldList.add(new Query.Builder().match(m -> m.field("name").query(q -> q.stringValue(searchVal))).build());
        shouldList.add(new Query.Builder().match(m -> m.field("normalizedName")
                .query(q -> q.stringValue(StringUtils.nomalizeUnicodeAndSpecialChars(searchVal)))).build());
        return client.search(s -> s
                .index(indexName)
                .query(q -> q
                        .bool(b -> b
                                .should(shouldList))), clzz).hits();
    }

    public HitsMetadata<HisCity> searchCity(String searchVal) throws IOException {
        return search(IndexName.HIS_CITIES, searchVal, HisCity.class);
    }

    public HitsMetadata<HisCity> searchTermCity(String val) throws IOException {
        return client.search(s -> s
                .index(IndexName.HIS_CITIES)
                .query(q -> q
                        .term(t -> t
                                .field("name.keyword")
                                .value(v -> v.stringValue(val))
                                .boost(1f)
                                .caseInsensitive(true)
                        )), HisCity.class).hits();
    }

    public <T> HitsMetadata<T> searchTerm(String index, String termVal, Class<T> clzz) throws IOException {
        return client.search(s -> s
                        .index(index)
                        .query(q -> q
                                .term(t -> t
                                        .field("name")
                                        .value(v -> v.stringValue(termVal)))), clzz)
                .hits();

    }

    public HitsMetadata<HisDistrict> searchDistrict(String cityId, String searchVal) throws IOException {
        String normalizedSearch = normalizedSearch(searchVal, dictionary.getDistrictDic());
        return searchMatch(IndexName.HIS_DISTRICTS, normalizedSearch, "cityId", cityId, HisDistrict.class);
    }

    public HitsMetadata<HisDistrict> searchTermDistrict(String cityId, String searchVal) throws IOException {
        List<Query> mustList = new ArrayList<>();
        mustList.add(new Query.Builder().match(m -> m.field("cityId").query(q -> q.stringValue(cityId))).build());
        mustList.add(new Query.Builder().term(t -> t.field("name.keyword").value(v -> v.stringValue(searchVal))).build());
        return searchTerm(IndexName.HIS_DISTRICTS, mustList, HisDistrict.class);
    }

    public HitsMetadata<HisWard> searchTermHisWard(String districtId, String searchVal) throws IOException {
        List<Query> mustList = new ArrayList<>();
        mustList.add(new Query.Builder().match(m -> m.field("districtId").query(q -> q.stringValue(districtId))).build());
        mustList.add(new Query.Builder().term(t -> t.field("name.keyword").value(v -> v.stringValue(searchVal))).build());
        return searchTerm(IndexName.HIS_DISTRICTS, mustList, HisWard.class);
    }

    public HitsMetadata<HisWard> searchMatchHisWard(String districtId, String searchVal) throws IOException {
        String normalizedSearch = normalizedSearch(searchVal, dictionary.getWardDic());
        return searchMatch(IndexName.HIS_WARDS, normalizedSearch, "districtId", districtId, HisWard.class);
    }

    private <T> HitsMetadata<T> searchTerm(String index, List<Query> queries, Class<T> returnClass) throws IOException {
        return client.search(s -> s
                .index(index)
                .query(q -> q
                        .bool(b -> b.must(queries))), returnClass).hits();
    }

    private <T> HitsMetadata<T> searchMatch(String index, String searchVal,
                                            String idFieldName, String idFieldVal, Class<T> returnClass) throws IOException {
        List<Query> mustList = new ArrayList<>();
        List<Query> shouldList = new ArrayList<>();
        List<Query> mustMatchCase = new ArrayList<>();
        List<String> listMatch = buildMatchCase(searchVal);
        for (String matchCase : listMatch) {
            mustMatchCase.add(new Query.Builder()
                    .match(m -> m
                            .field("normalizedName")
                            .query(q -> q.stringValue(matchCase))).build());
        }
        shouldList.add(new Query.Builder()
                .match(m -> m.field("normalizedName.keyword")
                        .query(q -> q.stringValue(searchVal)))
                .build());
        shouldList.add(new Query.Builder().bool(b -> b.must(mustMatchCase)).build());
        mustList.add(new Query.Builder()
                .match(m -> m.field(idFieldName)
                        .query(q -> q.stringValue(idFieldVal)))
                .build());
        mustList.add(new Query.Builder()
                .bool(b -> b.should(shouldList))
                .build());
        Query query = new Query.Builder()
                .bool(b -> b.must(mustList))
                .build();
        return client.search(s -> s
                        .index(index)
                        .query(query),
                returnClass).hits();
    }


    public String normalizedSearch(String val, List<String> dic) {
        String normalize = StringUtils.nomalizeUnicodeAndSpecialChars(val);
        for (String di : dic) {
            if (normalize.startsWith(di)) {
                normalize = normalize.replaceFirst(di, "");
                break;
            }
        }
        return normalize.trim();
    }

    private List<String> buildMatchCase(String searchVal) {
        List<String> splited = new ArrayList<>(Arrays.asList(searchVal.split(" ")));
        if (splited.size() == 1) {
            return splited;
        }
        return splited.stream().map(out -> "*".concat(out).concat("*")).collect(Collectors.toList());
    }
}
