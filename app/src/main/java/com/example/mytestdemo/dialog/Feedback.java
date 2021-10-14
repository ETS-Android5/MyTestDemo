package com.example.mytestdemo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mytestdemo.R;
import com.example.mytestdemo.userlist.UserList;

import org.jetbrains.annotations.NotNull;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class Feedback extends Dialog {
    private EditText msg;
    private Button feedbackbtn;
    private TextView feedback,title;

    public Feedback(@NonNull @NotNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.updatedialog);
        msg = findViewById(R.id.msg_data_dialog);
        feedbackbtn = findViewById(R.id.btn_yes);
        title = findViewById(R.id.titlefb);
        feedback=findViewById(R.id.feedback);
        msg.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);//多行模式
        msg.setSingleLine(false);//是否单行模式
        msg.setHorizontallyScrolling(false);//是否水平滚动
        feedbackbtn.setText("反馈");
        feedback.setText("反馈内容:");
        msg.setHint("输入反馈信息");
        title.setText("反馈或留言");

        feedbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = msg.getText().toString();
                String username = BmobUser.getCurrentUser().getUsername();
                if (!s.isEmpty()) {
                    UserList userfeedback = new UserList();
                    userfeedback.setFeedbackmsg(s);
                    userfeedback.setFeedbackuser(username);
                    userfeedback.save(new SaveListener<String>() {
                        @Override
                        public void done(String objectId, BmobException e) {
                            if (e == null) {
                                Toast.makeText(getContext(), "反馈成功！", Toast.LENGTH_SHORT).show();
//                            toast("添加数据成功，返回objectId为："+objectId);
                            } else {
//                            toast("创建数据失败：" + e.getMessage());
                                Toast.makeText(getContext(), "发送失败！" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                dismiss();
            }
        });
        super.onCreate(savedInstanceState);
    }
}
