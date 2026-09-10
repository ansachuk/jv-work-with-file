package core.basesyntax;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class WorkWithFile {
    public void getStatistic(String fromFileName, String toFileName) {
        Path path = new File(fromFileName).toPath();
        int supplyTotal = 0;
        int buyTotal = 0;

        try {
            Files.deleteIfExists(Paths.get(toFileName));
            new File(toFileName).createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            String[] lineList = Files.readAllLines(path).toArray(new String[0]);

            for (String line : lineList) {
                String[] fields = line.split(",");
                if (fields[0].equals(CsvFields.supply.name())) {
                    supplyTotal += Integer.parseInt(fields[1]);
                } else {
                    buyTotal += Integer.parseInt(fields[1]);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Can't read the file " + fromFileName, e);
        }

        CsvFields[] fieldsList = CsvFields.values();
        Path newFilePath = new File(toFileName).toPath();

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
