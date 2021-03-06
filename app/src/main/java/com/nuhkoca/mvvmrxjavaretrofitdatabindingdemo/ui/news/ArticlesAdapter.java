package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.ui.news;

import android.content.Context;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.BR;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.R;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.callback.IOverflowMenuItemClickListener;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.entity.DbEverything;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.entity.DbTopHeadlines;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.Articles;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.databinding.NewsItemCardBinding;

import java.util.ArrayList;
import java.util.List;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {

    private List<Articles> mArticlesList;
    private List<DbTopHeadlines> mDbTopHeadlines;
    private List<DbEverything> mDbEverything;
    private IOverflowMenuItemClickListener mIOverflowMenuItemClickListener;

    private Context mContext;

    public ArticlesAdapter(IOverflowMenuItemClickListener mIOverflowMenuItemClickListener) {
        mArticlesList = new ArrayList<>();
        this.mIOverflowMenuItemClickListener = mIOverflowMenuItemClickListener;
    }

    public ArticlesAdapter(List<DbTopHeadlines> mDbTopHeadlines, List<DbEverything> mDbEverything, IOverflowMenuItemClickListener mIOverflowMenuItemClickListener) {
        this.mDbTopHeadlines = mDbTopHeadlines;
        this.mDbEverything = mDbEverything;
        this.mIOverflowMenuItemClickListener = mIOverflowMenuItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        NewsItemCardBinding newsItemCardBinding = DataBindingUtil.inflate(layoutInflater,
                R.layout.news_item_card, parent, false);

        return new ViewHolder(newsItemCardBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mArticlesList != null) {
            Articles articles = mArticlesList.get(position);

            holder.bindViews(articles);
        } else if (mDbTopHeadlines != null) {
            DbTopHeadlines dbTopHeadlines = mDbTopHeadlines.get(position);

            holder.bindViews(dbTopHeadlines);
        } else {
            DbEverything dbEverything = mDbEverything.get(position);

            holder.bindViews(dbEverything);
        }

        holder.prepareLines();
    }

    public void swapData(List<Articles> articlesList) {
        mArticlesList = articlesList;

        notifyDataSetChanged();
    }

    public void swapOfflineTopHeadlines(List<DbTopHeadlines> dbTopHeadlinesList) {
        this.mDbTopHeadlines = dbTopHeadlinesList;

        notifyDataSetChanged();
    }

    public void swapOfflineEverything(List<DbEverything> dbEverythingList) {
        this.mDbEverything = dbEverythingList;

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mArticlesList != null) {
            return mArticlesList.size();
        } else if (mDbTopHeadlines != null) {
            return mDbTopHeadlines.size();
        } else {
            return mDbEverything.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private NewsItemCardBinding mNewsItemCardBinding;

        ViewHolder(View itemView) {
            super(itemView);

            mNewsItemCardBinding = DataBindingUtil.getBinding(itemView);
        }

        void bindViews(Articles articles) {
            if (articles != null) {
                mNewsItemCardBinding.setVariable(BR.articlesImage, articles.getUrlToImage());
                mNewsItemCardBinding.setVariable(BR.articlesTitle, articles.getTitle());
                mNewsItemCardBinding.setVariable(BR.articlesDescription, articles.getDescription());
                mNewsItemCardBinding.setVariable(BR.articlesUrl, articles.getUrl());
                mNewsItemCardBinding.setVariable(BR.overFlowClickListener, mIOverflowMenuItemClickListener);
                mNewsItemCardBinding.setVariable(BR.articlesSourceName, articles.getSource().getName());

                mNewsItemCardBinding.executePendingBindings();
            }
        }

        void bindViews(DbTopHeadlines dbTopHeadlines) {
            if (dbTopHeadlines != null) {
                mNewsItemCardBinding.setVariable(BR.articlesImage, dbTopHeadlines.getUrlToImage());
                mNewsItemCardBinding.setVariable(BR.articlesTitle, dbTopHeadlines.getTitle());
                mNewsItemCardBinding.setVariable(BR.articlesDescription, dbTopHeadlines.getDescription());
                mNewsItemCardBinding.setVariable(BR.articlesUrl, dbTopHeadlines.getUrl());
                mNewsItemCardBinding.setVariable(BR.overFlowClickListener, mIOverflowMenuItemClickListener);
                mNewsItemCardBinding.setVariable(BR.articlesSourceName, dbTopHeadlines.getSource());

                mNewsItemCardBinding.executePendingBindings();
            }
        }

        void bindViews(DbEverything dbEverything) {
            if (dbEverything != null) {
                mNewsItemCardBinding.setVariable(BR.articlesImage, dbEverything.getUrlToImage());
                mNewsItemCardBinding.setVariable(BR.articlesTitle, dbEverything.getTitle());
                mNewsItemCardBinding.setVariable(BR.articlesDescription, dbEverything.getDescription());
                mNewsItemCardBinding.setVariable(BR.articlesUrl, dbEverything.getUrl());
                mNewsItemCardBinding.setVariable(BR.overFlowClickListener, mIOverflowMenuItemClickListener);
                mNewsItemCardBinding.setVariable(BR.articlesSourceName, dbEverything.getSource());

                mNewsItemCardBinding.executePendingBindings();
            }
        }

        void prepareLines() {
            int config = mContext.getResources().getConfiguration().orientation;

            if (config == Configuration.ORIENTATION_LANDSCAPE) {
                mNewsItemCardBinding.tvSourceDescription.setLines(7);
                mNewsItemCardBinding.tvSourceName.setLines(1);
            }
        }
    }
}