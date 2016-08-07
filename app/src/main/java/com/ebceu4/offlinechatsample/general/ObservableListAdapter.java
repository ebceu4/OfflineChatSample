package com.ebceu4.offlinechatsample.general;

import android.support.v7.widget.RecyclerView;

public abstract class ObservableListAdapter extends RecyclerView.Adapter {
    public abstract void notifyLastItemInserted();
}
