package com.ebceu4.offlinechatsample.general;

import android.databinding.ObservableList;

public class ObservableListAdapterNotifier<T> extends ObservableList.OnListChangedCallback<ObservableList<T>>{

    private ObservableListAdapter adapter;

    public ObservableListAdapterNotifier(ObservableListAdapter adapter){
        this.adapter = adapter;
    }

    @Override
    public void onChanged(ObservableList<T> list) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemRangeChanged(ObservableList<T> list, int i, int count) {
        adapter.notifyItemRangeChanged(i, count);
    }

    @Override
    public void onItemRangeInserted(ObservableList<T> list, int i, int count) {
        adapter.notifyItemRangeInserted(i,count);
        if(list.size() == i + count){
            adapter.notifyLastItemInserted();
        }
    }

    @Override
    public void onItemRangeMoved(ObservableList<T> list, int from, int to, int count) {

        if(from > to)
        {
            int tmp = to;
            to = from;
            from = tmp;
        }

        adapter.notifyItemRangeRemoved(from,count);
        adapter.notifyItemRangeInserted(to-count,count);
    }

    @Override
    public void onItemRangeRemoved(ObservableList<T> list, int i, int count) {
        adapter.notifyItemRangeRemoved(i, count);
    }
}
