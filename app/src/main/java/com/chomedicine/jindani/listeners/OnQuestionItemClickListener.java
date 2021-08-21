package com.chomedicine.jindani.listeners;

import android.view.View;

import com.chomedicine.jindani.adapter.AdapterQnaList;

public interface OnQuestionItemClickListener {
    public void onItemClick(AdapterQnaList.ViewHolder holder, View view, int position);
}
