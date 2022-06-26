package com.fornary4.exercise.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.fornary4.exercise.R;
import com.fornary4.exercise.entity.Choose;
import com.fornary4.exercise.entity.Judge;
import com.fornary4.exercise.util.FileUtil;
import com.fornary4.exercise.util.SourceUtil;

import java.util.ArrayList;
import java.util.List;

public class ListResultAdapter extends BaseAdapter {
    private static final String[] options = {"A", "B", "C", "D"};
    private Context context;
    private List<Integer> extract;
    private List<String> choices;
    private List<Choose> singleList;
    private List<Choose> multipleList;
    private List<Judge> judgeList;
    private Drawable drawableCorrect;
    private Drawable drawableWrong;
    private Drawable drawableCorrectMiss;


    public ListResultAdapter(Context context, List<Integer> extract, List<String> choices) {
        this.context = context;
        this.extract = extract;
        this.choices = choices;
        singleList = FileUtil.getSingleList(context);
        multipleList = FileUtil.getMultipleList(context);
        judgeList = FileUtil.getJudgeList(context);
        drawableCorrect = SourceUtil.getDrawable(context, R.drawable.icon_correct);
        drawableWrong = SourceUtil.getDrawable(context, R.drawable.icon_wrong);
        drawableCorrectMiss = SourceUtil.getDrawable(context, R.drawable.icon_correct_miss);
    }

    @Override
    public int getCount() {
        return extract.size();
    }

    @Override
    public Object getItem(int position) {
        return extract.get(position);
    }

    @Override
    public long getItemId(int position) {
        return extract.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        String userChoice = choices.get(position);
        if (position < 60) {
            String correct;
            view = LayoutInflater.from(context).inflate(R.layout.item_choose, null);
            TextView tv_question = view.findViewById(R.id.tv_question);
            TextView option_A = view.findViewById(R.id.option_A);
            TextView option_B = view.findViewById(R.id.option_B);
            TextView option_C = view.findViewById(R.id.option_C);
            TextView option_D = view.findViewById(R.id.option_D);
            List<TextView> textViewList = new ArrayList<>();
            textViewList.add(option_A);
            textViewList.add(option_B);
            textViewList.add(option_C);
            textViewList.add(option_D);
            if (position < 40) {
                tv_question.setText(String.format("%d.%s", position + 1, singleList.get(extract.get(position)).problem));
                option_A.setText(singleList.get(extract.get(position)).A);
                option_B.setText(singleList.get(extract.get(position)).B);
                option_C.setText(singleList.get(extract.get(position)).C);
                option_D.setText(singleList.get(extract.get(position)).D);
                correct = singleList.get(extract.get(position)).answer;
            } else {
                tv_question.setText(String.format("%d.%s", position + 1, multipleList.get(extract.get(position)).problem));
                option_A.setText(multipleList.get(extract.get(position)).A);
                option_B.setText(multipleList.get(extract.get(position)).B);
                option_C.setText(multipleList.get(extract.get(position)).C);
                option_D.setText(multipleList.get(extract.get(position)).D);
                correct = multipleList.get(extract.get(position)).answer;
            }
            for (int i = 0; i < options.length; i++) {
                if (correct.contains(options[i]) && userChoice.contains(options[i])) {
                    textViewList.get(i).setBackground(ContextCompat.getDrawable(context, R.drawable.correct_button));
                    textViewList.get(i).setCompoundDrawables(null, null, drawableCorrect, null);
                } else if (correct.contains(options[i]) && !userChoice.contains(options[i])) {
                    textViewList.get(i).setBackground(ContextCompat.getDrawable(context, R.drawable.correct_miss_button));
                    textViewList.get(i).setCompoundDrawables(null, null, drawableCorrectMiss, null);
                } else if (!correct.contains(options[i]) && userChoice.contains(options[i])) {
                    textViewList.get(i).setBackground(ContextCompat.getDrawable(context, R.drawable.wrong_button));
                    textViewList.get(i).setCompoundDrawables(null, null, drawableWrong, null);
                }


            }


        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_judge, null);
            TextView tv_question = view.findViewById(R.id.tv_question);
            TextView option_true = view.findViewById(R.id.option_true);
            TextView option_false = view.findViewById(R.id.option_false);
            tv_question.setText(String.format("%d.%s", position + 1, judgeList.get(extract.get(position)).problem));
            String correct = judgeList.get(extract.get(position)).answer;

            if (correct.equals("对")) {
                option_true.setBackground(ContextCompat.getDrawable(context, R.drawable.correct_button));
                option_true.setCompoundDrawables(null, null, drawableCorrect, null);
            } else {
                option_false.setBackground(ContextCompat.getDrawable(context, R.drawable.correct_button));
                option_false.setCompoundDrawables(null, null, drawableCorrect, null);
            }

            if (!correct.equals(userChoice)) {
                if (userChoice.equals("对")) {
                    option_true.setBackground(ContextCompat.getDrawable(context, R.drawable.wrong_button));
                    option_true.setCompoundDrawables(null, null, drawableWrong, null);
                } else {
                    option_false.setBackground(ContextCompat.getDrawable(context, R.drawable.wrong_button));
                    option_false.setCompoundDrawables(null, null, drawableWrong, null);
                }
            }

        }
        return view;
    }




}
