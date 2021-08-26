package com.chomedicine.jindani.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chomedicine.jindani.R;
import com.chomedicine.jindani.activities.WriteAnswerActivity;
import com.chomedicine.jindani.models.AnswerModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterAnswerList extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public ArrayList<AnswerModel> list = new ArrayList<>();
    private RecyclerView ansList;

    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference databaseReference;

    private Context context;

    public AdapterAnswerList(RecyclerView ansList, Context context) {
        this.ansList = ansList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        if (viewType == 1) { //해당 의사 답변
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_my_answer, parent, false);
            return new MyViewHolder(view);
        }else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_answer, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder){
            ((MyViewHolder) holder).bind(this.list.get(position));
        }
        else{
            ((ViewHolder) holder).bind(this.list.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(firebaseUser.getUid().equals(list.get(position).getDocId())){ //해당 의사일 때 1 리턴
            return 1;
        }else{
            return 0;
        }
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
//        this.notifyDataSetChanged();
        //채팅 추가될때마다 리사이클러뷰가 제일 하단으로 가게함
        ansList.scrollToPosition(getItemCount()-1);
    }

    public void check(){
        this.notifyDataSetChanged();
    }

    //해당 의사 답변
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(AnswerModel ans){
            TextView name = itemView.findViewById(R.id.doc_name);
            TextView dept = itemView.findViewById(R.id.doc_dept);
            TextView content = itemView.findViewById(R.id.doc_ans_content);
            TextView date = itemView.findViewById(R.id.doc_date);

            name.setText(ans.getDocName());
            dept.setText(ans.getDocDept());
            content.setText(ans.getAnswer_content());
            date.setText(ans.getAnswer_date());

            Button btn_edit = itemView.findViewById(R.id.btn_edit);
            Button btn_delete = itemView.findViewById(R.id.btn_delete);

            //수정 버튼
            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WriteAnswerActivity.class);
                    intent.putExtra("AnswerModel", ans);
                    context.startActivity(intent);
                }
            });

            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteDialog(ans.getAnswerId());
                }
            });
        }
    }

    //다른 의사 답변
    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(AnswerModel ans){
            TextView name = itemView.findViewById(R.id.doc_name);
            TextView dept = itemView.findViewById(R.id.doc_dept);
            TextView content = itemView.findViewById(R.id.doc_ans_content);
            TextView date = itemView.findViewById(R.id.doc_date);

            name.setText(ans.getDocName());
            dept.setText(ans.getDocDept());
            content.setText(ans.getAnswer_content());
            date.setText(ans.getAnswer_date());
        }
    }

    //답변 삭제 대화창
    public void showDeleteDialog(String aId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("답변을 삭제하시겠습니까?");
        builder.setCancelable(false);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //해당 답변 삭제
                databaseReference = FirebaseDatabase.getInstance().getReference().child("JindaniApp").child("Answer").child(aId);
                databaseReference.removeValue();

                dialog.dismiss();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

}
