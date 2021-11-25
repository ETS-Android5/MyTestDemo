package com.example.mytestdemo.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.mytestdemo.R;

public class WeChatFragment extends Fragment {

    View view;
    private View mCardAlp;
    private View mCardbg;
    private ImageView mChatImg;
    private CardView cardView;
    private TextView mChatGroupTitle;
    private TextView mItemChatTitle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_wechat_list,container,false);
        initView();
        mCardAlp.getBackground().setAlpha(0);
        mCardbg.getBackground().setAlpha(100);
        try {
            Glide.with(requireActivity()).load("https://files.lumingyuan6868.xyz/2021/11/19/29579ed5b7cc4994b59e893c47677117.jpg").placeholder(android.R.drawable.ic_input_add).apply(new RequestOptions()
                    .transforms(new CenterCrop(), new RoundedCorners(20)
                    )).into(mChatImg);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
        return view;
    }

    @SuppressLint("CutPasteId")
    private void initView() {
        mCardAlp = view.findViewById(R.id.card_alp);
        mCardbg=view.findViewById(R.id.card_alp_card_bg);
       cardView=view.findViewById(R.id.card_alp_card_bg);
        mChatImg = view.findViewById(R.id.chat_img);
        mChatGroupTitle = view.findViewById(R.id.chat_group_title);
        mItemChatTitle = view.findViewById(R.id.item_chat_title);
    }
}
