package com.ua.max.oliynick.flicker.activity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.internal.widget.ListViewCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ua.max.oliynick.flicker.error.ChatException;
import com.ua.max.oliynick.flicker.interfaces.IChatModel;
import com.ua.max.oliynick.flicker.model.ChatModel;
import com.ua.max.oliynick.flicker.singleton.MainApp;
import com.ua.max.oliynick.flicker.util.GenericObserver;
import com.ua.max.oliynick.flicker.widgets.RoundedImageView;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.packet.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import oliynick.max.ua.com.flicker.R;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * Created by Максим on 23.02.2016.
 */
@ContentView(R.layout.chat)
public class ChatActivity extends BaseActivity {

    //@Inject
    private IChatModel model;

    @InjectView(R.id.msgListView)
    private ListViewCompat msgRootListView;

    @InjectView(R.id.statusMessage)
    private TextView statusMessageLabel;

    @InjectView(R.id.avatar)
    private RoundedImageView avatarImageView;

    @InjectView(R.id.sendMsgBtn)
    private Button sendButton;

    @InjectView(R.id.inptMessArea)
    private EditText messArea;

    ArrayAdapter<String> itemsAdapter = null;

    private GenericObserver<Message> messageObserver = null;
    private GenericObserver<String> presenceObserver = null;

    private static Chat chat = null;

    public static synchronized void setChat(Chat c) {
        chat = c;
    }

    private static synchronized Chat getChat() {
        Chat tmp = chat;
        chat = null;
        return tmp;
    }

    public ChatActivity() {
        super();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(MainApp.getConnectionListener());
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(MainApp.getConnectionListener(), filter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ChatModel(getChat());

        List<String> messages = new ArrayList<>(15);

        for(Message m : model.loadMessages(15)) {
            messages.add(new String(m.getFrom() + " " + m.getBody()));
        }

        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messages);
        msgRootListView.setAdapter(itemsAdapter);

        initObservers();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSend();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyObservers();
    }

    private void initObservers() {

        messageObserver = new GenericObserver<Message>() {
            @Override
            public void onValueChanged(final Observable observable, final Message oldValue, final Message newValue) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        itemsAdapter.add(newValue.getFrom() + " " +newValue.getBody());
                        itemsAdapter.notifyDataSetChanged();
                    }
                });

            }
        };

        presenceObserver = new GenericObserver<String>() {

            @Override
            public void onValueChanged(Observable observable, String oldValue, final String newValue) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        statusMessageLabel.setText(newValue);
                    }
                });

            }
        };

        model.addMessageObserver(messageObserver);
        model.addPresenceObserver(presenceObserver);
    }

    private void destroyObservers() {
        model.removeMessageObserver(messageObserver);
        model.removePresenceObserver(presenceObserver);
    }

    private void onSend() {

        try {
            model.sendMessage(messArea.getText().toString());
        } catch (ChatException e) {
            e.printStackTrace();
        }
    }

}
