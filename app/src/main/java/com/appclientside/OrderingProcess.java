package com.appclientside;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.appclientside.com.utils.ChatMessage;
import com.appclientside.com.utils.Usuario;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class OrderingProcess extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseListAdapter<ChatMessage> adapter;
    private ListView listOfMessages;
    private SwipeRefreshLayout swr;

    private Handler handler;
    private Runnable runn;
    private int delay; //milliseconds

    private Usuario currentUser = new Usuario();
    private Usuario resCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordering_process);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("mensajes/" + getIntent().getStringExtra("orderCode"));
        FloatingActionButton fab =
                findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = findViewById(R.id.input);

                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                myRef.push()
                        .setValue(new ChatMessage(input.getText().toString(),
                                ((Usuario) getIntent().getSerializableExtra("currentUser")).getNombre())
                        );

                // Clear the input
                input.setText("");
                displayChatMessages();
            }
        });
        listOfMessages = findViewById(R.id.list_of_messages);

        displayChatMessages();
        swr = findViewById(R.id.swiperefresh);
        swr.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        displayChatMessages();
                        swr.setRefreshing(false);
                    }
                }
        );

        handler = new Handler();
        delay = 500; //milliseconds

        runn = new Runnable() {
            public void run() {
                if (currentUser.getContratando() != null && currentUser.getContratando().isEmpty()) {
                    Intent intent = new Intent(OrderingProcess.this, MapsActivity.class);
                    startActivity(intent);
                }
            }
        };


        handler.postDelayed(runn, delay);


    }


    public void saverUser() {
        currentUser = resCurrentUser;
    }

    private void getCurrentClient(String userName) {
        myRef = database.getReference("clients");
        Query query = myRef.orderByChild("correo").equalTo(mAuth.getCurrentUser().getEmail().replace("@", "+").replace(".", "-"));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    resCurrentUser = dataSnapshot.getChildren().iterator().next().getValue(Usuario.class);
                    Log.i("Current", resCurrentUser.getNombre());
                }
                saverUser();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void displayChatMessages() {

        FirebaseListOptions<ChatMessage> options = new FirebaseListOptions.Builder<ChatMessage>()
                .setQuery(myRef, ChatMessage.class)
                .setLayout(R.layout.message)
                .build();
        adapter = new FirebaseListAdapter<ChatMessage>(options) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml
                TextView messageText = v.findViewById(R.id.message_text);
                TextView messageUser = v.findViewById(R.id.message_user);
                TextView messageTime = v.findViewById(R.id.message_time);
                Log.i("adaptador", model.getMessageText());
                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }
        };

        adapter.startListening();
        listOfMessages.setAdapter(adapter);
    }
}
