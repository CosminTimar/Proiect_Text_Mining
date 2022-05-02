package com.company;

import java.util.TreeMap;

public class DictionaryAttribute {
    private final static int DEFAULT_COUNT = 1;
    private TreeMap<String,Integer> docWhereAppearance = new TreeMap<>();
    private int count;

    public DictionaryAttribute(String path, int count){
        addDocPath(path);
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public TreeMap<String, Integer> getDocWhereAppearance() {
        return docWhereAppearance;
    }


    public DictionaryAttribute increment(){
       this.count = count+1;
       return this;
    }

    @Override
    public String toString() {
        return ", count=" + count;
    }

    public DictionaryAttribute addDocPath(String path){
        docWhereAppearance.put(path,DEFAULT_COUNT);
        return this;
    }

    public DictionaryAttribute incrementForDoc(String path){
        docWhereAppearance.put(path,docWhereAppearance.get(path) + DEFAULT_COUNT);
        return this;
    }

    public void printPath(){
        System.out.println(docWhereAppearance);
    }

    public void printPathWithAppearance() {
       docWhereAppearance.forEach((k,v)-> System.out.println("Doc: "+k+" count: "+v));
    }


}
