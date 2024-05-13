package com.example.ElasticSearch.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InsertDataToESFromFiles {

    @Autowired
    private RestHighLevelClient client;


    /**
     * 특정 경로의 파일을 읽어와서 ES에 데이터를 삽입하는 코드 (조금 지져분한데...!!!)
     *
     * @param index
     * @param filePath
     */
    public void insertFileData(String index, String filePath) {
        try {

            // Check if the index exists
            boolean indexExists = client.indices().exists(new GetIndexRequest(index), RequestOptions.DEFAULT);
            if (!indexExists) {
                // create index
                client.indices().create(new CreateIndexRequest(index), RequestOptions.DEFAULT);
            } else if (!indexExists) {
                throw new IllegalArgumentException("Index " + index + " does not exist");
            }

            // Read the file content
            String fileContent = new String(Files.readAllBytes(Paths.get(filePath)));

            // Parse the JSON
            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> dataList = objectMapper.readValue(fileContent, new TypeReference<List<Map<String, Object>>>() {
            });

            // Iterate over the data list
            for (Map<String, Object> data : dataList) {
                // Check if the release_date field is empty or null
                if (data.get("release_date") == null || data.get("release_date").toString().isEmpty()) {
                    // Set a default date value
                    data.put("release_date", "2000-01-01");
                }

                // Parse the genres string into a list
                String genresString = (String) data.get("genres");
                List<Map<String, Object>> genres = objectMapper.readValue(genresString, new TypeReference<List<Map<String, Object>>>() {
                });

                // Process the genres list to extract the name values
                List<String> genreNames = genres.stream()
                        .map(genre -> (String) genre.get("name"))
                        .collect(Collectors.toList());
                data.put("genres", genreNames);

                // Create the IndexRequest without specifying an id
                IndexRequest request = new IndexRequest(index);

                // Elasticsearch will automatically generate a unique id for each document
                request.source(data, XContentType.JSON);

                // Send the IndexRequest
                client.index(request, RequestOptions.DEFAULT);
            }
        } catch (IOException e) {
            // Handle the exception here
            e.printStackTrace();
        }
    }

    /**
     * 특정 경로의 파일을 읽어와서 ES에 데이터를 삽입하는 코드 (조금 지져분한데...!!!)
     *
     * @param index
     * @param filePath
     */
    public void insertFileDataToNews(String index, String filePath) {
        try {

            // Check if the index exists
            boolean indexExists = client.indices().exists(new GetIndexRequest(index), RequestOptions.DEFAULT);
            if (!indexExists) {
                // create index
                client.indices().create(new CreateIndexRequest(index), RequestOptions.DEFAULT);
            } else if (!indexExists) {
                throw new IllegalArgumentException("Index " + index + " does not exist");
            }

            // Read the file content
            String fileContent = new String(Files.readAllBytes(Paths.get(filePath)));

            // Parse the JSON
            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> dataList = objectMapper.readValue(fileContent, new TypeReference<List<Map<String, Object>>>() {
            });

            // Iterate over the data list
            for (Map<String, Object> data : dataList) {

                // Create the IndexRequest without specifying an id
                IndexRequest request = new IndexRequest(index);

                // Elasticsearch will automatically generate a unique id for each document
                request.source(data, XContentType.JSON);

                // Send the IndexRequest
                client.index(request, RequestOptions.DEFAULT);
            }
        } catch (IOException e) {
            // Handle the exception here
            e.printStackTrace();
        }
    }

}