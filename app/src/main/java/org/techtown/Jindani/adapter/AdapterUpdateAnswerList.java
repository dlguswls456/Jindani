package org.techtown.Jindani.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.Jindani.R;
import org.techtown.Jindani.listeners.OnAnswerItemClickListener;
import org.techtown.Jindani.models.AnswerModel;

import java.util.ArrayList;

public class AdapterUpdateAnswerList extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnAnswerItemClickListener {

    public ArrayList<AnswerModel> list = new ArrayList<>();
    private RecyclerView ansList;

    public OnAnswerItemClickListener listener;

    public AdapterUpdateAnswerList(RecyclerView ansList) {
        this.ansList = ansList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_doctor_update_qna,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((AdapterUpdateAnswerList.ViewHolder) holder).bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if(listener != null){
            listener.onItemClick(holder,view,position);
        }
    }

    public AnswerModel getAnswerItem(int position){
        return list.get(position);
    }

    public void addAnsToList(AnswerModel ans){
        this.list.add(ans);
        this.notifyDataSetChanged();
        //채팅 추가될때마다 리사이클러뷰가 제일 하단으로 가게함
        ansList.scrollToPosition(getItemCount()-1);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (listener != null){
                        listener.onItemClick(AdapterUpdateAnswerList.ViewHolder.this, v, pos);
                    }

                }
            });
        }

        public void bind(AnswerModel ans){
            TextView date = itemView.findViewById(R.id.textView_updateAnsDate);
            TextView content = itemView.findViewById(R.id.textView_updateAnsContent);

            date.setText(ans.getAnswer_date());
            content.setText(ans.getAnswer_content());
        }
    }

    public void setOnItemClickListener(OnAnswerItemClickListener listener) {
        this.listener = listener;
    }
}
