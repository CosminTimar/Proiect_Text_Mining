package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CreateDoc {

    public void createFile(MyDictionary dictionary) throws IOException {

        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter("file.txt"));
            List<String> str = getDocumentWords(dictionary);
            for (String word: str) {
                writer.write("@attribute ");
                writer.write(word);
                writer.newLine();
            }
            writer.newLine();
            writer.write("@data");
            writer.newLine();

            getWordsIndex(dictionary,str,writer);
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    private List<String> getDocumentWords(MyDictionary dictionary){
        Set<String> keys = dictionary.getDictionary().keySet();
        return new ArrayList<>(keys);
    }

    private void getWordsIndex(MyDictionary dictionary, List<String> key,BufferedWriter writer) throws IOException {

        ArrayList<String> file = dictionary.getFineName();
        writer.newLine();
        for(String fil : file) {
            StringBuilder format = new StringBuilder(fil + " # ");
            for (String word : key) {
                if (dictionary.getDictionary()
                        .get(word)
                        .getDocWhereAppearance()
                        .containsKey(fil))
                {
                        format
                                .append(key
                                        .indexOf(word))
                                .append(":")
                                .append(dictionary
                                    .getDictionary()
                                    .get(word)
                                        .getDocWhereAppearance()
                                        .get(fil))
                            .append(" ");
                }
            }
            writer.write(String.valueOf(format));
            writer.newLine();
        }

    }
}
