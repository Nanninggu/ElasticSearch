package com.example.ElasticSearch.controller;

import com.example.ElasticSearch.service.ElasticSearchService;
import com.example.ElasticSearch.service.InsertDataToESFromFiles;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/es")
public class SearchServiceController {

    @Autowired
    InsertDataToESFromFiles insertDataToESFromFiles;

    @Autowired
    private ElasticSearchService elasticSearchService;

    /**
     * index와 body값을 고정값으로 넣고, 대량의 데이터를 indexing 시킨뒤 조회를 해보자.
     * 결국 키워드만 넣어주면 해당 키워드를 가지고 있는 데이터를 얼마나 빠르게 조회해 오는지 테스트.
     *
     * @param keyword
     * @return
     * @throws IOException
     */
    @GetMapping("/search_keyword")
    public ModelAndView searchByKeyword(@RequestParam String keyword) throws IOException {
        SearchResponse searchResponse = elasticSearchService.searchByKeyword("index-movie", "keywords", keyword);
        ModelAndView modelAndView = new ModelAndView("searchResults");
        modelAndView.addObject("results", searchResponse.getHits().getHits());
        return modelAndView;
    }

    /**
     * 특정 index값에 body 필드를 조회하여 검색된 데이터를 리턴한다.
     *
     * @param keyword
     * @return
     * @throws IOException
     */
    @GetMapping("/get_body")
    public ResponseEntity<List<Object>> getBody(@RequestParam String keyword) throws IOException {
        List<Object> bodies = elasticSearchService.searchByKeywordBody("index-222", "body", keyword);
        return ResponseEntity.ok(bodies);
    }

    /**
     * 지정된 index name으로 해당 path의 파일을 읽어서 데이터를 넣는다.
     *
     * @throws IOException
     */
    @PostMapping("/insert_file_of_json")
    public void insertFileOfJson() throws IOException {
        insertDataToESFromFiles.insertFileDataToNews("index-news-kor", "C:\\data_set_for_es\\news\\json\\2024-05-13_11-56-12.json");
    }

}
