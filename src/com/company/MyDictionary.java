package com.company;

import opennlp.tools.stemmer.PorterStemmer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class MyDictionary {

    private final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    private final TreeMap<String, DictionaryAttribute> dictionary = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private final ArrayList<String> stopWords = new ArrayList<>();
    private final PorterStemmer porterStemmer = new PorterStemmer();

    public MyDictionary(String path) {
        try {
            loadStopWords("/home/el-pingu/Projects/Proiect_Text_Mining/Resources/stop_words_english.xml");
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        constructDictionary(path);
    }

    public void constructDictionary(String path) {
        try {

            File dir = new File(path);
            File[] listOfXml = dir.listFiles();

            if( listOfXml != null)
                extractInfoFromDirectory(listOfXml);
        } catch(ParserConfigurationException | SAXException | IOException e){
                e.printStackTrace();
        }


        dictionary.remove("");
    }

    private void extractInfoFromDirectory(File[] listOfXml) throws ParserConfigurationException, SAXException, IOException {
        for(File file : listOfXml) {

            extractAndNormalizeXml(file);
        }
    }

    private void extractAndNormalizeXml(File file) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);


        doc.getDocumentElement().normalize();

        NodeList docWordList = doc.getElementsByTagName("p");

        for (int i = 0; i < docWordList.getLength(); i++) {
            Node node = docWordList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                extractNoNeedCharacters((Element) node, file.getName());

            }
        }
    }

    public void loadStopWords(String path) throws ParserConfigurationException, IOException, SAXException {
        File file = new File(path);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);


        doc.getDocumentElement().normalize();

        NodeList docWordList = doc.getElementsByTagName("word");

        for (int i = 0; i < docWordList.getLength(); i++) {
            Node node = docWordList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                extractStopWords((Element) node);

            }
        }

    }

    private void extractStopWords(Element node) {

        String stopWord = node.getTextContent();
        stopWords.add(stopWord);
        //System.out.println(stopWord);
    }

    private boolean isNumber(String year){
        try {
            Integer.parseInt(year);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

    private List<String> stemmingTheWords(String[] words){
       List<String> theWords = new ArrayList<>();
         for(String word:words) {
            porterStemmer.stem(word);
            theWords.add(porterStemmer.toString());
        }
         return theWords;
    }

    private void extractNoNeedCharacters(Element node,String path) {

        String[] words = node
                .getTextContent()
                .toLowerCase(Locale.ROOT)
                .split("[\\p{Punct}\\s]+");

        words = stemmingTheWords(words).toArray(new String[0]);


        extractNoYearNumber(path, words);
    }

    private void extractNoYearNumber(String path, String[] words) {

        List<String> list = new ArrayList<>(Arrays.asList(words));
        for (String year: words) {
            if(isNumber(year) && (Integer.parseInt(year)< 1000 || Integer.parseInt(year)>2022)){
               list.remove(year);
            }
        }
        words = list.toArray(new String[0]);
        constructTreeMap(words, path);
    }

    private void constructTreeMap(String[] words, String path) {
        for (String word : words) {
            if (!dictionary.containsKey(word)) {
                dictionary.put(word, new DictionaryAttribute(path,1));
            } else {
                addWhereWordAppearance(path, word);

            }

        }
    }

    private void addWhereWordAppearance(String path, String word) {
        dictionary.put(word, dictionary.get(word).increment());
        if(!dictionary.get(word).getDocWhereAppearance().containsKey(path)) {
            dictionary.put(word,dictionary.get(word).addDocPath(path));
        }else{
            dictionary.put(word,dictionary.get(word).incrementForDoc(path));
        }
    }

    public void printDictionary(){
        dictionary.forEach((k,v)-> System.out.println("Word: "+k +v));
    }

    public void printWhereAppearance(String wordToFind){
        if(!dictionary.containsKey(wordToFind)){
            System.out.println("Word "+wordToFind+" doesn't exist in this documents.");
        }
        else {
            System.out.println("Word: " + wordToFind);
            dictionary.get(wordToFind).printPath();
            printMaxAppearanceInDoc(wordToFind);
        }
    }

    public void printMaxAppearanceInDoc(String word){
        Map.Entry<String,Integer> max = null;

        for(Map.Entry<String,Integer> maxCount : dictionary.get(word).getDocWhereAppearance().entrySet()){
            if (max == null || maxCount.getValue().compareTo(max.getValue()) > 0)
            {
                max = maxCount;
            }
        }

        assert max != null;
        System.out.println("Doc: "+max.
                getKey() + " count: "+max.getValue());
    }

    public void extractStopWords() {
        for (String word:stopWords) {
            dictionary.remove(word);

        }
    }
}
