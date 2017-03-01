package siddu.chatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import static android.R.attr.id;

public class chatActivity extends AppCompatActivity {
    private Button button_send_msg ;
    private EditText input_msg;
  // private TextView chat_msg;
    private String user_name,friend_name;
    private DatabaseReference root;
    private String temp_key;
    private String inchat_msg,chat_user_name;
    private ListView displaymsgs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        button_send_msg = (Button) findViewById(R.id.sendmsg);
        input_msg = (EditText) findViewById(R.id.entermsg);
        displaymsgs=(ListView)findViewById(R.id.msgslist);
        arraymsgs=new ArrayList<String>();
        arrayAdaptermsgs =new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arraymsgs);
        displaymsgs.setAdapter(arrayAdaptermsgs);
        user_name = getIntent().getExtras().get("user_name").toString();
        friend_name = getIntent().getExtras().get("friend_name").toString();
        setTitle(""+friend_name);
        root= FirebaseDatabase.getInstance().getReference().child(friend_name);

        input_msg.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    button_send_msg.performClick();
                    return true;

                }
                return false;
            }
        });
        button_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> map = new HashMap<String, Object>();
                temp_key=root.push().getKey();
                root.updateChildren(map);
                DatabaseReference message_root = root.child(temp_key);
            Map<String,Object> map2=new HashMap<String, Object>();
            map2.put("name",user_name);
            map2.put("msg",input_msg.getText().toString());
            message_root.updateChildren(map2);
                input_msg.setText("");

        }
    });
    root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    ArrayList<String> arraymsgs;
    ArrayAdapter<String> arrayAdaptermsgs;
    private void append_chat_conversation(DataSnapshot dataSnapshot){
        Iterator i = dataSnapshot.getChildren().iterator();
        while(i.hasNext()){
            inchat_msg =(String) ((DataSnapshot)i.next()).getValue();
            chat_user_name = (String) ((DataSnapshot)i.next()).getValue();
            //chat_msg.append(chat_user_name+" : "+inchat_msg+"\n");
            arraymsgs.add(chat_user_name+" : "+inchat_msg);
            arrayAdaptermsgs =new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arraymsgs);
            displaymsgs.setAdapter(arrayAdaptermsgs);
        }
    }
}
