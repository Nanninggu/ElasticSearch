package com.example.ElasticSearch.controller;

import com.example.ElasticSearch.service.ElasticSearchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@RestController
@RequestMapping("/news")
public class NewsGetController {

    @Autowired
    private ElasticSearchService elasticSearchService;

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * index와 body값을 고정값으로 넣고, 대량의 데이터를 indexing 시킨뒤 조회를 해보자.
     * 결국 키워드만 넣어주면 해당 키워드를 가지고 있는 데이터를 얼마나 빠르게 조회해 오는지 테스트.
     *
     * @param keyword
     * @return
     * @throws IOException
     */
    @GetMapping("/search_news")
    public ModelAndView searchByKeyword(@RequestParam String keyword) throws IOException {
        SearchResponse searchResponse = elasticSearchService.searchByKeyword("index-news", "title", keyword);
        ModelAndView modelAndView = new ModelAndView("searchResults_news");
        modelAndView.addObject("results", searchResponse.getHits().getHits());
        return modelAndView;
    }

}
