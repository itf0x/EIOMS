package com.example.eioms.admin.ui.account;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eioms.Data;
import com.example.eioms.databinding.CountFragmentBinding;

import java.util.List;

public class MyAccountRecyclerViewAdapter extends RecyclerView.Adapter<MyAccountRecyclerViewAdapter.ViewHolder> {

    private final List<Data> dataList;

    public MyAccountRecyclerViewAdapter(List<Data> items) {
        dataList = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(CountFragmentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = dataList.get(position);
        holder.name.setText(dataList.get(position).getName());
        switch (dataList.get(position).getAuthority()){
            case "0":
                holder.auth.setText("学生信息员");
                break;
            case "1":
                holder.auth.setText("教学督导人员");
                break;
            case "2":
                holder.auth.setText("教学管理人员");
                break;
            case "3":
                holder.auth.setText("管理员");
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView name;
        public final TextView auth;
        public Data mItem;

        public ViewHolder(CountFragmentBinding binding) {
            super(binding.getRoot());
            name = binding.tvUsername;
            auth = binding.tvAuth;

            //点击事件
            name.setOnClickListener(view -> {
                if(mOnAccountClickListener != null){
                    mOnAccountClickListener.onRecyclerAccountClick(getAdapterPosition());
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + name.getText() + "'";
        }
    }

    private OnRecyclerAccountClickListener mOnAccountClickListener;

    public void setRecyclerAccountClickListener(OnRecyclerAccountClickListener listener){
        mOnAccountClickListener = listener;
    }

    public  interface OnRecyclerAccountClickListener{
        void onRecyclerAccountClick(int position);
    }
}