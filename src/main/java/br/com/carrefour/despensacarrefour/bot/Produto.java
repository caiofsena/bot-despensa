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
	private String unidadeMedida;
	private boolean promocao;
	
	@Override
	public String toString() {
		return nome + " - " + preco;
	}
	
	public static String listaTodosProdutos(){
		return "[ {\"nome\" : \"banana\", \"preco\": \"R$ 3,00\", \"unidadeMedida\": \"kg\", \"promocao\": true} ,"
				+ "{\"nome\" : \"tomate\", \"preco\": \"R$ 5,50\", \"unidadeMedida\": \"kg\", \"promocao\": false} ,"
				+ "{\"nome\" : \"cebola\", \"preco\": \"R$ 4,20\", \"unidadeMedida\": \"kg\", \"promocao\": false} ,"
				+ "{\"nome\" : \"pneu\", \"preco\": \"R$ 150,00\", \"unidadeMedida\": \"unidade\", \"promocao\": true} ,"
				+ "{\"nome\" : \"queijo\", \"preco\": \"R$ 7,00\", \"unidadeMedida\": \"kg\", \"promocao\": false} ,"
				+ "{\"nome\" : \"presunto\", \"preco\": \"R$ 5,00\", \"unidadeMedida\": \"kg\", \"promocao\": true} ,"
				+ "{\"nome\" : \"alvejante\", \"preco\": \"R$ 16,00\", \"unidadeMedida\": \"ml\", \"promocao\": false} ,"
				+ "{\"nome\" : \"cotonete\", \"preco\": \"R$ 4,80\", \"unidadeMedida\": \"g\", \"promocao\": false} ,"
				+ "{\"nome\" : \"oregano\", \"preco\": \"R$ 0,90\", \"unidadeMedida\": \"mg\", \"promocao\": false} ,"
				+ "{\"nome\" : \"pilha\", \"preco\": \"R$ 7,80\", \"unidadeMedida\": \"unidade\", \"promocao\": false} ]";
	}
	
}
