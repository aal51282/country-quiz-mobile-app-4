package com.example.project4;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.project4.data.Quiz;

public class QuizPagerAdapter extends FragmentStateAdapter {

    private Quiz quiz;

    public QuizPagerAdapter(FragmentActivity fragmentActivity, Quiz quiz) {
        super(fragmentActivity);
        this.quiz = quiz;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Create a new instance of QuestionFragment with the question data
        return QuestionFragment.newInstance(quiz.getQuestions().get(position), position);
    }

    @Override
    public int getItemCount() {
        return quiz.getQuestions().size();
    }
}