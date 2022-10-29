package com.kob.MatchingSystem.Servicel.Impl.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class MatchingPool extends Thread{

    private static List<Player> players = new ArrayList<>();

    private final ReentrantLock lock = new ReentrantLock();

    private static RestTemplate restTemplate;

    private static final String startGameUrl = "http://127.0.0.1:3000/pk/start/game/";

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        MatchingPool.restTemplate = restTemplate;
    }

    public void addPlayer(Integer userId, Integer rating){
        lock.lock();
        try {
            players.add(new Player(userId, rating, 0));
        } finally {
            lock.unlock();
        }
    }

    public void removePlayer(Integer userId){
        lock.lock();
        try {
            List<Player> newPlayers = new ArrayList<>();
            for (Player player: players){
                if (player.getUserId().equals(userId)){
                    newPlayers.add(player);
                }
            }
            MatchingPool.players = newPlayers;
        } finally {
            lock.unlock();
        }
    }

    private void increaseWaitingTime(){
        for (Player player: players){
            player.setWaitingTime(player.getWaitingTime() + 1);
        }
    }

    private boolean checkMatch(Player a, Player b){
        int delta = Math.abs(a.getRating() - b.getRating());
        int waitingTime = Math.min(a.getWaitingTime(), b.getWaitingTime());
        return delta <= waitingTime * 10;
    }

    private void sendResult(Player a, Player b){
        System.out.println("send result: " + a.getUserId() + " " + b.getUserId());
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("aId", a.getUserId().toString());
        data.add("bId", b.getUserId().toString());
        restTemplate.postForObject(startGameUrl, data, String.class);
    }
    private void matchPlayers(){
        boolean[] used = new boolean[players.size()];
        for (int i = 0; i < players.size(); i ++){
            if (used[i]) continue;
            for (int j = i + 1; j < players.size(); j ++){
                if (used[j]) continue;
                Player a = players.get(i);
                Player b = players.get(j);
                if (checkMatch(a, b)){
                    used[i] = used[j] = true;
                    sendResult(a, b);
                    break;
                }
            }

        }
        List<Player> newPlayers = new ArrayList<>();
        for (int i = 0; i < players.size(); i ++){
            if (!used[i]){
                newPlayers.add(players.get(i));
            }
        }

        MatchingPool.players = newPlayers;
    }

    @Override
    public void run(){
        while (true){
            System.out.println("match players: " + players);
            try {
                Thread.sleep(1000);
                lock.lock();
                try {
                    increaseWaitingTime();
                    matchPlayers();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
