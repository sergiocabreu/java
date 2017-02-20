package com.sergiocabreu.java8.eclipse;

import java.util.Calendar;

public class Gasto {

	private double valor;
	private String tipo;
	private Calendar data;

	@Override
	public String toString() {
		return tipo;
	}

	public Gasto(double valor, String tipo, Calendar data) {
		this.valor = valor;
		this.tipo = tipo;
		this.data = data;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Calendar getData() {
		return data;
	}

	public void setData(Calendar data) {
		this.data = data;
	}

}
