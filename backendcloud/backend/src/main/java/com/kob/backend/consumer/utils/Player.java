package com.kob.backend.consumer.utils;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    private Integer id;
    private Integer botId; // -1 represents human playing
    private String code;
    private Integer sx;
    private Integer sy;
    private List<Integer> steps;

    public String getStepsString(){
        StringBuilder builder = new StringBuilder();
        for (int d: steps){
            builder.append(d);
        }
        return builder.toString();
    }

    // check if snake will increase
    private boolean checkTailIncreasing(int step){
        if (step <= 10){
            return true;
        }
        return step % 3 == 1;
    }

    public List<Cell> getCells(){
        List<Cell> res = new ArrayList<>();

        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};

        int x = sx, y = sy;
        res.add(new Cell(x, y));
        int step = 0;
        for (int d: steps){
            x += dx[d];
            y += dy[d];
            res.add(new Cell(x, y));
            step += 1;
            if (!checkTailIncreasing(step)){
                res.remove(0);
            }
        }

        return res;
    }
}
