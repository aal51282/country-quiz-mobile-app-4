package com.example.project4.data;

import java.io.Serializable;
import java.util.List;

public class Quiz implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Question> questions;
    private int currentScore;
    private int currentQuestionIndex;

    public Quiz(List<Question> questions) {
        this.questions = questions;
        this.currentScore = 0;
        this.currentQuestionIndex = 0;
    } // Quiz constructor

    public List<Question> getQuestions() {
        return questions;
    } // getQuestions

    public int getCurrentScore() {
        return currentScore;
    } // getCurrentScore

    public void incrementScore() {
        currentScore++;
    } // incrementScore

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    } // getCurrentQuestionIndex

    public void moveToNextQuestion() {
        currentQuestionIndex++;
    } // moveToNextQuestion

    public boolean isFinished() {
        return currentQuestionIndex >= questions.size();
    } // isFinished
} // Quiz