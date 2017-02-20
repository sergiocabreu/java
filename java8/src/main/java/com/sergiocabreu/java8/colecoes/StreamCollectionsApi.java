package com.sergiocabreu.java8.colecoes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.sergiocabreu.java8.Curso;

public class StreamCollectionsApi {

public static void main(String[] args) {
		
		List<Curso> cursos = new ArrayList<Curso>();
		
		cursos.add(new Curso("Python", 45));
		cursos.add(new Curso("JavaScript", 150));
		cursos.add(new Curso("Java 8", 113));
		cursos.add(new Curso("C", 55));
		
		cursos.stream()
			.filter(c -> c.getAlunos() >= 100)
			.collect(Collectors.toMap(
					c -> c.getNome(),
					c -> c.getAlunos()))
			.forEach((nome, alunos) -> System.out.println(nome +" = " + alunos));
		
		
//		OptionalDouble average = cursos.stream()
//			.filter(c -> c.getAlunos() >= 100)
//			.mapToInt(Curso::getAlunos)
//			.average();		
		
/*		cursos.stream()
			.filter(c->c.getAlunos() >= 100)
			.findAny()
			.ifPresent(c -> System.out.println(c.getNome()));*/
		
	}
	
}
