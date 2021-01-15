package com.reed.telegram.bot;

import com.reed.telegram.entity.Domain;
import com.reed.telegram.entity.Message;
import com.reed.telegram.entity.User;
import com.reed.telegram.repository.JpaDomainsRepository;
import com.reed.telegram.repository.JpaMessagesRepository;
import com.reed.telegram.repository.JpaUsersRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class Bot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String botUsername;

    @Value("${bot.token}")
    private String botToken;

    private final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private JpaUsersRepository jpaUsersRepository;
    private JpaMessagesRepository jpaMessagesRepository;
    private JpaDomainsRepository jpaDomainsRepository;

    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public void onUpdateReceived(Update update) {
        log.info(String.format("Message from user @%s received successfully.",
                update.getMessage().getFrom().getUserName()));

        String chatId = update.getMessage().getChatId().toString();
        String userMessage = update.getMessage().getText();
        StringBuilder botMessage = new StringBuilder();

        User user = telegramToUser(update.getMessage().getFrom(), chatId);
        List<User> dbresponse = jpaUsersRepository.findByUsername(user.getUsername());

        if (dbresponse.isEmpty()) {
            log.info("No user with this username. Saving new user to DB...");
            botMessage.append("Здравствуйте.\nПользователь не найден. Вношу данные в базу. Теперь команды доступны.\n");

            sendMessage(chatId, userMessage, botMessage.toString());
            botMessage.delete(0, botMessage.length());

            user.setLastMessageAt(update.getMessage().getDate());
            user.setChatId(chatId);
            jpaUsersRepository.saveAndFlush(user);
            log.info("User successfully saved.");
        } else {
            switch (userMessage) {
                case "/users":
                    log.debug("/users triggered. Sending users list.");
                    List<User> users = jpaUsersRepository.findAll();

                    for (User u : users) {
                        botMessage.append("\nИмя пользователя:\n  ")
                                .append(u.getUsername())
                                .append("\nПоследняя активность:\n  ")
                                .append(DATE_FORMAT.format(new Date(u.getLastMessageAt() * 1000L)))
                                .append("\n");
                    }
                    sendMessage(chatId, userMessage, botMessage.toString());
                    botMessage.delete(0, botMessage.length());

                    break;
                //TODO Delete '/messages' command. Only for testing, can produce errors 'cause of too long message.
                case "/messages":
                    log.debug("/messages triggered. Sending messages list.");
                    List<Message> messages = jpaMessagesRepository.findAll();

                    for (Message m : messages) {
                        botMessage.append("\nID текущего чата:\n  ").append(m.getChatId())
                                .append("\nСообщение пользователя:\n  ").append(m.getUserMessage())
                                .append("\nОтвет:\n  ").append(m.getBotMessage())
                                .append("\n");
                    }
                    sendMessage(chatId, userMessage, botMessage.toString());
                    botMessage.delete(0, botMessage.length());

                    break;
                case "/domains":
                    log.info("/domains triggered. Sending domains list size.");
                    List<Domain> domains = jpaDomainsRepository.findAll();

                    botMessage.append("\nДоменов собрано:  ").append(domains.size());
                    sendMessage(chatId, userMessage, botMessage.toString());
                    botMessage.delete(0, botMessage.length());

                    break;
                default:
                    log.debug("No one command triggered. Updating user last_message_at .");
                    botMessage.append("\nДата и время последней активности обновлены.\n");

                    sendMessage(chatId, userMessage, botMessage.toString());
                    botMessage.delete(0, botMessage.length());
                    user.setId(dbresponse.get(0).getId());
                    user.setLastMessageAt(update.getMessage().getDate());

                    jpaUsersRepository.saveAndFlush(user);
            }
        }
    }

    public void sendMessage(String chatId, String userMessage, String botMessage) {
        log.info("Sending message. Chat ID: " + chatId);
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(botMessage);
        try {
            execute(message);

            Message messageDB = new Message();
            messageDB.setChatId(chatId);
            messageDB.setUserMessage(userMessage);
            messageDB.setBotMessage(botMessage);
            jpaMessagesRepository.saveAndFlush(messageDB);

            log.info("Message successfully sent and saved to DB.");
        } catch (TelegramApiException e) {
            log.error("sendMessage() in Bot failed! More about error:");
            log.error(e.getMessage(), e);
        }
    }

    private User telegramToUser(org.telegram.telegrambots.meta.api.objects.User user, String chatId) {
        return new User(user.getFirstName(), user.getLastName(), user.getUserName(), chatId);
    }

    @Autowired
    public void jpaMessagesRepository(JpaMessagesRepository jpaMessagesRepository) {
        this.jpaMessagesRepository = jpaMessagesRepository;
    }

    @Autowired
    public void jpaUsersRepository(JpaUsersRepository jpaUsersRepository) {
        this.jpaUsersRepository = jpaUsersRepository;
    }

    @Autowired
    public void jpaDomainsRepository(JpaDomainsRepository jpaDomainsRepository) {
        this.jpaDomainsRepository = jpaDomainsRepository;
    }
}
