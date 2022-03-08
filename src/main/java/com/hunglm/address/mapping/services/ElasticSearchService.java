package com.hunglm.address.mapping.services;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.CountRequest;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.IndexOperation;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import com.hunglm.address.mapping.entities.ESDocument;
import com.hunglm.address.mapping.entities.ym.City;
import com.hunglm.address.mapping.entities.ym.District;
import com.hunglm.address.mapping.entities.ym.Ward;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ElasticSearchService {
  private final ElasticsearchClient client;

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
    return client.search(s -> s
            .index(indexName)
            .query(q -> q
                .match(t -> t
                    .field("name")
                    .query(q1 -> q1.stringValue(searchVal))
                )),
        clzz).hits();
  }

  public HitsMetadata<City> searchCity(String searchVal) throws IOException {
    return search("ym-cities", searchVal, City.class);
  }

  public HitsMetadata<City> searchTermCity(String indexName, String val) throws IOException {
    return client.search(s -> s
            .index(indexName)
            .query(q -> q
                .term(t -> t
                    .field("name")
                    .value(v -> v.stringValue(val))
                ))
        , City.class
    ).hits();
  }

  public HitsMetadata<District> searchDistrict(String searchVal) throws IOException {
    return search("ym-districts", searchVal, District.class);
  }

  public HitsMetadata<Ward> searchWard(String searchVal) throws IOException {
    return search("ym-wards", searchVal, Ward.class);
  }


}
