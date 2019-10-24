package com.tw.hedpapp.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.tw.commonsdk.glide.ImageLoader;
import com.tw.commonsdk.utils.Logger;
import com.tw.commonsdk.utils.ResUtils;
import com.tw.hedpapp.R;
import com.tw.hedpapp.bean.Tweet;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @Author: Sunzhipeng
 * @Date 2019/10/24
 * @Time 10:50
 */
public class TweetsAdapter extends BaseQuickAdapter<Tweet, BaseViewHolder> {

    public TweetsAdapter(@Nullable List<Tweet> itemsBeanList) {
        super(R.layout.item_wechat_moments, itemsBeanList);
    }

    @Override
    protected void convert(BaseViewHolder helper, Tweet item) {
        if (item.getSender() == null) {
            return;
        }
        ImageView ivAvatar = helper.getView(R.id.iv_avatar);
        ImageLoader.getImageLoader(mContext)
                .loadUrl(item.getSender().getAvatar(), ivAvatar);
        helper.setText(R.id.tv_nick, item.getSender().getNick());
        if (!TextUtils.isEmpty(item.getContent())) {
            helper.setGone(R.id.tv_content, true);
            TextView tvContent = (TextView) helper.getView(R.id.tv_content);
            tvContent.setText(item.getContent());
            Logger.e("***" + tvContent.getLineCount());
            tvContent.post(new Runnable() {
                @Override
                public void run() {
                    if (tvContent.getLineCount() > 4) {
                        tvContent.setMaxLines(4);
                        helper.setGone(R.id.tv_open_close, true);
                        TextView tvOpenClose = (TextView) helper.getView(R.id.tv_open_close);
                        tvOpenClose.setText(ResUtils.resString(R.string.app_full_text));
                        tvOpenClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (ResUtils.resString(R.string.app_full_text).equals(tvOpenClose.getText())) {
                                    tvContent.setMaxLines(Integer.MAX_VALUE);
                                    tvOpenClose.setText(ResUtils.resString(R.string.app_tuck_up));
                                } else {
                                    tvContent.setMaxLines(4);
                                    tvOpenClose.setText(ResUtils.resString(R.string.app_full_text));
                                }
                            }
                        });
                    } else {
                        helper.setGone(R.id.tv_open_close, false);
                    }
                }
            });

        } else {
            helper.setGone(R.id.tv_content, false);
            helper.setGone(R.id.tv_open_close, false);
        }
        List<Tweet.ImagesBean> images = item.getImages();
        if (item.getImages() != null && !item.getImages().isEmpty()) {
            helper.setGone(R.id.ngv, true);
            ArrayList<ImageInfo> imageInfo = new ArrayList<>();
            if (images != null) {
                for (Tweet.ImagesBean image : images) {
                    ImageInfo info = new ImageInfo();
                    info.setThumbnailUrl(image.getUrl());
                    info.setBigImageUrl(image.getUrl());
                    imageInfo.add(info);
                }
            }
            NineGridView nineGrid = helper.getView(R.id.ngv);
            nineGrid.setAdapter(new NineGridViewClickAdapter(mContext, imageInfo));

        } else {
            helper.setGone(R.id.ngv, false);
        }

        if (item.getComments() != null && !item.getComments().isEmpty()) {
            helper.setGone(R.id.rv_comment, true);
            LinearLayoutManager manager = new LinearLayoutManager(mContext);
            manager.setOrientation(RecyclerView.VERTICAL);
            RecyclerView rv = helper.getView(R.id.rv_comment);
            rv.setLayoutManager(manager);
            rv.setAdapter(new CommentAdapter(R.layout.item_coment, item.getComments()));
        } else {
            helper.setGone(R.id.rv_comment, false);
        }
    }
}