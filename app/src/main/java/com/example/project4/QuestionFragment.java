package com.example.project4;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.project4.data.Question;

import java.util.List;

/**
 * A fragment representing a single question in the quiz.
 */
public class QuestionFragment extends Fragment {

    private static final String ARG_QUESTION = "arg_question";
    private static final String ARG_POSITION = "arg_position";

    private Question question;
    private int position;
    private OnAnswerSelectedListener callback;

    public interface OnAnswerSelectedListener {
        void onAnswerSelected(int questionPosition, String answer);
    } // OnAnswerSelectedListener

    public QuestionFragment() {
        // Required empty public constructor
    } // QuestionFragment constructor

    /**
     * Creates a new instance of the fragment with the given question and position.
     * @param question The question to display.
     * @param position The position of the question in the quiz.
     * @return A new instance of the fragment.
     * */
    public static QuestionFragment newInstance(Question question, int position) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_QUESTION, question);
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    } // newInstance

    /**
     * Called when the fragment is attached to the activity.
     * @param context The context of the activity.
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnAnswerSelectedListener) {
            callback = (OnAnswerSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnAnswerSelectedListener");
        } // if
    } // onAttach

    /**
     * Called when the fragment is detached from the activity.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_question, container, false);

        if (getArguments() != null) {
            question = (Question) getArguments().getSerializable(ARG_QUESTION);
            position = getArguments().getInt(ARG_POSITION);
        } // if

        TextView tvQuestion = view.findViewById(R.id.tvQuestion);
        RadioGroup radioGroup = view.findViewById(R.id.radioGroupAnswers);

        // Set question text
        String questionText = getString(R.string.continent_question, question.getCountry().getName());
        tvQuestion.setText(questionText);

        // Check if this question already has an answer (when restoring state)
        String userAnswer = question.getUserAnswer();

        // Add radio buttons for each answer
        List<String> answers = question.getAnswerChoices();
        for (int i = 0; i < answers.size(); i++) {
            String answerText = answers.get(i);
            RadioButton rb = new RadioButton(getContext());
            rb.setId(View.generateViewId());
            rb.setText(answerText);

            // If restoring a previous answer, check the appropriate button
            if (userAnswer != null && userAnswer.equals(answerText)) {
                rb.setChecked(true);
            } // if

            radioGroup.addView(rb);
        } // for

        // Listener to notify when an answer is selected
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Only proceed if this is a new selection (not just restoring state)
                if (checkedId != -1) {
                    RadioButton selected = group.findViewById(checkedId);
                    if (selected != null && callback != null) {
                        callback.onAnswerSelected(position, selected.getText().toString());
                    } // if
                } // if
            } // onCheckedChanged
        });

        return view;
    } // onCreateView
} // QuestionFragment