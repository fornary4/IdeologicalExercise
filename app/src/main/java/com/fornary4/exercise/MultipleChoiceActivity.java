package com.fornary4.exercise;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import com.fornary4.exercise.entity.ErrorInfo;
import com.fornary4.exercise.util.FileUtil;
import com.fornary4.exercise.util.SourceUtil;
import com.fornary4.exercise.util.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultipleChoiceActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String[] options = {"A", "B", "C", "D"};
    private static int QUESTION_COUNTS;

    private List<Choose> questionList;
    private List<Button> buttonList;
    private boolean[] chosen;
    private int curIndex = 0;
    private boolean[] curChoice;

    private TextView tv_title;
    private ImageView iv_back;
    private TextView tv_progress;
    private TextView tv_question;
    private Button btn_A;
    private Button btn_B;
    private Button btn_C;
    private Button btn_D;
    private Button btn_confirm;
    private Button btn_next;
    private Button btn_finish;
    private Drawable drawableCorrect;
    private Drawable drawableWrong;
    private Drawable drawableCorrectMiss;

    private UserDBHelper mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_choice);
        initData();
        intiView();
    }

    private void initData() {
        mDB = UserDBHelper.getInstance(this);

        int type = getIntent().getIntExtra("isDoError", 0);
        if (type == 1) {
            List<ErrorInfo> errorList = mDB.queryByType(UserDBHelper.TYPE_MULTIPLE);
            List<Choose> multipleList = FileUtil.getMultipleList(this);
            questionList = new ArrayList<>();
            for (ErrorInfo info : errorList) {
                questionList.add(multipleList.get(info.position));
            }
        } else {
            questionList = FileUtil.getMultipleList(this);
        }

        QUESTION_COUNTS = questionList.size();

        drawableCorrect = SourceUtil.getDrawable(this, R.drawable.icon_correct);
        drawableWrong = SourceUtil.getDrawable(this, R.drawable.icon_wrong);
        drawableCorrectMiss = SourceUtil.getDrawable(this, R.drawable.icon_correct_miss);

        buttonList = new ArrayList<>();
        chosen = new boolean[QUESTION_COUNTS];
        curChoice = new boolean[4];
        Arrays.fill(chosen, false);
        Arrays.fill(curChoice, false);

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
        btn_confirm = findViewById(R.id.btn_confirm);
        btn_next = findViewById(R.id.btn_next);
        btn_finish = findViewById(R.id.btn_finish);

        buttonList.add(btn_A);
        buttonList.add(btn_B);
        buttonList.add(btn_C);
        buttonList.add(btn_D);
        tv_title.setText("多选题");
        iv_back.setOnClickListener(v -> finish());
        for (int i = 0; i < buttonList.size(); i++) {
            int copyI = i;
            buttonList.get(i).setOnClickListener(v -> {
                curChoice[copyI] = !curChoice[copyI];
                buttonList.get(copyI).setBackgroundResource(curChoice[copyI] ? R.drawable.chosen_button : R.drawable.normal_button);
            });
        }
        tv_progress.setOnClickListener(v -> showDialog());
        tv_progress.setText(curIndex + 1 + "/" + QUESTION_COUNTS);
        tv_question.setText(questionList.get(curIndex).problem);
        btn_A.setText(questionList.get(curIndex).A);
        btn_B.setText(questionList.get(curIndex).B);
        btn_C.setText(questionList.get(curIndex).C);
        btn_D.setText(questionList.get(curIndex).D);
        btn_confirm.setOnClickListener(this);
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
                enableChoose();
                Arrays.fill(curChoice, false);
            } else {
                ToastUtil.show(MultipleChoiceActivity.this, "题号范围有误");
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
                Arrays.fill(curChoice, false);
                if (curIndex < QUESTION_COUNTS) {
                    enableChoose();
                    btn_next.setClickable(false);
                    refresh();
                }
                if (curIndex == QUESTION_COUNTS - 1) {
                    btn_next.setVisibility(View.GONE);
                    btn_finish.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_confirm:
                disableChoose();
                removeChoose();
                judgeSelection();
        }
    }


    private void disableChoose() {
        for (Button btn : buttonList) {
            btn.setClickable(false);
        }
    }

    private void removeChoose() {
        for (Button btn : buttonList) {
            btn.setBackgroundResource(R.drawable.normal_button);
        }
    }

    private void enableChoose() {
        for (Button btn : buttonList) {
            btn.setClickable(true);
        }
    }

    private void judgeSelection() {
        btn_next.setClickable(true);
        String correct = questionList.get(curIndex).answer;
        String choice = getAnswer();
        drawResult(correct, choice);
        if (!chosen[curIndex] && !correct.equals(choice)) {
            ErrorInfo info = new ErrorInfo(UserDBHelper.TYPE_MULTIPLE, curIndex);
            mDB.insert(info);
        }
        chosen[curIndex] = true;

    }

    private void drawResult(String correct, String choice) {
        for (int i = 0; i < buttonList.size(); i++) {
            Button btn = buttonList.get(i);
            if (correct.contains(options[i]) && choice.contains(options[i])) {
                btn.setBackgroundResource(R.drawable.correct_button);
                btn.setCompoundDrawables(null, null, drawableCorrect, null);
            } else if (correct.contains(options[i]) && !choice.contains(options[i])) {
                btn.setBackgroundResource(R.drawable.correct_miss_button);
                btn.setCompoundDrawables(null, null, drawableCorrectMiss, null);
            } else if (!correct.contains(options[i]) && choice.contains(options[i])) {
                btn.setBackgroundResource(R.drawable.wrong_button);
                btn.setCompoundDrawables(null, null, drawableWrong, null);
            }

        }
    }

    private String getAnswer() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < curChoice.length; i++) {
            if (curChoice[i]) {
                sb.append(options[i]);
            }
        }
        return sb.toString();
    }

    private void refresh() {
        removeComponents();
        refreshContent();

    }

    private void removeComponents() {
        for (Button btn : buttonList) {
            btn.setBackgroundResource(R.drawable.normal_button);
            btn.setCompoundDrawables(null, null, null, null);
        }
    }

    private void refreshContent() {
        tv_progress.setText(curIndex + 1 + "/" + QUESTION_COUNTS);
        tv_question.setText(questionList.get(curIndex).problem);
        btn_A.setText(questionList.get(curIndex).A);
        btn_B.setText(questionList.get(curIndex).B);
        btn_C.setText(questionList.get(curIndex).C);
        btn_D.setText(questionList.get(curIndex).D);
    }
}