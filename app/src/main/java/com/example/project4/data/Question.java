package com.example.project4.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Question implements Serializable {
    private static final long serialVersionUID = 1L;
    private Country country;
    private List<String> answerChoices;
    private String correctAnswer;
    private String userAnswer = null;

    public Question(Country country, String correctAnswer, List<String> incorrectAnswers) {
        this.country = country;
        this.correctAnswer = correctAnswer;
        answerChoices = new ArrayList<>();
        answerChoices.add(correctAnswer);
        answerChoices.addAll(incorrectAnswers);
        Collections.shuffle(answerChoices);
    }

    // Getters and setters
    public Country getCountry() { return country; }
    public List<String> getAnswerChoices() { return answerChoices; }
    public String getCorrectAnswer() { return correctAnswer; }
    public String getUserAnswer() { return userAnswer; }
    public void setUserAnswer(String answer) { this.userAnswer = answer; }
}