package com.reed.telegram;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.reed.telegram.bot.Bot;
import com.reed.telegram.entity.Domain;
import com.reed.telegram.entity.User;
import com.reed.telegram.repository.JpaDomainsRepository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.reed.telegram.repository.JpaUsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Component
@Slf4j
@EnableScheduling
public class Scheduler {

    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    private final String DATE = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    private final String DOMAINS_URL = "https://backorder.ru/json/?order=desc&expired=1&by=hotness&page=1&items=50";

    private JpaUsersRepository jpaUsersRepository;
    private JpaDomainsRepository jpaDomainsRepository;
    private Bot bot;

    @Scheduled(cron = "0 */5 * * * ?")
    public void requestAndSaveDomains() {
        log.info("Scheduled task triggered. Building request and downloading domains list...");

        HttpGet request = new HttpGet(DOMAINS_URL);
        request.addHeader("custom-key", "mkyong");
        request.addHeader(HttpHeaders.USER_AGENT, "TelegramBot");
        String jsonLine = "";

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            log.info(response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();
            Header headers = entity.getContentType();
            log.info(headers.toString());

            jsonLine = EntityUtils.toString(entity);
            log.info("Successfully downloaded JSON with domains. Converting into Java object list...");
        } catch (Exception e) {
            log.error("Fail while downloading domains JSON! More about error:");
            log.error(e.getMessage(), e);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        List<Domain> domains;

        try {
            domains = objectMapper.readValue(jsonLine, new TypeReference<List<Domain>>(){});
            jpaDomainsRepository.deleteAll();
            log.info("Successfully converted domains JSON into list and erased old data. Domains amount: " + domains.size());
        } catch (JsonProcessingException e) {
            log.error("Fail while converting domains JSON into list! More about error:");
            log.error(e.getMessage(), e);
            log.warn("Continuing with old domains list.");
            domains = jpaDomainsRepository.findAll();
        }

        for (Domain domain : domains) {
            jpaDomainsRepository.save(domain);
        }

        log.info("Saved domains into DB. Building message for users...");

        String botMessage = String.format("%s. Собрано %d доменов.", DATE, domains.size());
        List<User> users = jpaUsersRepository.findAll();

        for (User user : users) {
            bot.sendMessage(user.getChatId(), "", botMessage);
        }

        log.info("All messages sent.");
    }

    @Autowired
    public void jpaUsersRepository(JpaUsersRepository jpaUsersRepository) {
        this.jpaUsersRepository = jpaUsersRepository;
    }

    @Autowired
    public void jpaDomainsRepository(JpaDomainsRepository jpaDomainsRepository) {
        this.jpaDomainsRepository = jpaDomainsRepository;
    }

    @Autowired
    public void bot(Bot bot) {
        this.bot = bot;
    }
}
