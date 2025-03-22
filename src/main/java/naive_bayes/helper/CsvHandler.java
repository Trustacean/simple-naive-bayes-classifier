package naive_bayes.helper;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class CsvHandler {

    @SuppressWarnings("CallToPrintStackTrace")
    public static List<String[]> readCsv(String file, char separator, boolean header) {
        try {
            FileReader filereader = new FileReader(file);

            CSVParser parser = new CSVParserBuilder().withSeparator(separator).build();
            CSVReader csvReader;
            csvReader = new CSVReaderBuilder(filereader)
            .withCSVParser(parser)
            .build();
            List<String[]> allData = csvReader.readAll();
            if (!header) {
                String[] defaultHeader = new String[allData.get(0).length];
                for (int i = 0; i < allData.get(i).length; i++) {
                    defaultHeader[i] = "Column" + i;
                }
                allData.add(0, defaultHeader);
            }
            return allData;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
