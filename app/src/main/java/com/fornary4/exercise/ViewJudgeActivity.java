package com.fornary4.exercise;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.fornary4.exercise.entity.Judge;
import com.fornary4.exercise.util.FileUtil;
import com.fornary4.exercise.util.SourceUtil;
import com.fornary4.exercise.util.ToastUtil;

import java.util.List;

public class ViewJudgeActivity extends AppCompatActivity implements View.OnClickListener {

    private static int QUESTION_COUNTS;

    private List<Judge> questionList;
    private int curIndex = 0;

    private TextView tv_title;
    private ImageView iv_back;
    private TextView tv_progress;
    private TextView tv_question;
    private Button btn_true;
    private Button btn_false;

    private Button btn_previous;
    private Button btn_next;
    private Drawable drawableCorrect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_judge);
        initData();
        intiView();

    }

    private void initData() {
        questionList = FileUtil.getJudgeList(this);
        QUESTION_COUNTS = questionList.size();

        drawableCorrect = SourceUtil.getDrawable(this, R.drawable.icon_correct);


    }

    private void intiView() {
        tv_title = findViewById(R.id.tv_title);
        iv_back = findViewById(R.id.iv_back);
        tv_progress = findViewById(R.id.tv_progress);
        tv_question = findViewById(R.id.tv_question);
        btn_true = findViewById(R.id.btn_true);
        btn_false = findViewById(R.id.btn_false);
        btn_previous = findViewById(R.id.btn_previous);
        btn_next = findViewById(R.id.btn_next);

        tv_title.setText("答案");
        iv_back.setOnClickListener(v -> finish());
        tv_progress.setOnClickListener(v -> showDialog());

        tv_progress.setText(curIndex + 1 + "/" + QUESTION_COUNTS);
        tv_question.setText(questionList.get(curIndex).problem);
        btn_previous.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        drawAnswer();
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
                drawAnswer();
            } else {
                ToastUtil.show(ViewJudgeActivity.this, "题号范围有误");
            }
            dialog.dismiss();
        });
        dialog.show();
    }

    private void drawAnswer() {
        String correct = questionList.get(curIndex).answer;
        if (correct.equals("对")) {
            btn_true.setBackgroundResource(R.drawable.correct_button);
            btn_true.setCompoundDrawables(null, null, drawableCorrect, null);
        } else {
            btn_false.setBackgroundResource(R.drawable.correct_button);
            btn_false.setCompoundDrawables(null, null, drawableCorrect, null);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                if (curIndex < QUESTION_COUNTS) {
                    curIndex++;
                    refresh();
                    drawAnswer();
                }

                break;
            case R.id.btn_previous:
                if (curIndex > 0) {
                    curIndex--;
                    refresh();
                    drawAnswer();
                }
                break;
        }
    }


    private void refresh() {
        removeComponents();
        tv_progress.setText(curIndex + 1 + "/" + QUESTION_COUNTS);
        tv_question.setText(questionList.get(curIndex).problem);


    }

    private void removeComponents() {
        btn_true.setBackgroundResource(R.drawable.normal_button);
        btn_false.setBackgroundResource(R.drawable.normal_button);


        btn_true.setCompoundDrawables(null, null, null, null);
        btn_false.setCompoundDrawables(null, null, null, null);

    }
}