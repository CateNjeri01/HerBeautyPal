package com.example.mercie.example.fragments.dermatologist;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mercie.example.R;
import com.example.mercie.example.adapters.MessageRecyclerViewAdapter;
import com.example.mercie.example.models.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {

    private static final String TAG = "ChatsFragment";

    private List<Message> messageList;

    private FirebaseFirestore mDb;
    private FirebaseAuth mAuth;
    private MessageRecyclerViewAdapter adapter;

    public ChatsFragment() {
        // Required empty public constructor
        mDb = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        messageList = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dermatologist_fragment_chats, container, false);

        RecyclerView chatsRV = view.findViewById(R.id.messagesRV);
        chatsRV.setHasFixedSize(true);
        chatsRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MessageRecyclerViewAdapter(messageList);

        chatsRV.setAdapter(adapter);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mDb.collection("chats")
                .addSnapshotListener(
                        (queryDocumentSnapshots, e) -> {
                            if (e != null) {
                                Log.e(TAG, "onViewCreated: ", e);
                                return;
                            }

                            if (queryDocumentSnapshots.isEmpty()) {
                                Toast.makeText(getActivity(), "No chats Yet", Toast.LENGTH_SHORT).show();
                            } else {
                                messageList.clear();
                                for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                                    Message msg = snapshot.toObject(Message.class);

                                    messageList.add(msg);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                );


    }
}
