package com.kob.backend.consumer.utils;


import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.Record;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread{

    private final Integer rows, cols;
    private final  Integer inner_walls_count;
    private final int[][] g;

    final private static int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};

    private final Player playerA, playerB;

    private Integer nextStepA = null;
    private Integer nextStepB = null;

    private final ReentrantLock lock = new ReentrantLock();
    private String status = "playing"; // playing -> finished
    private String loser = null; // A, B, all

    private static final String addBotUrl = "http://127.0.0.1:3002/bot/add/";
    public Game(Integer rows,
                Integer cols,
                Integer inner_walls_count,
                Integer idA,
                Integer idB,
                Bot botA,
                Bot botB){
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_count = inner_walls_count;
        this.g = new int[rows][cols];

        Integer botIdA = -1, botIdB = -1;

        String codeA = "", codeB = "";

        if (botA != null){
            botIdA = botA.getId();
            codeA = botA.getContent();

        }


        if (botB != null){
            botIdB = botB.getId();
            codeB = botB.getContent();
        }

        this.playerA = new Player(idA, botIdA, codeA, this.rows - 2, 1, new ArrayList<>());
        this.playerB = new Player(idB, botIdB, codeB, 1, this.cols - 2, new ArrayList<>());
    }

    public Player getPlayerA() {
        return playerA;
    }

    public Player getPlayerB() {
        return playerB;
    }

    public void setNextStepA(Integer nextStepA){
        lock.lock();
        try {
            if (this.nextStepA == null){
                this.nextStepA = nextStepA;
            }
        } finally {
            lock.unlock();
        }

    }

    public void setNextStepB(Integer nextStepB) {
        lock.lock();
        try {
            if (this.nextStepB == null){
                this.nextStepB = nextStepB;
            }
        } finally {
            lock.unlock();
        }
    }

    public int[][] getG() {
        return g;
    }

    public boolean checkConnectivity(int sx, int sy, int tx, int ty){

        if (sx == tx && sy == ty){
            return true;
        }

        g[sx][sy] = 1;
        for (int i = 0; i < 4; i ++){
            int x = sx + dx[i], y = sy + dy[i];
            if (x >= 0 && x < this.rows && y >= 0 && y < this.cols && g[x][y] == 0){
                if (checkConnectivity(x, y, tx, ty)){
                    g[sx][sy] = 0;
                    return true;
                }
            }
        }
        g[sx][sy] = 0;
        return false;
    }

    public boolean draw() {
        for (int i = 0; i < this.rows; i ++){
            for (int j = 0; j < this.cols; j ++){
                g[i][j] = 0;
            }
        }

        for (int r = 0; r < this.rows; r ++) {
            g[r][0] = g[r][this.cols - 1] = 1;
        }

        for (int c = 0; c < this.cols; c ++) {
            g[0][c] = g[this.rows - 1][c] = 1;
        }

        Random random = new Random();
        for (int i = 0; i < this.inner_walls_count / 2; i ++){
            for (int j = 0; j < 1000; j ++){
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);
                if (g[r][c] == 1 || g[this.rows - 1 - r][this.cols - 1 - c] == 1){
                    continue;
                }
                if ((r == this.rows - 2 && c == 1) || (c == this.cols - 2 && r == 1)){
                    continue;
                }

                g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = 1;
                break;
            }
        }

        return checkConnectivity(this.rows - 2, 1, 1, this.cols - 2);
    }

    public void createMap(){
        for (int i = 0; i < 1000; i ++){
            if (draw()){
                break;
            }
        }
    }

    // encode board into string
    private String getInput(Player player){
        // ----map----#sx#sy#mymove#osx#osy#omove
        Player me, you;
        if (player.getId().equals(playerA.getId())) {
            me = playerA;
            you = playerB;
        } else {
            you = playerA;
            me = playerB;
        }

        return getMapString() +
                "#" + me.getSx() +
                "#" + me.getSy() +
                "#" + "(" + me.getStepsString() + ")" +
                "#" + you.getSx() +
                "#" + you.getSy() +
                "#" + "(" + you.getStepsString() + ")";
    }

    private void sendBotCode(Player player){
        if (player.getBotId().equals(-1)){
            return;
        }

        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();

        data.add("userId", player.getId().toString());
        data.add("botCode", player.getCode());
        data.add("input", getInput(player));

        WebSocketServer.restTemplate.postForObject(addBotUrl, data, String.class);

    }
    public boolean nextStep(){
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sendBotCode(playerA);
        sendBotCode(playerB);

        try{
            for (int i = 0; i < 100; i ++){
                Thread.sleep(100);
                lock.lock();
                try {
                    if (nextStepA != null && nextStepB != null){
                        playerA.getSteps().add(nextStepA);
                        playerB.getSteps().add(nextStepB);
                        return true;
                    }
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e){
            e.printStackTrace();
        }

        return false;

    }


    private void sendAllMessage(String message){
        System.out.println("sending to all: " + message);
        if (WebSocketServer.users.get(playerA.getId()) != null)
            WebSocketServer.users.get(playerA.getId()).sendMessage(message);
        if (WebSocketServer.users.get(playerB.getId()) != null)
            WebSocketServer.users.get(playerB.getId()).sendMessage(message);
    }

    // send game result to clients
    public void sendResult(){
        JSONObject resp = new JSONObject();
        resp.put("event", "result");
        resp.put("loser", loser);
        saveToDatabase();
        sendAllMessage(resp.toJSONString());
    }



    private boolean checkValid(List<Cell> cellsA, List<Cell> cellsB){
        int n = cellsA.size();
        Cell cell = cellsA.get(n - 1);
        if (g[cell.x][cell.y] == 1) return false;

        for (int i = 0; i < n - 1; i ++){
            if (cellsA.get(i).x == cell.x && cellsA.get(i).y == cell.y){
                return false;
            }
        }

        for (int i = 0; i < n; i ++){
            if (cellsB.get(i).x == cell.x && cellsB.get(i).y == cell.y){
                return false;
            }
        }

        return true;
    }
    // judge the next step
    private void judge() {
        List<Cell> cellsA = playerA.getCells();
        List<Cell> cellsB = playerB.getCells();

        boolean validA = checkValid(cellsA, cellsB);
        boolean validB = checkValid(cellsB, cellsA);

        if (!validA || !validB){
            status = "finished";
            if (!validA && !validB){
                loser = "all";
            } else if (!validA) {
                loser = "A";
            } else {
                loser = "B";
            }
        }
    }

    // send next steps to clients
    private void sendMove() {
        lock.lock();
        try{
            JSONObject resp = new JSONObject();
            resp.put("event", "move");
            resp.put("a_direction", nextStepA);
            resp.put("b_direction", nextStepB);
            nextStepA = nextStepB = null;
            sendAllMessage(resp.toJSONString());
        } finally {
            lock.unlock();
        }
    }

    private void saveToDatabase(){
        Record record = new Record(
                null,
                playerA.getId(),
                playerA.getSx(),
                playerA.getSy(),
                playerB.getId(),
                playerB.getSx(),
                playerB.getSy(),
                playerA.getStepsString(),
                playerB.getStepsString(),
                getMapString(),
                loser,
                new Date()
        );

        WebSocketServer.recordMapper.insert(record);
    }

    private String getMapString(){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.rows; i ++){
            for (int j = 0; j < this.cols; j ++){
                builder.append(g[i][j]);
            }
        }
        return builder.toString();
    }

    @Override
    public void run(){
        // we need to sleep for 1s before start the game
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < 1000; i++){
            // if get next step
            if (nextStep()){
                judge();
                if (this.status.equals("playing")){
                    sendMove();
                } else {
                    sendResult();
                    break;
                }
            } else {
                this.status = "finished";
                lock.lock();
                try{
                    if (nextStepA == null && nextStepB == null){
                        this.loser = "all";
                    } else if (nextStepA == null){
                        this.loser = "A";
                    } else {
                        this.loser = "B";
                    }
                } finally {
                    lock.unlock();
                }
                sendResult();
                break;

            }
        }
    }
}
