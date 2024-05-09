package com.example.ElasticSearch.config;

import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {

    /**
     * ElasticSearch RestHighLevelClient 빈 등록,
     * 7버전과 호환되는 헤더를 설정해준다. (로컬에 설치한 ElasticSearch 서버와 Kibana 8버전이다.)
     *
     * @return
     */
    @Bean
    public RestHighLevelClient client() {
        RestClientBuilder builder = RestClient.builder(new HttpHost("localhost", 9200, "http"))
                .setDefaultHeaders(new BasicHeader[]{
                        new BasicHeader("Accept", "application/vnd.elasticsearch+json;compatible-with=7"),
                        new BasicHeader("Content-Type", "application/vnd.elasticsearch+json;compatible-with=7")
                });

        return new RestHighLevelClient(builder);
    }

}