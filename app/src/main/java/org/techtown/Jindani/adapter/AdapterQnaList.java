package org.techtown.Jindani.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.Jindani.R;
import org.techtown.Jindani.listeners.OnQuestionItemClickListener;
import org.techtown.Jindani.models.QuestionModel;

import java.util.ArrayList;

public class AdapterQnaList extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnQuestionItemClickListener {

    public ArrayList<QuestionModel> list = new ArrayList<>();
    public ArrayList<QuestionModel> copyList = new ArrayList<>();
    private RecyclerView qnaList;

    public OnQuestionItemClickListener listener;

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
        ((AdapterQnaList.ViewHolder) holder).bind(list.get(position));
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

    public QuestionModel getQnaItem(int position){
        return list.get(position);
    }

    public Filter getFilter(){
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            ArrayList<QuestionModel> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(copyList);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(QuestionModel item : copyList){
                    if(item.getQuestion_title().toLowerCase().contains(filterPattern) ||
                            item.getQuestion_content().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            list.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };

    public void addQToList(QuestionModel qna){
        this.list.add(qna);
        this.copyList.add(qna);
        this.notifyDataSetChanged();
        //채팅 추가될때마다 리사이클러뷰가 제일 하단으로 가게함
        qnaList.scrollToPosition(getItemCount()-1);
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (listener != null){
                        listener.onItemClick(ViewHolder.this, v, pos);
                    }

                }
            });
        }

        public void bind(QuestionModel qna){
            TextView title = itemView.findViewById(R.id.title);
            TextView content = itemView.findViewById(R.id.content);

            title.setText(qna.getQuestion_title());
            content.setText(qna.getQuestion_content());
        }
    }

    public void setOnItemClickListener(OnQuestionItemClickListener listener) {
        this.listener = listener;
    }
}
