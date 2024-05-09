package com.example.ElasticSearch.controller;

import com.example.ElasticSearch.service.InsertDataToESFromFiles;
import com.example.ElasticSearch.service.ElasticSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/es")
public class ElasticSearchController {

    @Autowired
    InsertDataToESFromFiles insertDataToESFromFiles;
    @Autowired
    private ElasticSearchService elasticSearchService;

    /**
     * 특정 경로에 있는 json file을 특정 index에 데이터를 넣는다.
     *
     * @throws IOException
     */
    @PostMapping("/insert_file_data")
    public void insertFileData() throws IOException {
        insertDataToESFromFiles.insertFileData("index-222", "C:\\data_set_for_es\\sample_data.json");
    }

    /**
     * 특정 index의 데이터 건수를 조회한다.
     *
     * @param indexName
     * @return
     * @throws IOException
     */
    @GetMapping("/get_count_index")
    public ResponseEntity<Object> getCountIndex(String indexName) throws IOException {
        return ResponseEntity.ok(elasticSearchService.getCount(indexName));
    }

    /**
     * 특정 id 값을 가지고 있는 데이터를 조회한다.
     *
     * @return
     * @throws IOException
     */
    @GetMapping("/get_data_from_es")
    public ResponseEntity<Object> getDataFromES() throws IOException {
        return ResponseEntity.ok(elasticSearchService.getDataFromES());
    }

    /**
     * 특정 index의 특정 필드를 기준으로 키워드를 가지고 있는 데이터를 조회한다.
     * ex) localhost:8080/es/search_by_keyword?index=index-movie&field=keywords&keyword=prison cell
     *
     * @param index
     * @param field
     * @param keyword
     * @return
     * @throws IOException
     */
    @GetMapping("/search_by_keyword")
    public ResponseEntity<Object> searchByKeyword(String index, String field, String keyword) throws IOException {
        return ResponseEntity.ok(elasticSearchService.searchByKeyword(index, field, keyword));
    }


    /**
     * 모든 인덱스 정보를 List 형태로 출력 한다.
     *
     * @return
     * @throws IOException
     */
    @GetMapping("/get_index_list")
    public ResponseEntity<Object> getIndexList() throws IOException {
        return ResponseEntity.ok(elasticSearchService.getIndexList());
    }

}
