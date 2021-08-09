package org.techtown.Jindani;

import android.content.Intent;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class AdapterQnaList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public ArrayList<QnaModel> list = new ArrayList<QnaModel>();
    private RecyclerView qnaList;

    public AdapterQnaList(RecyclerView qnalist) {
        this.qnaList = qnalist;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_qna,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((AdapterQnaList.ViewHolder) holder).bind(this.list.get(position));
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public void addQToList(QnaModel qna){
        this.list.add(qna);
        this.notifyDataSetChanged();
        //채팅 추가될때마다 리사이클러뷰가 제일 하단으로 가게함
        qnaList.scrollToPosition(getItemCount()-1);
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(QnaModel qna){
            TextView title = itemView.findViewById(R.id.title);
            TextView content = itemView.findViewById(R.id.content);

            title.setText(qna.question_title);
            content.setText(qna.question_content);
        }
    }
}
