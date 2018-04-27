package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.ui.news;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.ArticlesWrapper;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.SourcesWrapper;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.helper.ObservableHelper;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class NewsFragmentViewModel extends ViewModel {
    public MutableLiveData<ArticlesWrapper> mNewsWrapper = new MutableLiveData<>();
    public MutableLiveData<SourcesWrapper> mSourcesWrapper = new MutableLiveData<>();

    private String countryCode;
    private String sources;
    private String category;
    private String query;
    private String language;

    NewsFragmentViewModel(String countryCode, String sources, String category, String query) {
        this.countryCode = countryCode;
        this.sources = sources;
        this.category = category;
        this.query = query;
    }

    NewsFragmentViewModel(String query){
        this.query = query;
    }

    NewsFragmentViewModel(String language, String countryCode) {
        this.countryCode = countryCode;
        this.language = language;
    }

    public void fetchTopHeadlines() {
        Observable<ArticlesWrapper> getTopHeadlines = ObservableHelper.getTopHeadlines(countryCode,
                sources, category, query);

        getTopHeadlines.subscribeOn(Schedulers.io())
                .retry(1)
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends ArticlesWrapper>>() {
                    @Override
                    public Observable<? extends ArticlesWrapper> call(Throwable throwable) {
                        return null;
                    }
                })
                .subscribe(new Subscriber<ArticlesWrapper>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ArticlesWrapper articlesWrapper) {
                        mNewsWrapper.postValue(articlesWrapper);
                    }
                });
    }

    public void fetchEverything() {
        Observable<ArticlesWrapper> getEverything = ObservableHelper.getEverything(query);

        getEverything.subscribeOn(Schedulers.io())
                .retry(1)
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends ArticlesWrapper>>() {
                    @Override
                    public Observable<? extends ArticlesWrapper> call(Throwable throwable) {
                        return null;
                    }
                })
                .subscribe(new Subscriber<ArticlesWrapper>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ArticlesWrapper articlesWrapper) {
                        mNewsWrapper.postValue(articlesWrapper);
                    }
                });
    }

    public void fetchSources() {
        Observable<SourcesWrapper> getSources = ObservableHelper.getSources(language, countryCode);

        getSources.subscribeOn(Schedulers.io())
                .retry(1)
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends SourcesWrapper>>() {
                    @Override
                    public Observable<? extends SourcesWrapper> call(Throwable throwable) {
                        return null;
                    }
                })
                .subscribe(new Subscriber<SourcesWrapper>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(SourcesWrapper sourcesWrapper) {
                        mSourcesWrapper.postValue(sourcesWrapper);
                    }
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}