package com.fornary4.exercise;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fornary4.exercise.database.UserDBHelper;
import com.fornary4.exercise.entity.Choose;
import com.fornary4.exercise.entity.ErrorInfo;
import com.fornary4.exercise.util.FileUtil;
import com.fornary4.exercise.util.SourceUtil;
import com.fornary4.exercise.util.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SingleChoiceActivity extends AppCompatActivity implements View.OnClickListener {
    private static int QUESTION_COUNTS;

    private List<Choose> questionList;
    private boolean[] chosen;
    private int curIndex = 0;
    private String curChoice;

    private TextView tv_title;
    private ImageView iv_back;
    private TextView tv_progress;
    private TextView tv_question;
    private Button btn_A;
    private Button btn_B;
    private Button btn_C;
    private Button btn_D;
    private Button btn_next;
    private Button btn_finish;
    private Drawable drawableCorrect;
    private Drawable drawableWrong;

    private UserDBHelper mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_choice);
        initData();
        intiView();

    }

    private void initData() {
        mDB = UserDBHelper.getInstance(this);
        int type = getIntent().getIntExtra("isDoError", 0);
        if (type == 1) {
            List<ErrorInfo> errorList = mDB.queryByType(UserDBHelper.TYPE_SINGLE);
            List<Choose> singleList = FileUtil.getSingleList(this);
            questionList = new ArrayList<>();
            for (ErrorInfo info : errorList) {
                questionList.add(singleList.get(info.position));
            }
        } else {
            questionList = FileUtil.getSingleList(this);
        }
        QUESTION_COUNTS = questionList.size();

        drawableCorrect = SourceUtil.getDrawable(this, R.drawable.icon_correct);
        drawableWrong = SourceUtil.getDrawable(this, R.drawable.icon_wrong);

        chosen = new boolean[QUESTION_COUNTS];
        Arrays.fill(chosen, false);

    }

    private void intiView() {
        tv_title = findViewById(R.id.tv_title);
        iv_back = findViewById(R.id.iv_back);
        tv_progress = findViewById(R.id.tv_progress);
        tv_question = findViewById(R.id.tv_question);
        btn_A = findViewById(R.id.btn_A);
        btn_B = findViewById(R.id.btn_B);
        btn_C = findViewById(R.id.btn_C);
        btn_D = findViewById(R.id.btn_D);
        btn_next = findViewById(R.id.btn_next);
        btn_finish = findViewById(R.id.btn_finish);

        tv_title.setText("单选题");
        iv_back.setOnClickListener(v -> finish());
        tv_progress.setOnClickListener(v -> showDialog());
        btn_A.setOnClickListener(this);
        btn_B.setOnClickListener(this);
        btn_C.setOnClickListener(this);
        btn_D.setOnClickListener(this);
        tv_progress.setText(curIndex + 1 + "/" + QUESTION_COUNTS);
        tv_question.setText(questionList.get(curIndex).problem);
        btn_A.setText(questionList.get(curIndex).A);
        btn_B.setText(questionList.get(curIndex).B);
        btn_C.setText(questionList.get(curIndex).C);
        btn_D.setText(questionList.get(curIndex).D);
        btn_next.setOnClickListener(this);
        btn_next.setClickable(false);
        btn_finish.setOnClickListener(v -> {
            if (chosen[curIndex])
                finish();
        });
        findViewById(R.id.iv_progress).setOnClickListener(v -> showDialog());
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.create();
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog, null);
        dialog.setView(dialogView);
        EditText et = dialogView.findViewById(R.id.et_position);
        Button btn_cancel = dialogView.findViewById(R.id.btn_cancel);
        Button btn_confirm = dialogView.findViewById(R.id.btn_confirm);
        btn_cancel.setOnClickListener(v1 -> dialog.dismiss());
        btn_confirm.setOnClickListener(v2 -> {
            curIndex = Integer.parseInt(et.getText().toString()) - 1;
            if (curIndex >= 0 && curIndex < QUESTION_COUNTS) {
                refresh();
            } else {
                ToastUtil.show(SingleChoiceActivity.this, "题号范围有误");
            }
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                curIndex++;
                if (curIndex < QUESTION_COUNTS) {
                    btn_next.setClickable(false);
                    refresh();
                }
                if (curIndex == QUESTION_COUNTS - 1) {
                    btn_next.setVisibility(View.GONE);
                    btn_finish.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_A:
                curChoice = "A";
                judgeSelection();
                break;
            case R.id.btn_B:
                curChoice = "B";
                judgeSelection();
                break;
            case R.id.btn_C:
                curChoice = "C";
                judgeSelection();
                break;
            case R.id.btn_D:
                curChoice = "D";
                judgeSelection();
                break;
        }
    }

    private void judgeSelection() {
        btn_next.setClickable(true);
        String correct = questionList.get(curIndex).answer;
        switch (correct) {
            case "A":
                btn_A.setBackgroundResource(R.drawable.correct_button);
                btn_A.setCompoundDrawables(null, null, drawableCorrect, null);
                break;
            case "B":
                btn_B.setBackgroundResource(R.drawable.correct_button);
                btn_B.setCompoundDrawables(null, null, drawableCorrect, null);
                break;
            case "C":
                btn_C.setBackgroundResource(R.drawable.correct_button);
                btn_C.setCompoundDrawables(null, null, drawableCorrect, null);
                break;
            case "D":
                btn_D.setBackgroundResource(R.drawable.correct_button);
                btn_D.setCompoundDrawables(null, null, drawableCorrect, null);
                break;
        }
        if (!correct.equals(curChoice)) {
            switch (curChoice) {
                case "A":
                    btn_A.setBackgroundResource(R.drawable.wrong_button);
                    btn_A.setCompoundDrawables(null, null, drawableWrong, null);
                    break;
                case "B":
                    btn_B.setBackgroundResource(R.drawable.wrong_button);
                    btn_B.setCompoundDrawables(null, null, drawableWrong, null);
                    break;
                case "C":
                    btn_C.setBackgroundResource(R.drawable.wrong_button);
                    btn_C.setCompoundDrawables(null, null, drawableWrong, null);
                    break;
                case "D":
                    btn_D.setBackgroundResource(R.drawable.wrong_button);
                    btn_D.setCompoundDrawables(null, null, drawableWrong, null);
                    break;
            }
        }
        if (!chosen[curIndex] && !correct.equals(curChoice)) {
            ErrorInfo info = new ErrorInfo(UserDBHelper.TYPE_SINGLE, curIndex);
            mDB.insert(info);
        }
        chosen[curIndex] = true;


    }

    private void refresh() {
        removeComponents();
        tv_progress.setText(curIndex + 1 + "/" + QUESTION_COUNTS);
        tv_question.setText(questionList.get(curIndex).problem);
        btn_A.setText(questionList.get(curIndex).A);
        btn_B.setText(questionList.get(curIndex).B);
        btn_C.setText(questionList.get(curIndex).C);
        btn_D.setText(questionList.get(curIndex).D);
    }

    private void removeComponents() {
        btn_A.setBackgroundResource(R.drawable.normal_button);
        btn_B.setBackgroundResource(R.drawable.normal_button);
        btn_C.setBackgroundResource(R.drawable.normal_button);
        btn_D.setBackgroundResource(R.drawable.normal_button);

        btn_A.setCompoundDrawables(null, null, null, null);
        btn_B.setCompoundDrawables(null, null, null, null);
        btn_C.setCompoundDrawables(null, null, null, null);
        btn_D.setCompoundDrawables(null, null, null, null);
    }


}