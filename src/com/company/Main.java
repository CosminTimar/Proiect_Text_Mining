package com.company;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
	    MyDictionary dictionary = new MyDictionary("/home/el-pingu/Projects/Reuters_34");
        dictionary.extractStopWords();
        //dictionary.printDictionary();

        //dictionary.printWhereAppearance("york");


        CreateDoc doc = new CreateDoc();
        try {
            doc.createFile(dictionary);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
