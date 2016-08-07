package com.ebceu4.offlinechatsample.general;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import com.ebceu4.offlinechatsample.BR;
import rx.functions.Func0;

public class BoundViewHolder<TItem, TBinding extends ViewDataBinding> extends RecyclerView.ViewHolder{

    private TBinding binding;

    public static <TItem, TBinding extends ViewDataBinding> BoundViewHolder<TItem, TBinding> create(Func0<TBinding> createBinding) {
       return new BoundViewHolder<>(createBinding.call());
    }

    public BoundViewHolder(TBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(TItem item) {
        binding.setVariable(BR.vm, item);
    }
}
