package com.example.eioms.teacher.ui.notice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.eioms.Bean;
import com.example.eioms.databinding.NoticeFragmentBinding;

import java.util.List;

public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<Bean> data;

    public MyItemRecyclerViewAdapter(List<Bean> items) {
        data = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(NoticeFragmentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = data.get(position);
        holder.title.setText(data.get(position).getTitle());
        holder.user.setText(data.get(position).getUsername());
        holder.time.setText(data.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;
        public final TextView user;
        public final TextView time;
        public Bean mItem;

        public ViewHolder(NoticeFragmentBinding binding) {
            super(binding.getRoot());
            time = binding.tvTime;
            user = binding.tvUser;
            title = binding.tvTitle;

            //点击事件
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mOnItemClickListener != null){
                        mOnItemClickListener.onRecyclerItemClick(getAdapterPosition());
                    }
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + title.getText() + "'";
        }

    }



    private OnRecyclerItemClickListener mOnItemClickListener;

    public void setRecyclerItemClickListener(OnRecyclerItemClickListener listener){
        mOnItemClickListener = listener;
    }

    public  interface OnRecyclerItemClickListener{
        void onRecyclerItemClick(int position);
    }
}