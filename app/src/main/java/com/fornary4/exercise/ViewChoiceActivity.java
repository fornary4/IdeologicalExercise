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

import com.fornary4.exercise.database.UserDBHelper;
import com.fornary4.exercise.entity.Choose;
import com.fornary4.exercise.util.FileUtil;
import com.fornary4.exercise.util.SourceUtil;
import com.fornary4.exercise.util.ToastUtil;

import java.util.List;

public class ViewChoiceActivity extends AppCompatActivity implements View.OnClickListener {

    private static int QUESTION_COUNTS;

    private List<Choose> questionList;
    private int curIndex = 0;

    private TextView tv_title;
    private ImageView iv_back;
    private TextView tv_progress;
    private TextView tv_question;
    private Button btn_A;
    private Button btn_B;
    private Button btn_C;
    private Button btn_D;
    private Button btn_previous;
    private Button btn_next;
    private Drawable drawableCorrect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_choice);
        initData();
        intiView();

    }

    private void initData() {
        questionList = getIntent().getIntExtra("type", 0) == UserDBHelper.TYPE_SINGLE ? FileUtil.getSingleList(this) : FileUtil.getMultipleList(this);
        QUESTION_COUNTS = questionList.size();

        drawableCorrect = SourceUtil.getDrawable(this, R.drawable.icon_correct);


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
        btn_previous = findViewById(R.id.btn_previous);
        btn_next = findViewById(R.id.btn_next);

        tv_title.setText("答案");
        iv_back.setOnClickListener(v -> finish());
        tv_progress.setOnClickListener(v -> showDialog());
        tv_progress.setText(curIndex + 1 + "/" + QUESTION_COUNTS);
        tv_question.setText(questionList.get(curIndex).problem);
        btn_A.setText(questionList.get(curIndex).A);
        btn_B.setText(questionList.get(curIndex).B);
        btn_C.setText(questionList.get(curIndex).C);
        btn_D.setText(questionList.get(curIndex).D);
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
                ToastUtil.show(ViewChoiceActivity.this, "题号范围有误");
            }
            dialog.dismiss();
        });
        dialog.show();
    }

    private void drawAnswer() {
        String correct = questionList.get(curIndex).answer;
        if (correct.contains("A")) {
            btn_A.setBackgroundResource(R.drawable.correct_button);
            btn_A.setCompoundDrawables(null, null, drawableCorrect, null);
        }
        if (correct.contains("B")) {
            btn_B.setBackgroundResource(R.drawable.correct_button);
            btn_B.setCompoundDrawables(null, null, drawableCorrect, null);
        }
        if (correct.contains("C")) {
            btn_C.setBackgroundResource(R.drawable.correct_button);
            btn_C.setCompoundDrawables(null, null, drawableCorrect, null);
        }
        if (correct.contains("D")) {
            btn_D.setBackgroundResource(R.drawable.correct_button);
            btn_D.setCompoundDrawables(null, null, drawableCorrect, null);
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