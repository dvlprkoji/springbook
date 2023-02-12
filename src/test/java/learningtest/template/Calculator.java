package learningtest.template;


import org.hamcrest.Matcher;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {

    public Integer fileReadTemplate(String filepath, BufferedReaderCallback callback) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filepath));
            int ret = callback.doSomethingWithReader(br);
            return ret;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (br != null) {
                try { br.close();}
                catch (IOException e) { System.out.println(e.getMessage());}
            }
        }
    }

    public int calcSum(String filepath) throws IOException {
        return fileReadTemplate(filepath, new BufferedReaderCallback() {
            @Override
            public Integer doSomethingWithReader(BufferedReader bufferedReader) throws IOException {
                Integer sum = 0;
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    sum += Integer.valueOf(line);
                }
                return sum;
            }
        });
    }

    public Integer calcMultiply(String filepath) throws IOException {
        return fileReadTemplate(filepath, new BufferedReaderCallback() {
            @Override
            public Integer doSomethingWithReader(BufferedReader bufferedReader) throws IOException {
                Integer multiply = 1;
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    multiply *= Integer.valueOf(line);
                }
                return multiply;
            }
        });
    }
}