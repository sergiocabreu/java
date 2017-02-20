package com.sergiocabreu.java8.datas;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.SynchronousQueue;

public class Datas {

	public static void main(String[] args) {

		
		long milisegundo = new Date().getTime();
		
		System.out.println(milisegundo);
	}
	
	private void teste(){
		LocalDate hoje = LocalDate.now();
		System.out.println(hoje);

		LocalDate olimpiadasRio = LocalDate.of(2016, Month.JUNE, 5);

		int anos = olimpiadasRio.getYear() - hoje.getYear();

		System.out.println(anos);

		// Period periodo = Period.between(hoje, olimpiadasRio);

		LocalDate proximasOlipiadas = olimpiadasRio.plusYears(4);

		System.out.println(proximasOlipiadas);

		DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		String valorFormatado = proximasOlipiadas.format(formatador);

		System.out.println(valorFormatado);
	}

}
