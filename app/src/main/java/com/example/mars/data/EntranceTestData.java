package com.example.mars.data;

import java.util.ArrayList;

public class EntranceTestData {

    String question;
    String answer;


    String userSubmitAnswer;

    ArrayList<String> optionList;
    public EntranceTestData(String question, String answer, String userSubmitAnswer, ArrayList<String> optionList) {
        this.question = question;
        this.answer = answer;
        this.userSubmitAnswer = userSubmitAnswer;
        this.optionList=optionList;
    }
    public ArrayList<String> getOptionList() {
        return optionList;
    }
    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getUserSubmitAnswer() {
        return userSubmitAnswer;
    }

    public void setUserSubmitAnswer(String userSubmitAnswer) {
        this.userSubmitAnswer = userSubmitAnswer;
    }
}
