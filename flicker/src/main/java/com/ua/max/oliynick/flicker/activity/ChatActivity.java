package com.ua.max.oliynick.flicker.activity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.internal.widget.ListViewCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.inject.Inject;
import com.ua.max.oliynick.flicker.error.ChatException;
import com.ua.max.oliynick.flicker.interfaces.IChatModel;
import com.ua.max.oliynick.flicker.model.ChatModel;
import com.ua.max.oliynick.flicker.singleton.MainApp;
import com.ua.max.oliynick.flicker.util.GenericObserver;
import com.ua.max.oliynick.flicker.util.MessageModel;
import com.ua.max.oliynick.flicker.widgets.RoundedImageView;

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

    /**
     * <p>
     *     Represents chat jid key. jid should be passed
     *     via intent as extra string
     * </p>
     * */
    public static final String JID_KEY = ChatActivity.class.getCanonicalName().concat(".jid_key");

    private enum Pos {left, right};

    @Inject
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

    private GenericObserver<MessageModel> messageObserver = null;
    private GenericObserver<String> presenceObserver = null;

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

        ((ChatModel)model).setChat(getIntent().getStringExtra(ChatActivity.JID_KEY));

        List<String> messages = new ArrayList<>(15);

        try {
            for(MessageModel m : model.loadMessages(15)) {
                messages.add(new String(m.getDirection() + " " + m.getBody()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "failed to load initial messages for ".
                    concat(getIntent().getStringExtra(ChatActivity.JID_KEY)), Toast.LENGTH_LONG);
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

        messageObserver = new GenericObserver<MessageModel>() {
            @Override
            public void onValueChanged(final Observable observable, final MessageModel oldValue, final MessageModel newValue) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addMessage(newValue.getBody(), newValue.getTitle(), Pos.left);
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

        final String body = messArea.getText().toString();
        final AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {

            @Override
            protected void onPostExecute(String errMessage) {
                super.onPostExecute(errMessage);

                if(errMessage == null) {
                    messArea.setText(null);
                    addMessage(body, MainApp.getConnection().getUser(), Pos.right);
                } else {
                    Toast.makeText(getBaseContext(), "error occurred while sending message", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    model.sendMessage(body);
                } catch (ChatException e) {
                    e.printStackTrace();
                    //TODO add proper message
                    return e.getMessage();
                }

                return null;
            }
        };

        task.execute();
    }

    private void addMessage(final String message, final String title, final Pos pos) {
        itemsAdapter.add(title + " " + message);
        itemsAdapter.notifyDataSetChanged();
    }

}
