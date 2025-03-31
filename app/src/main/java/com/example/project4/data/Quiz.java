package com.example.project4.data;

import java.util.List;

public class Quiz {
    private List<Question> questions;
    private int currentScore;
    private int currentQuestionIndex;

    public Quiz(List<Question> questions) {
        this.questions = questions;
        this.currentScore = 0;
        this.currentQuestionIndex = 0;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void incrementScore() {
        currentScore++;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void moveToNextQuestion() {
        currentQuestionIndex++;
    }

    public boolean isFinished() {
        return currentQuestionIndex >= questions.size();
    }
}
