package com.ebceu4.offlinechatsample.activities.chatRoom;

import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.ebceu4.offlinechatsample.InjectionApp;
import com.ebceu4.offlinechatsample.R;
import com.ebceu4.offlinechatsample.databinding.ActivityChatRoomBinding;
import com.ebceu4.offlinechatsample.databinding.ItemChatMessageBinding;
import com.ebceu4.offlinechatsample.general.BoundViewHolder;
import com.ebceu4.offlinechatsample.general.ObservableListAdapter;
import com.ebceu4.offlinechatsample.general.ObservableListAdapterNotifier;
import com.ebceu4.offlinechatsample.infrastructure.dto.ChatMessage;
import com.ebceu4.offlinechatsample.OfflineChatApplication;
import com.ebceu4.offlinechatsample.infrastructure.services.PermissionManager;

import javax.inject.Inject;


public class ChatRoomActivity extends AppCompatActivity {

    @Inject
    public ChatRoomViewModel viewModel;

    @Inject
    public PermissionManager permissionManager;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((OfflineChatApplication) getApplication()).activityDestroyed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean isGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        permissionManager.permissionRequestResult(permissions[0], isGranted);
    }

    @Override
    protected void onStart() {
        super.onStart();
        permissionManager.registerForPermissionProcessing(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        permissionManager.unregisterFromPermissionProcessing(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        InjectionApp.DI.inject(this);

        ((OfflineChatApplication)getApplication()).activityCreated();

        setContentView(R.layout.activity_chat_room);

        ActivityChatRoomBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_room);

        binding.setVm(viewModel);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        ObservableListAdapter adapter = new ObservableListAdapter() {

            @Override
            public void notifyLastItemInserted() {
                int size = viewModel.getMessages().size();
                if(layoutManager.findLastCompletelyVisibleItemPosition() == size - 2) {
                    layoutManager.scrollToPosition(size-1);
                }
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return BoundViewHolder.create(() -> ItemChatMessageBinding.inflate(getLayoutInflater(), parent, false));
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

                if (position == 0)
                    viewModel.getMessages().loadMore();

                ChatMessage item = viewModel.getMessages().get(position);
                ((BoundViewHolder) holder).bind(item);
            }

            @Override
            public int getItemCount() {

                int size = viewModel.getMessages().size();

                if(size == 0)
                    viewModel.getMessages().loadMore();

                return size;
            }
        };

        viewModel.getMessages().addOnListChangedCallback(new ObservableListAdapterNotifier<ChatMessage>(adapter));

        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(false);
        layoutManager.setStackFromEnd(true);

        binding.messagesList.setLayoutManager(layoutManager);

        binding.messagesList.setAdapter(adapter);
    }

}
