package org.techtown.Jindani.listeners;

import android.view.View;

import org.techtown.Jindani.adapter.AdapterQnaList;

public interface OnQuestionItemClickListener {
    public void onItemClick(AdapterQnaList.ViewHolder holder, View view, int position);
}
