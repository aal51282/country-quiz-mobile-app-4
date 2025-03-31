package com.example.project4;

import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class QuizPagerAdapter extends FragmentPagerAdapter {

    private Quiz quiz;

    public QuizPagerAdapter(FragmentManager fm, Quiz quiz) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.quiz = quiz;
    }

    @Override
    public Fragment getItem(int position) {
        // Create a new instance of QuestionFragment with the question data.
        return QuestionFragment.newInstance(quiz.getQuestions().get(position), position);
    }

    @Override
    public int getCount() {
        return quiz.getQuestions().size();
    }
}