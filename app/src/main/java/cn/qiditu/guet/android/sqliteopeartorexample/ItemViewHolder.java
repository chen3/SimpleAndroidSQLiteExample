package cn.qiditu.guet.android.sqliteopeartorexample;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

class ItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.item_name)
    TextView tvUsername;
    @BindView(R.id.item_interest)
    TextView tvInterest;
    @BindView(R.id.icon_order)
    ImageView imageOrder;

    ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}
