package learningtest.template;

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

    public Integer lineReadTemplate(String filepath, LineCallback callback, int initVal) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filepath));
            Integer res = initVal;
            String line = null;
            while ((line = br.readLine()) != null) {
                res = callback.doSomethingWithLine(line, res);
            }
            return res;
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
        LineCallback callback = new LineCallback() {
            @Override
            public int doSomethingWithLine(String line, Integer initVal) {
                return initVal + Integer.parseInt(line);
            }};
        return lineReadTemplate(filepath, callback, 0);
    }

    public Integer calcMultiply(String filepath) throws IOException {
        LineCallback callback = new LineCallback() {
            @Override
            public int doSomethingWithLine(String line, Integer initVal) {
                return initVal *= Integer.parseInt(line);
            }};
        return lineReadTemplate(filepath, callback, 1);
    }
}