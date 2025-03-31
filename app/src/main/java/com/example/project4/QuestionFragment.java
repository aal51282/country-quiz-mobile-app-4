package com.example.project4;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.project4.data.Question;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionFragment extends Fragment {

    private static final String ARG_QUESTION = "arg_question";
    private static final String ARG_POSITION = "arg_position";

    private Question question;
    private int position;
    private OnAnswerSelectedListener callback;

    public interface OnAnswerSelectedListener {
        void onAnswerSelected(int questionPosition, String answer);
    }

    public QuestionFragment() {
        // Required empty public constructor.
    }

    public static QuestionFragment newInstance(Question question, int position) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_QUESTION, question);
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAnswerSelectedListener) {
            callback = (OnAnswerSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnAnswerSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment.
        View view = inflater.inflate(R.layout.fragment_question, container, false);

        if (getArguments() != null) {
            question = (Question) getArguments().getSerializable(ARG_QUESTION);
            position = getArguments().getInt(ARG_POSITION);
        }

        TextView tvQuestion = view.findViewById(R.id.tvQuestion);
        RadioGroup radioGroup = view.findViewById(R.id.radioGroupAnswers);

        // Set question text.
        String questionText = "On which continent is " + question.getCountry().getName() + " located?";
        tvQuestion.setText(questionText);

        // Add radio buttons for each answer.
        List<String> answers = question.getAnswerChoices();
        for (int i = 0; i < answers.size(); i++) {
            RadioButton rb = new RadioButton(getContext());
            rb.setId(View.generateViewId());
            rb.setText(answers.get(i));
            radioGroup.addView(rb);
        }

        // Listener to notify when an answer is selected.
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected = group.findViewById(checkedId);
                if (selected != null && callback != null) {
                    callback.onAnswerSelected(position, selected.getText().toString());
                }
            }
        });

        return view;
    }
}