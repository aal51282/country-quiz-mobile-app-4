package com.example.project4.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a question in the quiz.
 */
public class Question implements Serializable {
    private static final long serialVersionUID = 1L;
    private Country country;
    private List<String> answerChoices;
    private String correctAnswer;
    private String userAnswer = null;

    /**
     * Constructs a Question object with the given country, correct answer, and incorrect answers.
     * @param country The country associated with the question.
     * @param correctAnswer The correct answer for the question.
     * @param incorrectAnswers A list of incorrect answers for the question.
     */
    public Question(Country country, String correctAnswer, List<String> incorrectAnswers) {
        this.country = country;
        this.correctAnswer = correctAnswer;
        answerChoices = new ArrayList<>();
        answerChoices.add(correctAnswer);
        answerChoices.addAll(incorrectAnswers);
        Collections.shuffle(answerChoices);
    }  // Question constructor

    // Getters and setters

    /**
     * Returns the country associated with the question.
     * @return The country of the question.
     */
    public Country getCountry() {
        return country;
    } // getCountry

    /**
     * Returns the list of answer choices for the question.
     * @return The list of answer choices.
     */
    public List<String> getAnswerChoices() {
        return answerChoices;
    } // getAnswerChoices

    /**
     * Returns the correct answer for the question.
     * @return The correct answer.
     */
    public String getCorrectAnswer() {
        return correctAnswer;
    } // getCorrectAnswer

    /**
     * Returns the user's answer for the question.
     * @return The user's answer.
     */
    public String getUserAnswer() {
        return userAnswer;
    } // getUserAnswer

    /**
     * Sets the user's answer for the question.
     * @param answer The user's answer.
     */
    public void setUserAnswer(String answer) {
        this.userAnswer = answer;
    } // setUserAnswer
} // Question