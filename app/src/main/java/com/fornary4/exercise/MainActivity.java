package com.fornary4.exercise;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


import com.fornary4.exercise.database.UserDBHelper;
import com.fornary4.exercise.dialog.AlertDialogTool;
import com.fornary4.exercise.entity.ErrorInfo;
import com.fornary4.exercise.util.ToastUtil;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout btn_into_single;
    private LinearLayout btn_into_judge;
    private LinearLayout btn_into_multiple;
    private LinearLayout btn_into_error;
    private LinearLayout btn_into_about;
    private LinearLayout btn_into_answer;
    private LinearLayout btn_into_exam;
    private LinearLayout btn_into_do_error;


    private UserDBHelper mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initDB();
        TestDB();
    }


    private void initView() {
        btn_into_single = findViewById(R.id.btn_into_single);
        btn_into_judge = findViewById(R.id.btn_into_judge);
        btn_into_multiple = findViewById(R.id.btn_into_multiple);
        btn_into_error = findViewById(R.id.btn_into_error);
        btn_into_about = findViewById(R.id.btn_into_about);
        btn_into_answer = findViewById(R.id.btn_into_answer);
        btn_into_exam = findViewById(R.id.btn_into_exam);
        btn_into_do_error = findViewById(R.id.btn_into_do_error);

        btn_into_single.setOnClickListener(this);
        btn_into_judge.setOnClickListener(this);
        btn_into_multiple.setOnClickListener(this);
        btn_into_error.setOnClickListener(this);
        btn_into_about.setOnClickListener(this);
        btn_into_answer.setOnClickListener(this);
        btn_into_exam.setOnClickListener(this);
        btn_into_do_error.setOnClickListener(this);
    }

    private void initDB() {
        mDB = UserDBHelper.getInstance(this);
        mDB.openReadLink();
        mDB.openWriteLink();
    }

    private void TestDB() {
        List<ErrorInfo> list = mDB.queryByType(0);
        for (ErrorInfo info : list) {
            Log.d("fornary", info.toString());
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_into_single:
                startActivity(new Intent(this, SingleChoiceActivity.class));
                break;
            case R.id.btn_into_judge:
                startActivity(new Intent(this, JudgeActivity.class));
                break;
            case R.id.btn_into_multiple:
                startActivity(new Intent(this, MultipleChoiceActivity.class));
                break;
            case R.id.btn_into_error:
                showChoose();
                break;
            case R.id.btn_into_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.btn_into_answer:
                showMenu();
                break;
            case R.id.btn_into_exam:
                startActivity(new Intent(this, ExamActivity.class));
                break;
            case R.id.btn_into_do_error:
                showDoError();
                break;

        }
    }

    private void showDoError() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.dialog_choose_type, null);

        AlertDialog dialog = AlertDialogTool.buildCustomStylePopupDialogGravity(this, v, Gravity.TOP, R.style.DialogTopPopup);

        Button btn_close = v.findViewById(R.id.btn_cancel);
        Button btn_type_single = v.findViewById(R.id.btn_type_single);
        Button btn_type_multiple = v.findViewById(R.id.btn_type_multiple);
        Button btn_type_judge = v.findViewById(R.id.btn_type_judge);



        btn_close.setOnClickListener(view -> dialog.dismiss());
        btn_type_single.setOnClickListener(view -> {
            dialog.dismiss();
            if (mDB.queryByType(UserDBHelper.TYPE_SINGLE).size() > 0) {
                Intent intent = new Intent(MainActivity.this, SingleChoiceActivity.class);
                intent.putExtra("isDoError", 1);
                startActivity(intent);
            } else {
                ToastUtil.show(MainActivity.this, "还没有错题");
            }

        });
        btn_type_multiple.setOnClickListener(view -> {
            dialog.dismiss();
            if (mDB.queryByType(UserDBHelper.TYPE_MULTIPLE).size() > 0) {
                Intent intent = new Intent(MainActivity.this, MultipleChoiceActivity.class);
                intent.putExtra("isDoError", 1);
                startActivity(intent);
            } else {
                ToastUtil.show(MainActivity.this, "还没有错题");
            }
        });
        btn_type_judge.setOnClickListener(view -> {
            dialog.dismiss();
            if (mDB.queryByType(UserDBHelper.TYPE_JUDGE).size() > 0) {
                Intent intent = new Intent(MainActivity.this, JudgeActivity.class);
                intent.putExtra("isDoError", 1);
                startActivity(intent);
            } else {
                ToastUtil.show(MainActivity.this, "还没有错题");
            }
        });

        dialog.show();
    }

    private void showMenu() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.dialog_choose_type, null);

        AlertDialog dialog = AlertDialogTool.buildCustomStylePopupDialogGravity(this, v, Gravity.TOP, R.style.DialogTopPopup);

        Button btn_close = v.findViewById(R.id.btn_cancel);
        Button btn_type_single = v.findViewById(R.id.btn_type_single);
        Button btn_type_multiple = v.findViewById(R.id.btn_type_multiple);
        Button btn_type_judge = v.findViewById(R.id.btn_type_judge);



        btn_close.setOnClickListener(view -> dialog.dismiss());
        btn_type_single.setOnClickListener(view -> {
            dialog.dismiss();
            Intent intent = new Intent(MainActivity.this, ViewChoiceActivity.class);
            intent.putExtra("type", UserDBHelper.TYPE_SINGLE);
            startActivity(intent);
        });
        btn_type_multiple.setOnClickListener(view -> {
            dialog.dismiss();
            Intent intent = new Intent(MainActivity.this, ViewChoiceActivity.class);
            intent.putExtra("type", UserDBHelper.TYPE_MULTIPLE);
            startActivity(intent);
        });
        btn_type_judge.setOnClickListener(view -> {
            dialog.dismiss();
            Intent intent = new Intent(MainActivity.this, ViewJudgeActivity.class);
            startActivity(intent);
        });

        dialog.show();
    }

    private void showChoose() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.dialog_choose_type, null);

        AlertDialog dialog = AlertDialogTool.buildCustomStylePopupDialogGravity(this, v, Gravity.TOP, R.style.DialogTopPopup);

        Button btn_close = v.findViewById(R.id.btn_cancel);
        Button btn_type_single = v.findViewById(R.id.btn_type_single);
        Button btn_type_multiple = v.findViewById(R.id.btn_type_multiple);
        Button btn_type_judge = v.findViewById(R.id.btn_type_judge);



        btn_close.setOnClickListener(view -> dialog.dismiss());
        btn_type_single.setOnClickListener(view -> {
            dialog.dismiss();
            Intent intent = new Intent(MainActivity.this, ErrorActivity.class);
            intent.putExtra("type", UserDBHelper.TYPE_SINGLE);
            startActivity(intent);
        });
        btn_type_multiple.setOnClickListener(view -> {
            dialog.dismiss();
            Intent intent = new Intent(MainActivity.this, ErrorActivity.class);
            intent.putExtra("type", UserDBHelper.TYPE_MULTIPLE);
            startActivity(intent);
        });
        btn_type_judge.setOnClickListener(view -> {
            dialog.dismiss();
            Intent intent = new Intent(MainActivity.this, ErrorActivity.class);
            intent.putExtra("type", UserDBHelper.TYPE_JUDGE);
            startActivity(intent);
        });



        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDB.closeLink();
    }
}