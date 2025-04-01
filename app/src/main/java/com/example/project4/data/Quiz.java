package com.example.project4.data;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a quiz with a list of questions.
 */
public class Quiz implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Question> questions;
    private int currentScore;
    private int currentQuestionIndex;

    /**
     * Constructs a Quiz object with the given list of questions.
     * @param questions The list of questions in the quiz.
     */
    public Quiz(List<Question> questions) {
        this.questions = questions;
        this.currentScore = 0;
        this.currentQuestionIndex = 0;
    } // Quiz constructor

    /**
     * Returns the list of questions in the quiz.
     * @return The list of questions.
     */
    public List<Question> getQuestions() {
        return questions;
    } // getQuestions

    /**
     * Returns the current score of the quiz.
     * @return The current score.
     */
    public int getCurrentScore() {
        return currentScore;
    } // getCurrentScore

    /**
     * Increments the current score by 1.
     */
    public void incrementScore() {
        currentScore++;
    } // incrementScore

    /**
     * Returns the index of the current question in the quiz.
     * @return The current question index.
     */
    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    } // getCurrentQuestionIndex

    /**
     * Moves to the next question in the quiz.
     */
    public void moveToNextQuestion() {
        currentQuestionIndex++;
    } // moveToNextQuestion

    /**
     * Checks if the quiz has reached its end.
     * @return True if the quiz is finished, false otherwise.
     */
    public boolean isFinished() {
        return currentQuestionIndex >= questions.size();
    } // isFinished
} // Quiz