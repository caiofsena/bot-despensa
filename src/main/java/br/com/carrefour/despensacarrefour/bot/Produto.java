package br.com.carrefour.despensacarrefour.bot;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Produto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String nome;
	private String preco;
	
	public Produto(String nome, String preco) {
		this.nome = nome;
		this.preco = preco;
	}

	@Override
	public String toString() {
		return "{nome: "+nome+", valor: "+preco+"}";
	}
	
}
