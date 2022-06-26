package com.fornary4.exercise.adapter;

import android.content.Context;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;

public class ListExamAdapter extends BaseAdapter {
    private static final String[] options = {"A", "B", "C", "D"};

    Context context;
    List<Integer> list;
    private List<Choose> singleList;
    private List<Choose> multipleList;
    private List<Judge> judgeList;
    private List<TextView> textViewList;
    public List<String> choices;

    public ListExamAdapter(Context context, List<Integer> list, List<String> choices) {
        this.context = context;
        this.list = list;
        this.choices = choices;
        singleList = FileUtil.getSingleList(context);
        multipleList = FileUtil.getMultipleList(context);
        judgeList = FileUtil.getJudgeList(context);
        textViewList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (position >= 0 && position < 60) {
            view = LayoutInflater.from(context).inflate(R.layout.item_choose, null);
            TextView tv_question = view.findViewById(R.id.tv_question);
            TextView option_A = view.findViewById(R.id.option_A);
            TextView option_B = view.findViewById(R.id.option_B);
            TextView option_C = view.findViewById(R.id.option_C);
            TextView option_D = view.findViewById(R.id.option_D);
            textViewList.clear();
            textViewList.add(option_A);
            textViewList.add(option_B);
            textViewList.add(option_C);
            textViewList.add(option_D);
            if (position < 40) {
                tv_question.setText(String.format("%d.%s", position + 1, singleList.get(list.get(position)).problem));
                option_A.setText(singleList.get(list.get(position)).A);
                option_B.setText(singleList.get(list.get(position)).B);
                option_C.setText(singleList.get(list.get(position)).C);
                option_D.setText(singleList.get(list.get(position)).D);
                for (int i = 0; i < options.length; i++) {
                    if (choices.get(position).equals(options[i])) {
                        textViewList.get(i).setBackground(ContextCompat.getDrawable(context, R.drawable.chosen_button));
                    }
                }
                for (int i = 0; i < options.length; i++) {
                    TextView tv = textViewList.get(i);
                    int copyI = i;
                    tv.setOnClickListener(v -> {
                        option_A.setBackground(ContextCompat.getDrawable(context, R.drawable.tv_transparent));
                        option_B.setBackground(ContextCompat.getDrawable(context, R.drawable.tv_transparent));
                        option_C.setBackground(ContextCompat.getDrawable(context, R.drawable.tv_transparent));
                        option_D.setBackground(ContextCompat.getDrawable(context, R.drawable.tv_transparent));
                        tv.setBackground(ContextCompat.getDrawable(context, R.drawable.chosen_button));
                        choices.set(position, options[copyI]);
                    });
                }
            } else {
                tv_question.setText(String.format("%d.%s", position + 1, multipleList.get(list.get(position)).problem));
                option_A.setText(multipleList.get(list.get(position)).A);
                option_B.setText(multipleList.get(list.get(position)).B);
                option_C.setText(multipleList.get(list.get(position)).C);
                option_D.setText(multipleList.get(list.get(position)).D);

                for (int i = 0; i < options.length; i++) {
                    if (choices.get(position).contains(options[i])) {
                        textViewList.get(i).setBackground(ContextCompat.getDrawable(context, R.drawable.chosen_button));
                    }
                }

                for (int i = 0; i < options.length; i++) {
                    TextView tv = textViewList.get(i);
                    int copyI = i;
                    tv.setOnClickListener(v -> {
                        if (!choices.get(position).contains(options[copyI])) {
                            choices.set(position, choices.get(position) + options[copyI]);
                            tv.setBackground(ContextCompat.getDrawable(context, R.drawable.chosen_button));
                        } else {
                            choices.set(position, deleteChar(choices.get(position), options[copyI]));
                            tv.setBackground(ContextCompat.getDrawable(context, R.drawable.tv_transparent));
                        }
                    });
                }

            }


        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_judge, null);
            TextView tv_question = view.findViewById(R.id.tv_question);
            TextView option_true = view.findViewById(R.id.option_true);
            TextView option_false = view.findViewById(R.id.option_false);
            tv_question.setText(String.format("%d.%s", position + 1, judgeList.get(list.get(position)).problem));

            if (choices.get(position).equals("对")) {
                option_true.setBackground(ContextCompat.getDrawable(context, R.drawable.chosen_button));
            }
            if (choices.get(position).equals("错")) {
                option_false.setBackground(ContextCompat.getDrawable(context, R.drawable.chosen_button));
            }

            option_true.setOnClickListener(v -> {
                option_false.setBackground(ContextCompat.getDrawable(context, R.drawable.tv_transparent));
                option_true.setBackground(ContextCompat.getDrawable(context, R.drawable.chosen_button));
                choices.set(position, "对");
            });
            option_false.setOnClickListener(v -> {
                option_true.setBackground(ContextCompat.getDrawable(context, R.drawable.tv_transparent));
                option_false.setBackground(ContextCompat.getDrawable(context, R.drawable.chosen_button));
                choices.set(position, "错");
            });
        }
        return view;
    }

    private String deleteChar(String s, String c) {
        char ch = c.charAt(0);
        StringBuilder sb = new StringBuilder();
        for (char it : s.toCharArray()) {
            if (it != ch) {
                sb.append(it);
            }
        }
        return sb.toString();
    }

    public List<String> getChoices() {
        return choices;
    }


}
