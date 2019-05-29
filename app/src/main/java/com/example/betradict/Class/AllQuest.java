package com.example.betradict.Class;

public class AllQuest {
    public String ques;
    public String opt1;
    public String opt2;
    public String opt3;
    public String qid;
    public Quest_wall quest_wall;
    public AllQuest()
    {  }

    public AllQuest(String ques, String opt1, String opt2, String opt3, String qid, Quest_wall quest_wall) {
        this.ques = ques;
        this.opt1 = opt1;
        this.opt2 = opt2;
        this.opt3 = opt3;
        this.qid = qid;
        this.quest_wall = quest_wall;
    }
}
