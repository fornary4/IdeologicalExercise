package com.fornary4.exercise;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.fornary4.exercise.adapter.ListExamAdapter;
import com.fornary4.exercise.database.UserDBHelper;
import com.fornary4.exercise.entity.Choose;
import com.fornary4.exercise.entity.ErrorInfo;
import com.fornary4.exercise.entity.Judge;
import com.fornary4.exercise.util.FileUtil;
import com.fornary4.exercise.util.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ExamActivity extends AppCompatActivity {
    private List<Choose> singleList;
    private List<Choose> multipleList;
    private List<Judge> judgeList;
    private List<Integer> extract = new ArrayList<>();
    private List<String> choices = new ArrayList<>();
    private ListView lv_exam;
    private List<String> userChoices;
    private TextView tv_submit;
    private UserDBHelper mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        initData();
        lv_exam = findViewById(R.id.lv_exam);
        ListExamAdapter adapter = new ListExamAdapter(this, extract, choices);
        lv_exam.setAdapter(adapter);
        userChoices = adapter.getChoices();

        tv_submit = findViewById(R.id.tv_submit);
        tv_submit.setOnClickListener(v -> {
            if (!isFinish(userChoices)) {
                ToastUtil.show(this, "还有未完成的题目");
            } else {
                showResult();
                for (String userChoice : userChoices) {
                    Log.d("fornary", userChoice);
                }

            }
        });

    }

    private void showResult() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.create();
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_result, null);
        dialog.setView(dialogView);
        TextView tv_result = dialogView.findViewById(R.id.tv_result);
        tv_result.setText(tv_result.getText().toString() + judgeResult());
        Button btn_confirm = dialogView.findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(v2 -> {
            dialog.dismiss();
            showDetail();
            finish();
        });
        dialog.show();
    }

    private void showDetail() {
        tv_submit.setClickable(false);
        Intent intent = new Intent(this, ResultActivity.class);
        Bundle bundle = new Bundle();
        ArrayList<Integer> arr_extract = new ArrayList<>(extract);
        ArrayList<String> arr_user_choices = new ArrayList<>(userChoices);

        bundle.putIntegerArrayList("extract", arr_extract);
        bundle.putStringArrayList("userChoices", arr_user_choices);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    private int judgeResult() {
        int result = 0;
        for (int i = 0; i < extract.size(); i++) {
            if (i >= 60) {
                if (userChoices.get(i).equals(judgeList.get(extract.get(i)).answer)) {
                    result += 1;
                } else {
                    mDB.insert(new ErrorInfo(UserDBHelper.TYPE_JUDGE, extract.get(i)));
                }
            } else if (i >= 40) {
                if (sortByChar(userChoices.get(i)).equals(multipleList.get(extract.get(i)).answer)) {
                    result += 2;
                } else {
                    mDB.insert(new ErrorInfo(UserDBHelper.TYPE_MULTIPLE, extract.get(i)));
                }
            } else {
                if (userChoices.get(i).equals(singleList.get(extract.get(i)).answer)) {
                    result += 1;
                } else {
                    mDB.insert(new ErrorInfo(UserDBHelper.TYPE_SINGLE, extract.get(i)));
                }
            }
        }
        return result;
    }

    private String sortByChar(String s) {
        char[] c = s.toCharArray();
        Arrays.sort(c);
        return new String(c);
    }

    private boolean isFinish(List<String> userChoices) {
        for (String s : userChoices) {
            if (s.equals("")) {
                return false;
            }
        }
        return true;
    }

    private void initData() {
        singleList = FileUtil.getSingleList(this);
        multipleList = FileUtil.getMultipleList(this);
        judgeList = FileUtil.getJudgeList(this);
        List<Integer> single = new ArrayList<>();
        List<Integer> multiple = new ArrayList<>();
        List<Integer> judge = new ArrayList<>();

        for (int i = 0; i < singleList.size(); i++) {
            single.add(i);
        }
        for (int i = 0; i < multipleList.size(); i++) {
            multiple.add(i);
        }
        for (int i = 0; i < judgeList.size(); i++) {
            judge.add(i);
        }
        Collections.shuffle(single);
        Collections.shuffle(multiple);
        Collections.shuffle(judge);
        extract.addAll(single.subList(0, 40));
        extract.addAll(multiple.subList(0, 20));
        extract.addAll(judge.subList(0, 20));

        for (int i = 0; i < 80; i++) {
            choices.add("");
        }
        mDB = UserDBHelper.getInstance(this);
    }
}