package com.sergiocabreu.java8;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	List<String> palavras = new ArrayList<String>();
    	palavras.add("Primeiro");
    	palavras.add("Segundo");
    	palavras.add("Terceiro");
        
    	palavras.sort(Comparator.comparing( s -> s.length() ));
    	
        palavras.sort(Comparator.comparing(String::length));        
        
		palavras.forEach(s -> System.out.println(s));
        palavras.forEach(System.out::println);
    }
}
 