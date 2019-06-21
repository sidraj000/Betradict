package com.vincis.betradict.Class;

public class Quest {
    public String ques;
    public String heading;
    public String opt1;
    public String opt2;
    public String opt3;
    public String qid;
    public int status;
    public float mybid;
    public float myrate;
    public String myans;
    public String cans;
    public Float amtearned;
    public Quest()
    {  }

    public Quest(String ques, String opt1, String opt2, String opt3, String qid, int status, float mybid, float myrate, String myans, String cans) {
        this.ques = ques;
        this.opt1 = opt1;
        this.opt2 = opt2;
        this.opt3 = opt3;
        this.qid = qid;
        this.status = status;
        this.mybid = mybid;
        this.myrate = myrate;
        this.myans = myans;
        this.cans = cans;
    }
    public Quest(String ques, String heading, String opt1, String opt2, String opt3, String qid, int status, float mybid, float myrate, String myans, String cans, Float amtearned) {
        this.ques = ques;
        this.heading = heading;
        this.opt1 = opt1;
        this.opt2 = opt2;
        this.opt3 = opt3;
        this.qid = qid;
        this.status = status;
        this.mybid = mybid;
        this.myrate = myrate;
        this.myans = myans;
        this.cans = cans;
        this.amtearned = amtearned;
    }


}
