package br.com.carrefour.despensacarrefour.bot;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class Bot extends TelegramLongPollingBot{
	
	@Value("${bot.token}")
	private String token;
	
	@Value("${bot.username}")
	private String username;
	
	@Singleton
	ObjectMapper mapper = new ObjectMapper();

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
		
		try {
			if(mensagem.isCommand() && mensagem.getText().trim().equalsIgnoreCase("/start")) {
				StringBuilder sb = new StringBuilder();
				sb.append("Bem Vindo ao Bot Despensa Carrefour! Aqui você pode informar os produtos que estão faltando na sua despensa e mostraremos se existem promoções para os produtos.");
				sb.append("\nFácil assim. Agora me diz, quais produtos estão faltando na sua despensa? Escreva os produtos separados por vígula. \n(Para exibir todos os produtos em promoção, escreva 'todos')");
				text = sb.toString();
			} else if(mensagem.getText().trim().equalsIgnoreCase("todos")) {
				text = getListaTodosProdutos().toString();
			} else {
				List<String> listaProdutosCliente = List.of(mensagem.getText().split(","));
				List<Produto> listaProdutosEmPromocao = getListaProdutosPromocao(listaProdutosCliente);
				
				if(listaProdutosEmPromocao.isEmpty())
					text = "Infelizmente os produtos que você informou não estão em promoção";
				else
					text = listaProdutosEmPromocao.toString();
			}
			
			response.setText(text);
			execute(response);
			log.info("Mensagem enviada \"{}\" to {}", text, chatId);
		} catch (TelegramApiException e) {
			log.error("Falha ao enviar a mensagem \"{}\" para {} acontece o erro: {}", text, chatId, e.getMessage());
		} catch (JsonMappingException e) {
			log.error("Falha ao enviar a mensagem \"{}\" para {} acontece o erro: {}", text, chatId, e.getMessage());
		} catch (JsonProcessingException e) {
			log.error("Falha ao enviar a mensagem \"{}\" para {} acontece o erro: {}", text, chatId, e.getMessage());
		}
	}
	
	public List<Produto> getListaProdutosPromocao(List<String> listaProdutosCliente) throws JsonMappingException, JsonProcessingException{
		List<Produto> listaTodosProdutos = getListaTodosProdutos();
		
		return listaTodosProdutos.stream()
				.filter((produto) -> listaProdutosCliente.stream()
						.anyMatch(produtoCliente -> produtoCliente.trim().equalsIgnoreCase(produto.getNome().trim())))
				.collect(Collectors.toList());
	}
	
	public List<Produto> getListaTodosProdutos() throws JsonMappingException, JsonProcessingException{
		return mapper.readValue(Produto.listaTodosProdutos().toString(), new TypeReference<List<Produto>>(){});
	}

}
