package com.example.project4;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.project4.data.Quiz;

/**
 * Adapter for the ViewPager2 that displays the quiz questions.
 */
public class QuizPagerAdapter extends FragmentStateAdapter {

    private Quiz quiz;

    /**
     * Constructor for the QuizPagerAdapter.
     * @param fragmentActivity The activity that owns the adapter.
     * @param quiz The quiz to display.
     */
    public QuizPagerAdapter(FragmentActivity fragmentActivity, Quiz quiz) {
        super(fragmentActivity);
        this.quiz = quiz;
    } // QuizPagerAdapter constructor

    /**
     * Creates a new instance of QuestionFragment with the question data.
     * @param position The position of the question in the quiz.
     *
     * @return A new instance of QuestionFragment.
     * */
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Create a new instance of QuestionFragment with the question data
        if (position < quiz.getQuestions().size()) {
            return QuestionFragment.newInstance(quiz.getQuestions().get(position), position);
        } else {
            return new Fragment();
        }
    } // createFragment

    /**
     * Returns the number of questions in the quiz.
     *
     * @return The number of questions in the quiz.
     */
    @Override
    public int getItemCount() {
        return quiz.getQuestions().size();
    } // getItemCount
} // QuizPagerAdapter