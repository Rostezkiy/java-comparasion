package litvinov.r;

import net.ricecode.similarity.JaroWinklerStrategy;
import net.ricecode.similarity.SimilarityStrategy;
import net.ricecode.similarity.StringSimilarityService;
import net.ricecode.similarity.StringSimilarityServiceImpl;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        printResult(compareStrings(getData()));
    }

    private static List<List<String>> getData() {
        ArrayList<List<String>> result = new ArrayList<>();
        List<String> firstArray = new ArrayList<>();
        List<String> secondArray = new ArrayList<>();
        int firstAmount;
        int secondAmount;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("input.txt"))) {
            String line = bufferedReader.readLine();
            firstAmount = Integer.parseInt(line);
            line = bufferedReader.readLine();
            while (line != null) {
                for (int i = 0; i < firstAmount; i++) {
                    firstArray.add(line);
                    line = bufferedReader.readLine();
                }
                if (StringUtils.isNumeric(line)) {
                    secondAmount = Integer.parseInt(line);
                    line = bufferedReader.readLine();
                    for (int i = 0; i < secondAmount; i++) {
                        secondArray.add(line);
                        line = bufferedReader.readLine();
                    }
                }
            }
            result.add(firstArray);
            result.add(secondArray);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("File not found");
        }
        return result;
    }

    private static Map<String, String> compareStrings(List<List<String>> stringArray) {
        SimilarityStrategy strategy = new JaroWinklerStrategy();
        StringSimilarityService service = new StringSimilarityServiceImpl(strategy);
        List<String> stringsOne = stringArray.get(0);
        List<String> stringsTwo = stringArray.get(1);
        Map<String, String> result = new HashMap<>();
        System.out.println("stringsOne = " + stringsOne);
        System.out.println("stringsTwo = " + stringsTwo);
        for (String arrayEntityOne : stringsOne) {
            System.out.println("EntityOne = " + arrayEntityOne);
            for (String arrayEntityTwo : stringsTwo) {
                System.out.println("comparing = " + arrayEntityTwo);
                if (service.score(arrayEntityOne, arrayEntityTwo) > 0.80) {
                    result.put(arrayEntityOne, arrayEntityTwo);
//                        stringsOne.remove(arrayEntityOne);
//                        stringsTwo.remove(arrayEntityTwo);
                } else {
                    result.putIfAbsent(arrayEntityOne, "?");
                }
            }
        }
        return result;
    }

    private static void printResult(Map<String, String> resultMap) {
        File file = new File("output.txt");
        try (BufferedWriter bf = new BufferedWriter(new FileWriter(file))) {
            for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                bf.write(entry.getKey() + ":" + entry.getValue());
                bf.newLine();
            }
            bf.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}