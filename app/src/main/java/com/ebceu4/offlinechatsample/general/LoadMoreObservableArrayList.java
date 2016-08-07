package com.ebceu4.offlinechatsample.general;

import android.databinding.ObservableArrayList;
import rx.Single;
import rx.functions.Func1;

public class LoadMoreObservableArrayList<T> extends ObservableArrayList<T> {

    private Func1<ObservableArrayList<T>, Single<Object>> loadMoreFunc;

    public LoadMoreObservableArrayList(Func1<ObservableArrayList<T>, Single<Object>> loadMoreFunc){
        this.loadMoreFunc = loadMoreFunc;
    }

    private boolean isLoading = false;

    public void loadMore() {

        if(isLoading)
            return;

        isLoading = true;

        loadMoreFunc.call(this).subscribe(v -> {
            isLoading = false;
        });
    }
}
