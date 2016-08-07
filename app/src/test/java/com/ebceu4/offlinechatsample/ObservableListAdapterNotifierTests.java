package com.ebceu4.offlinechatsample;

import android.databinding.ObservableArrayList;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ebceu4.offlinechatsample.general.ObservableListAdapterNotifier;
import static org.mockito.Mockito.*;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(MockitoJUnitRunner.class)
public class ObservableListAdapterNotifierTests {

    class StubViewHolder extends RecyclerView.ViewHolder
    {
        public StubViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Test
    public void shouldCallNotifyDataSetChangedOnChange() throws Exception {

    }
}