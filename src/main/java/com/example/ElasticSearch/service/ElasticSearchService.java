package com.example.ElasticSearch.service;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.indices.*;
import org.elasticsearch.cluster.metadata.MappingMetadata;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.ValueCount;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class ElasticSearchService {

    @Autowired
    private RestHighLevelClient client;

    /**
     * 특정 인덱스 값과 id 값을 정상적으로 조회해 오는지 테스트 하는 코드.
     *
     * @return
     * @throws IOException
     */
    public Map<String, Object> getDataFromES() throws IOException {
        GetRequest getRequest = new GetRequest("index-001", "S08YVo8BzMdr2BQJnSaU");
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);

        return getResponse.getSourceAsMap();
    }

    /**
     * 입력한 인덱스 값을 기준으로 해당 인덱스가 가지고 있는 필드수를 카운팅 한다.
     *
     * @param index
     * @return
     * @throws IOException
     */
    public long getCount(String index) throws IOException {
        CountRequest countRequest = new CountRequest(index);
        countRequest.query(QueryBuilders.matchAllQuery());
        CountResponse searchResponse = client.count(countRequest, RequestOptions.DEFAULT);

        return searchResponse.getCount();
    }

    /**
     * 이건 body 검색을 위한 코드인데, body 값을 고정값으로 설정해서 body 값을 조회해 온다.
     * 다른 값을 기준으로 값을 가져올수도 있음! 필드 파라미터 값만 바꿔주면 된다.
     *
     * @param index
     * @param field
     * @param keyword
     * @return
     * @throws IOException
     */
    public SearchResponse searchByKeyword(String index, String field, String keyword) throws IOException {

        // Create a SearchSourceBuilder
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // Add a match query
        sourceBuilder.query(QueryBuilders.matchQuery(field, keyword));
        // Create a SearchRequest
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.source(sourceBuilder);
        // Execute the search and get the response
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        return searchResponse;
    }

    /**
     * 이건 인덱스 리스트 가져오는 코드!
     *
     * @return
     * @throws IOException
     */
    public String[] getIndexList() throws IOException {

        // Create a GetIndexRequest
        GetIndexRequest request = new GetIndexRequest("*");
        // Execute the request and get the response
        GetIndexResponse getIndexResponse = client.indices().get(request, RequestOptions.DEFAULT);
        // Return the list of index names

        return getIndexResponse.getIndices();
    }


    /**
     * 이건 body 검색을 위한 코드인데, body 값을 고정값으로 설정해서 body 값을 조회해 온다.
     *
     * @param index
     * @return
     * @throws IOException
     */
    public List<Object> searchByKeywordBody(String index, String field, String keyword) throws IOException {

        // Create a SearchSourceBuilder
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // Add a match query
        sourceBuilder.query(QueryBuilders.matchQuery(field, keyword));
        // Create a SearchRequest
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.source(sourceBuilder);
        // Execute the search and get the response
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        // Get the body field from each hit and add it to a list
        List<Object> bodies = new ArrayList<>();
        Arrays.stream(searchResponse.getHits().getHits()).forEach(hit -> bodies.add(hit.getSourceAsMap().get("body")));

        return bodies;
    }

}
