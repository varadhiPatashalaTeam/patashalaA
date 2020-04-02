package in.varadhismartek.patashalaerp.DashboardModule.NoticeBoard;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.R;


public class NoticeBoardAdapter extends RecyclerView.Adapter<NoticeBoardViewHolder> {

    private ArrayList<NoticeBoardModel> noticeList;
    private Context mContext;
    private String recyclerTag;

    public NoticeBoardAdapter(ArrayList<NoticeBoardModel> noticeList, Context mContext, String recyclerTag){

        this.noticeList = noticeList;
        this.mContext = mContext;
        this.recyclerTag = recyclerTag;
    }

    @NonNull
    @Override
    public NoticeBoardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        switch (recyclerTag){

            case Constant.RV_NOTICEBOARD_ROW:
                return new NoticeBoardViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.notice_board_row, viewGroup, false));
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeBoardViewHolder holder, final int i) {

        switch (recyclerTag){

            case Constant.RV_NOTICEBOARD_ROW:

                String[] title  = noticeList.get(i).getTitle().split(" ");

                if (title.length == 1) {
                    char a = title[0].charAt(0);
                    holder.tv_shortName.setText(String.valueOf(a));

                } else if (title.length > 1) {
                    char a = title[0].charAt(0);
                    char b = title[1].charAt(0);
                    Log.d("CHAR", String.valueOf(a));
                    holder.tv_shortName.setText(String.valueOf(a) + b );
                }
                holder.tv_shortName.setAllCaps(true);

                if (noticeList.get(i).getTitle().length()>20){
                    holder.tv_title.setText(noticeList.get(i).getTitle().substring(0,20)+"...");
                }else {
                    holder.tv_title.setText(noticeList.get(i).getTitle());
                }

                if (noticeList.get(i).getMessage().length()>20){
                    holder.tv_message.setText(noticeList.get(i).getMessage().substring(0,20)+"...");
                }else {
                    holder.tv_message.setText(noticeList.get(i).getMessage());
                }

                holder.tv_dateView.setText(noticeList.get(i).getAddedDate());

                if(i == 0){
                    holder.ll_row.setBackgroundResource(R.color.white);

                }else if (i%2 != 0){
                    holder.ll_row.setBackgroundResource(R.color.trans_green);

                }else {
                    holder.ll_row.setBackgroundResource(R.color.white);

                }

                holder.ll_row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        NoticeBoardActivity noticeBoardActivity = (NoticeBoardActivity) mContext;
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("list", noticeList.get(i));
                        noticeBoardActivity.loadFragment(Constant.NOTICEBOARD_VIEW_FRAGMENT, bundle);
                    }
                });

                break;
        }
    }

    @Override
    public int getItemCount() {

        switch (recyclerTag){

            case Constant.RV_NOTICEBOARD_ROW:
                return noticeList.size();
        }

        return 0;
    }
}
