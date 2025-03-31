package com.example.project4.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Question {
    private Country country;
    private List<String> answerChoices;  // Contains 3 answers: one correct, two incorrect
    private String correctAnswer;
    private String userAnswer = null;  // To be set when the user selects an answer

    public Question(Country country, String correctAnswer, List<String> incorrectAnswers) {
        this.country = country;
        this.correctAnswer = correctAnswer;
        answerChoices = new ArrayList<>();
        answerChoices.add(correctAnswer);
        answerChoices.addAll(incorrectAnswers);
        Collections.shuffle(answerChoices); // Randomize order
    }

    public Country getCountry() {
        return country;
    }

    public List<String> getAnswerChoices() {
        return answerChoices;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String answer) {
        this.userAnswer = answer;
    }
}