package com.example.ElasticSearch.controller;

import com.example.ElasticSearch.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * CSV 파일을 JSON 파일로 변환한다.
     * inputFilePath가 csv 파일이고, outputFilePath가 json 파일이다.
     *
     * @param inputFilePath
     * @param outputFilePath
     * @throws IOException
     */
    @PostMapping("/convert-csv-file-to-json")
    public void convertCsvFileToJson(@RequestParam("inputFilePath") String inputFilePath,
                                     @RequestParam("outputFilePath") String outputFilePath) throws IOException {
        fileService.convertCsvFileToJson(inputFilePath, outputFilePath);
    }

}
