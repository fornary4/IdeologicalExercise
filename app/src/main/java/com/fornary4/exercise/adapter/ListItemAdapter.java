package com.fornary4.exercise.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.fornary4.exercise.R;
import com.fornary4.exercise.database.UserDBHelper;
import com.fornary4.exercise.entity.Choose;
import com.fornary4.exercise.entity.ErrorInfo;
import com.fornary4.exercise.entity.Judge;
import com.fornary4.exercise.util.FileUtil;
import com.fornary4.exercise.util.SourceUtil;

import java.util.List;

public class ListItemAdapter extends BaseAdapter {
    private Context context;
    private List<ErrorInfo> list;
    private List<Choose> singleList;
    private List<Choose> multipleList;
    private List<Judge> judgeList;


    public ListItemAdapter(Context context, List<ErrorInfo> list) {
        this.context = context;
        this.list = list;
        singleList = FileUtil.getSingleList(context);
        multipleList = FileUtil.getMultipleList(context);
        judgeList = FileUtil.getJudgeList(context);
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
        return list.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        String correct;
        if (list.get(position).type == UserDBHelper.TYPE_SINGLE || list.get(position).type == UserDBHelper.TYPE_MULTIPLE) {
            view = LayoutInflater.from(context).inflate(R.layout.item_choose, null);
            TextView tv_question = view.findViewById(R.id.tv_question);
            TextView option_A = view.findViewById(R.id.option_A);
            TextView option_B = view.findViewById(R.id.option_B);
            TextView option_C = view.findViewById(R.id.option_C);
            TextView option_D = view.findViewById(R.id.option_D);
            if (list.get(position).type == UserDBHelper.TYPE_SINGLE) {
                tv_question.setText(String.format("%d.%s", position + 1, singleList.get(list.get(position).position).problem));
                option_A.setText(singleList.get(list.get(position).position).A);
                option_B.setText(singleList.get(list.get(position).position).B);
                option_C.setText(singleList.get(list.get(position).position).C);
                option_D.setText(singleList.get(list.get(position).position).D);
                correct = singleList.get(list.get(position).position).answer;
            } else {
                tv_question.setText(String.format("%d.%s", position + 1, multipleList.get(list.get(position).position).problem));
                option_A.setText(multipleList.get(list.get(position).position).A);
                option_B.setText(multipleList.get(list.get(position).position).B);
                option_C.setText(multipleList.get(list.get(position).position).C);
                option_D.setText(multipleList.get(list.get(position).position).D);
                correct = multipleList.get(list.get(position).position).answer;
            }
            if (correct.contains("A")) {
                option_A.setBackground(ContextCompat.getDrawable(context, R.drawable.correct_button));
                option_A.setCompoundDrawables(null, null, SourceUtil.getDrawable(context, R.drawable.icon_correct), null);
            }
            if (correct.contains("B")) {
                option_B.setBackground(ContextCompat.getDrawable(context, R.drawable.correct_button));
                option_B.setCompoundDrawables(null, null, SourceUtil.getDrawable(context, R.drawable.icon_correct), null);
            }
            if (correct.contains("C")) {
                option_C.setBackground(ContextCompat.getDrawable(context, R.drawable.correct_button));
                option_C.setCompoundDrawables(null, null, SourceUtil.getDrawable(context, R.drawable.icon_correct), null);
            }
            if (correct.contains("D")) {
                option_D.setBackground(ContextCompat.getDrawable(context, R.drawable.correct_button));
                option_D.setCompoundDrawables(null, null, SourceUtil.getDrawable(context, R.drawable.icon_correct), null);
            }

        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_judge, null);
            TextView tv_question = view.findViewById(R.id.tv_question);
            TextView option_true = view.findViewById(R.id.option_true);
            TextView option_false = view.findViewById(R.id.option_false);
            tv_question.setText(String.format("%d.%s", position + 1, judgeList.get(list.get(position).position).problem));
            if (judgeList.get(list.get(position).position).answer.equals("对")) {
                option_true.setBackground(ContextCompat.getDrawable(context, R.drawable.correct_button));
                option_true.setCompoundDrawables(null, null, SourceUtil.getDrawable(context, R.drawable.icon_correct), null);
            } else {
                option_false.setBackground(ContextCompat.getDrawable(context, R.drawable.correct_button));
                option_false.setCompoundDrawables(null, null, SourceUtil.getDrawable(context, R.drawable.icon_correct), null);

            }
        }
        return view;
    }



}
