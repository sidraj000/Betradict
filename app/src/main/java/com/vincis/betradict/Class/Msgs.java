package com.vincis.betradict.Class;

import java.util.Date;

public class Msgs
{
    public String text;
    public Integer id;
    public Date date;
    public Msgs()
    {

    }

    public Msgs(String text, Integer id, Date date) {
        this.text = text;
        this.id = id;
        this.date = date;
    }
}
