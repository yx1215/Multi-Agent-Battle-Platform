package com.kob.BotRunningSystem.service.impl.utils;

import com.kob.BotRunningSystem.utils.BotInterface;
import org.joor.Reflect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
public class Consumer extends Thread{

    private Bot bot;

    private static RestTemplate restTemplate;

    private final static String receiveBotMoveUrl = "http://127.0.0.1:3000/pk/receive/bot/move/";

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        Consumer.restTemplate = restTemplate;
    }

    public void startTimeout(Integer timeout, Bot bot){
        this.bot = bot;
        this.start();

        try {
            this.join(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.interrupt();
        }

    }

    private String addUid(String code, String uid){
        int k = code.indexOf(" implements com.kob.BotRunningSystem.utils.BotInterface");
        return code.substring(0, k) + uid + code.substring(k);
    }

    @Override
    public void run(){

        UUID uuid = UUID.randomUUID();
        String uid = uuid.toString().substring(0, 8);
        String className = "com.kob.BotRunningSystem.utils.Bot" + uid;
        String code = addUid(bot.getBotCode(), uid);

        BotInterface botInterface = Reflect.compile(
                className,
                code
        ).create().get();

        Integer direction = botInterface.nextMove(bot.getInput());
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("userId", bot.getUserId().toString());
        data.add("direction", direction.toString());
        restTemplate.postForObject(receiveBotMoveUrl, data, String.class);
        System.out.println("move-direction: " + bot.getUserId() + " " + direction);

    }
}
