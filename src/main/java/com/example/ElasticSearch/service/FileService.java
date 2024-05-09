package com.example.ElasticSearch.service;

import com.example.ElasticSearch.util.CsvFileConvertToJsonFile;
import com.example.ElasticSearch.util.CustomMultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Service
public class FileService {

    /**
     * Convert a CSV file to a JSON file
     *
     * @param inputFilePath
     * @param outputFilePath
     * @throws IOException
     */
    public void convertCsvFileToJson(String inputFilePath, String outputFilePath) throws IOException {
        File file = new File(inputFilePath);
        Path path = Paths.get(inputFilePath);
        String name = file.getName();
        String originalFileName = file.getName();
        String contentType = Files.probeContentType(path);
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (final IOException e) {
        }
        MultipartFile multipartFile = new CustomMultipartFile(content, name, contentType, originalFileName);

        List<Map<?, ?>> data = CsvFileConvertToJsonFile.convertCsvToJson(multipartFile);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(Paths.get(outputFilePath).toFile(), data);
    }
}