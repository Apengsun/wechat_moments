package com.tw.hedpapp.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tw.hedpapp.R;
import com.tw.hedpapp.bean.Tweet;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * @Author: Sunzhipeng
 * @Date 2019/10/24
 * @Time 10:48
 */
public class CommentAdapter extends BaseQuickAdapter<Tweet.CommentsBean, BaseViewHolder> {


    public CommentAdapter(int layoutResId, @Nullable List<Tweet.CommentsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Tweet.CommentsBean item) {
        helper.setText(R.id.tv_nick, item.getSender().getNick()).setText(R.id.tv_comment,
                "：" + item.getContent());
    }
}
