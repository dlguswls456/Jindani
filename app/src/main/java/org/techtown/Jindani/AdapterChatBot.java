package org.techtown.Jindani;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterChatBot extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ChatModel> list = new ArrayList<ChatModel>();
    private RecyclerView rvChatList;

    //새로운 채팅 등록될 때마다 리사이클러뷰 위치 변경시켜야하므로 생성자로 리사이클러뷰 받아옴
    public AdapterChatBot(RecyclerView rvChatList) {
        this.rvChatList = rvChatList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        if (viewType == 0){ //사용자 답변
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_list,parent,false);
            return new UserViewHolder((ViewGroup) view);
        }
        else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bot_item_list,parent,false);
            return new BotViewHolder((ViewGroup) view);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) { //현재 뷰홀더에 채팅 내용 바인딩
        if(holder instanceof BotViewHolder){
            ((BotViewHolder) holder).bind(this.list.get(position));
        }
        else{
            ((UserViewHolder) holder).bind(this.list.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (!list.get(position).isBot) { // 사용자 답변일 때 0 리턴
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }


    public void addChatToList(ChatModel chat){
        this.list.add(chat);
        this.notifyDataSetChanged();
        //채팅 추가될때마다 리사이클러뷰가 제일 하단으로 가게함
        rvChatList.scrollToPosition(getItemCount()-1);
    }


    public class BotViewHolder extends RecyclerView.ViewHolder {

        public BotViewHolder(@NonNull ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.bot_item_list, itemView, false)); //봇 질문일 때 bot_item_list inflate
        }

        public void bind(ChatModel chat){
            TextView msg = itemView.findViewById(R.id.bot_msg);
            msg.setText(chat.chat);
        }
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        public UserViewHolder(@NonNull ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.user_item_list, itemView, false));
        }

        public void bind(ChatModel chat){
            TextView msg = itemView.findViewById(R.id.user_msg);
            msg.setText(chat.chat);
        }
    }
}
