package sms19.inapp.single.chatroom;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kitever.android.R;
import com.kitever.sendsms.fragments.SmsMailInterface;

import java.util.ArrayList;

import sms19.inapp.msg.imoze.EmojiconsFragment;
import sms19.inapp.msg.imozemodel.Emojicon;
import sms19.listview.newproject.model.FetchGroupDetails;
import sms19.listview.newproject.model.FetchSenderIDs;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BroadCastChatRoomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public  class BroadCastChatRoomFragment extends Fragment implements
        View.OnClickListener, MediaPlayer.OnCompletionListener,
        sms19.inapp.msg.imoze.EmojiconGridFragment.OnEmojiconClickedListener,
        EmojiconsFragment.OnEmojiconBackspaceClickedListener,
        sms19.listview.newproject.Home.ChatFragmentListener,
        View.OnLongClickListener, SmsMailInterface {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public BroadCastChatRoomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BroadCastChatRoomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BroadCastChatRoomFragment newInstance(String param1, String param2) {
        BroadCastChatRoomFragment fragment = new BroadCastChatRoomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_broad_cast_chat_room, container, false);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void ChangeFragment(ArrayList<FetchGroupDetails.GroupDetails> groupNameTagList, ArrayList<String> groupsArrayList) {

    }

    @Override
    public void setSendersId(ArrayList<FetchSenderIDs> sendersId) {

    }

    @Override
    public void Backpressed() {

    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {

    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {

    }

    @Override
    public void messagePrint(String packetId, String status) {

    }
}
