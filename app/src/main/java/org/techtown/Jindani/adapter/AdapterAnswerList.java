package org.techtown.Jindani.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.Jindani.R;
import org.techtown.Jindani.listeners.OnQuestionItemClickListener;
import org.techtown.Jindani.models.AnswerModel;

import java.util.ArrayList;

public class AdapterAnswerList extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public ArrayList<AnswerModel> list = new ArrayList<>();
    private RecyclerView ansList;

    public AdapterAnswerList(RecyclerView ansList) {
        this.ansList = ansList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_answer,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((AdapterAnswerList.ViewHolder) holder).bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
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
        }

        public void bind(AnswerModel ans){
            TextView name = itemView.findViewById(R.id.doc_name);
            TextView dept = itemView.findViewById(R.id.doc_dept);
            TextView content = itemView.findViewById(R.id.doc_ans_content);

            name.setText(ans.getDocName());
            dept.setText(ans.getDocDept());
            content.setText(ans.getAnswer_content());
        }
    }

}
