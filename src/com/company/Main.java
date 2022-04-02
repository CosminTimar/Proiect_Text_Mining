package com.company;

public class Main {

    public static void main(String[] args) {
	    MyDictionary dictionary = new MyDictionary("/home/el-pingu/Projects/Reuters_34");
        dictionary.extractStopWords();
        dictionary.printDictionary();

        //dictionary.printWhereAppearance("york");



    }
}
