package com.sergiocabreu.java8.colecoes;

import java.util.ArrayList;
import java.util.List;

import com.sergiocabreu.java8.Curso;

public class ExemploCursos {

	public static void main(String[] args) {
		
		List<Curso> cursos = new ArrayList<Curso>();
		
		cursos.add(new Curso("Python", 45));
		cursos.add(new Curso("JavaScript", 150));
		cursos.add(new Curso("Java 8", 113));
		cursos.add(new Curso("C", 55));
		
		Curso curso = new Curso("Curso 1", 10);
		
		int alunos = 0;
		Curso curso2 = new Curso("Curso 2", alunos);
				
/*		cursos.sort(Comparator.comparing(Curso::getAlunos));	
		cursos.sort(Comparator.comparingInt(Curso::getAlunos));*/

/*		System.out.println("Total alunos para cursos >= 100 -------------");
		cursos.stream()
		.filter(c -> c.getAlunos() >= 100)
		.map(c -> c.getAlunos())
		.forEach( total -> System.out.println(total));*/
		
/*		System.out.println("Soma");
		int alunos = cursos.stream()
		.filter(c -> c.getAlunos() >= 100)
		.mapToInt(Curso::getAlunos).sum();
		System.out.println(alunos);*/
		
/*		//reference method
		cursos.stream()
		.filter(c -> c.getAlunos() >= 100)
		.map(Curso::getAlunos)
		.forEach(System.out::println);*/
		
		
		
/*		System.out.println("\n>= 100 -------------");
		cursos.stream()
		.filter(c -> c.getAlunos() >= 100)
		.forEach( c -> System.out.println(c.getNome()));*/
		
/*		System.out.println("\nTodos -------------");
		
		cursos.forEach(c -> System.out.println(c.getNome()));*/
		
		//cursos.forEach(c -> System.out.println(c.getNome()));
	}

}
