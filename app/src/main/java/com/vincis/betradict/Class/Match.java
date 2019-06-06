package com.vincis.betradict.Class;

import java.util.Date;

public class Match {
    public Date date;
    public String id;
    public String teamA;
    public String teamB;
    public Integer status;
    public Match()
    {
    }

    public Match(Date date, String id, String teamA, String teamB, Integer status) {
        this.date = date;
        this.id = id;
        this.teamA = teamA;
        this.teamB = teamB;
        this.status = status;
    }
}
