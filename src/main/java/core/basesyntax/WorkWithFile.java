package core.basesyntax;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class WorkWithFile {
    public void getStatistic(String fromFileName, String toFileName) {
        Path filePath = new File(fromFileName).toPath();

        createNewFile(toFileName);

        int supplyTotal = countFieldValueFromCsvFile(CsvFields.supply, filePath);
        int buyTotal = countFieldValueFromCsvFile(CsvFields.buy, filePath);

        writeResultToTheFile(supplyTotal, buyTotal, new File(toFileName).toPath());
    }

    private void createNewFile(String fileName) {
        try {
            Files.deleteIfExists(Paths.get(fileName));
            new File(fileName).createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int countFieldValueFromCsvFile(CsvFields field, Path filePath) {
        int fieldTotal = 0;

        try {
            String[] fileContent = Files.readAllLines(filePath).toArray(new String[0]);

            for (String line : fileContent) {
                String[] fields = line.split(",");
                if (fields[0].equals(field.name())) {
                    fieldTotal += Integer.parseInt(fields[1]);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Can't read the file " + filePath, e);
        }

        return fieldTotal;
    }

    private void writeResultToTheFile(int supplyTotal, int buyTotal, Path newFilePath) {
        CsvFields[] fieldsList = CsvFields.values();

        for (CsvFields field : fieldsList) {
            switch (field) {
                case supply:
                    writeToTheFile(newFilePath, field.name(), supplyTotal);
                    break;
                case buy:
                    writeToTheFile(newFilePath, "\n" + field.name(), buyTotal);
                    break;
                default:
                    writeToTheFile(newFilePath, "\n" + field.name(), supplyTotal - buyTotal);
            }
        }
    }

    private void writeToTheFile(Path path, String fieldToWrite, int valueToWrite) {
        try {
            Files.write(path,
                    (fieldToWrite
                            + ","
                            + valueToWrite)
                            .getBytes(),
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("Can't write to the file", e);
        }
    }
}
