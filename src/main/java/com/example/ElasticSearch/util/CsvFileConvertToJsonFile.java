package com.example.ElasticSearch.util;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CsvFileConvertToJsonFile {

    /**
     * Convert a CSV file to a JSON file
     * @param file
     * @return
     * @throws IOException
     */
    public static List<Map<?, ?>> convertCsvToJson(MultipartFile file) throws IOException {
        CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
        CsvMapper mapper = new CsvMapper();
        MappingIterator<Map<?, ?>> readValues =
                mapper.readerFor(Map.class).with(bootstrapSchema).readValues(file.getInputStream());
        return readValues.readAll();
    }

}
