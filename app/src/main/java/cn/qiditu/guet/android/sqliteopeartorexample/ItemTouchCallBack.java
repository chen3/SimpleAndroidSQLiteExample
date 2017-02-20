package cn.qiditu.guet.android.sqliteopeartorexample;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.Collections;

class ItemTouchCallBack extends ItemTouchHelper.Callback {

    private ItemAdapter adapter;

    ItemTouchCallBack(ItemAdapter adapter) {
        this.adapter = adapter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder) {
        return ItemTouchCallBack.makeMovementFlags(
                        ItemTouchHelper.UP|ItemTouchHelper.DOWN,
                        ItemTouchHelper.START|ItemTouchHelper.END);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onMove(RecyclerView recyclerView,
                          RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        Collections.swap(adapter.data, fromPosition, toPosition);
        adapter.notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        adapter.remove(position);
    }
}
