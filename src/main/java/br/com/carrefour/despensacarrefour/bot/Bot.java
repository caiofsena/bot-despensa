package br.com.carrefour.despensacarrefour.bot;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class Bot extends TelegramLongPollingBot{
	
	@Value("${bot.token}")
	private String token;
	
	@Value("${bot.username}")
	private String username;

	@Override
	public void onUpdateReceived(Update update) {
		if (update.hasMessage()) {
			Message mensagem = update.getMessage();
			analiseMensagem(mensagem);
		}
	}

	@Override
	public String getBotUsername() {
		return username;
	}

	@Override
	public String getBotToken() {
		return token;
	}
	
	public void analiseMensagem(Message mensagem) {
		SendMessage response = new SendMessage();
		Long chatId = mensagem.getChatId();
		response.setChatId(chatId);
		String text = null;
		
		if(mensagem.isCommand() && mensagem.getText().equals("/start")) {
			StringBuilder sb = new StringBuilder();
			sb.append("Bem Vindo ao Bot Despensa Carrefour! Aqui você pode informar os produtos que estão faltando na sua despensa e mostraremos se existem promoções para os produtos.");
			sb.append("\nFácil assim. Agora me diz, quais produtos estão faltando na sua despensa? Escreva os produtos separados por vígula.");
			text = sb.toString();
		} else {
			List<String> listaProdutosCliente = List.of(mensagem.getText().split(","));
			List<Produto> listaProdutosEmPromocao = getListaProdutosPromocao(listaProdutosCliente);
			
			text = listaProdutosEmPromocao.toString();
		}
		
		try {
			response.setText(text);
			execute(response);
			log.info("Sent message \"{}\" to {}", text, chatId);
		} catch (TelegramApiException e) {
			log.error("Failed to send message \"{}\" to {} due to error: {}", text, chatId, e.getMessage());
		}
	}
	
	public List<Produto> getListaProdutosPromocao(List<String> listaProdutosCliente){
		List<Produto> listaProdutosEmPromocao = new ArrayList<Produto>();
		for (String nome : listaProdutosCliente) {
			Produto produto = new Produto(nome, "R$ 50,00");
			listaProdutosEmPromocao.add(produto);
		}
		return listaProdutosEmPromocao;
	}

}
