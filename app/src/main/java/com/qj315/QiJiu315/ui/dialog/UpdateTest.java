package com.qj315.QiJiu315.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mytestdemo.R;
import com.qj315.QiJiu315.bean.Messages;

import org.jetbrains.annotations.NotNull;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class UpdateTest extends Dialog {
    EditText msg;
    Button update;

    public UpdateTest(@NonNull @NotNull Context context) {
        super(context);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_update);
        msg = findViewById(R.id.msg_data_dialog);
        update = findViewById(R.id.btn_yes);
        msg.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);//多行模式
        msg.setSingleLine(false);//是否单行模式
        msg.setHorizontallyScrolling(false);//是否水平滚动
        BmobQuery<Messages> bmobQuery = new BmobQuery<Messages>();
        bmobQuery.getObject("Hnyl4449", new QueryListener<Messages>() {
            @Override
            public void done(Messages object, BmobException e) {
                if (e == null) {
                    msg.setText(object.getNotice());
                } else {
                    Toast.makeText(getContext(), "数据加载失败！" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = msg.getText().toString();
                if (s.length() <= 120) {
                    Messages msgdata = new Messages();
                    msgdata.setNotice(s);
                    msgdata.update("Hnyl4449", new UpdateListener() {

                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(getContext(), "更新成功！" + msgdata.getUpdatedAt(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "更新失败！" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
                    dismiss();
                }else {
                    Toast.makeText(getContext(), "最多支持输入120个字符！", Toast.LENGTH_SHORT).show();
                }
            }

        });


    }
}
