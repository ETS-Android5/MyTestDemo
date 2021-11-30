package com.qj315.mytestdemo.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mytestdemo.R;
import com.qj315.mytestdemo.bean.update;

import org.jetbrains.annotations.NotNull;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class DialogUpdateV extends Dialog {
    TextView mTitle;
    EditText mMsgDataDialog;
    Button mBtnYes;
    int vs;
    String code;

    public DialogUpdateV(@NonNull @NotNull Context context) {
        super(context);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_update);
        initView();
        getVS();
        mTitle.setText("更新版本code");
        mMsgDataDialog.setLines(1);
        mMsgDataDialog.setInputType(InputType.TYPE_CLASS_NUMBER);
        mBtnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVS();
                if (!mMsgDataDialog.getText().toString().isEmpty()) {
                    update ves = new update();
                    int newV = Integer.parseInt(mMsgDataDialog.getText().toString());
                    if (newV > vs) {
                        ves.setCode(mMsgDataDialog.getText().toString());
                        ves.update("hgLn333N", new UpdateListener() {

                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Toast.makeText(getContext(), "成功更新版本号" + ves.getUpdatedAt(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "更新失败！" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        });
                    } else {
                        Toast.makeText(getContext(), "版本code不能下调，需大于当前版本!", Toast.LENGTH_SHORT).show();
                    }
                    dismiss();
                }else {
                    Toast.makeText(getContext(), "版本code不能为空！", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void initView() {

        mTitle = findViewById(R.id.titlefb);
        mMsgDataDialog = findViewById(R.id.msg_data_dialog);
        mBtnYes = findViewById(R.id.btn_yes);
    }

    private void getVS() {
        BmobQuery<update> bmobQuery = new BmobQuery<update>();
        bmobQuery.getObject("hgLn333N", new QueryListener<update>() {
            @Override
            public void done(update object, BmobException e) {
                if (e == null) {
                    code = object.getCode();
                    vs = Integer.parseInt(code);
                    mMsgDataDialog.setHint("当前版本code为" + vs);
                    Log.i("TAG", "done: "+code+vs);
//                            toast("查询成功");
                } else {
//                            toast("查询失败：" + e.getMessage());
                    Toast.makeText(getContext(), "获取默认版本号异常！" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
