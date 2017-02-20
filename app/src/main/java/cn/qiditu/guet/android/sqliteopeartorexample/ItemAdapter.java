package cn.qiditu.guet.android.sqliteopeartorexample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import cn.qiditu.signalslot.signals.Signal0;
import cn.qiditu.signalslot.signals.Signal1;

class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    private Context context;
    private SQLiteDatabase db;

    ItemAdapter(Context context) {
        super();
        this.context = context;
        db = new DataHelper(context).getWritableDatabase();
        updateData();
    }

    ArrayList<Data> data = new ArrayList<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(context)
                            .inflate(R.layout.recycler_view_item, parent, false));
    }

    final Signal1<Integer> itemLongClick = new Signal1<>(this);
    final Signal1<RecyclerView.ViewHolder> orderLongClick = new Signal1<>(this);

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                itemLongClick.emit(position);
                return true;
            }
        });
        holder.imageOrder.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                orderLongClick.emit(holder);
                return true;
            }
        });
        holder.tvUsername.setText(data.get(position).name);
        holder.tvInterest.setText(data.get(position).interest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemCount() {
        return data.size();
    }

    void refresh() {
        DiffUtilHelper helper = new DiffUtilHelper();
        updateData();
        helper.notifyAdapter();
    }

    final Signal0 updateFinished = new Signal0(this);

    private void updateData() {
        data.clear();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from " + DataHelper.tableName, null);
            while (cursor.moveToNext()) {
                int idIndex = cursor.getColumnIndexOrThrow("ID");
                int nameIndex = cursor.getColumnIndexOrThrow("Name");
                int interestIndex = cursor.getColumnIndexOrThrow("Interest");
                data.add(new Data(cursor.getInt(idIndex),
                        cursor.getString(nameIndex),
                        cursor.getString(interestIndex)));
            }
        }
        finally {
            if(cursor != null) {
                cursor.close();
            }
            updateFinished.emit();
        }
    }

    void insert(@NonNull String name, @NonNull String interest) {
        DiffUtilHelper helper = new DiffUtilHelper();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", name);
        contentValues.put("Interest", interest);
        long id = db.insert(DataHelper.tableName, null, contentValues);
        data.add(new Data(id, name, interest));
        helper.notifyAdapter();
//        Cursor cursor = db.query(DataHelper.tableName,
//                                    new String[]{DataHelper.id},
//                                    DataHelper.id + "=?",
//                                    new String[]{String.valueOf(id)},
//                                    null, null, null);
//        if(cursor.moveToNext()) {
//            data.add(new Data(cursor.getLong(0), name, interest));
//            helper.notifyAdapter();
//        }
//        cursor.close();
    }

    void remove(int position) {
        DiffUtilHelper helper = new DiffUtilHelper();
        db.delete(DataHelper.tableName,
                    DataHelper.id + "=?",
                    new String[]{String.valueOf(data.get(position).id)});
        data.remove(position);
        helper.notifyAdapter();
    }

    private static class Data {

        private long id;
        private String name;
        private String interest;

        private Data(long id, @NonNull String name, @NonNull String interest) {
            this.id = id;
            this.name = name;
            this.interest = interest;
        }

    }

    private static class Callback extends DiffUtil.Callback {

        private ArrayList<Data> oldData;
        private ArrayList<Data> newData;

        Callback(@NonNull ArrayList<Data> oldData, @NonNull ArrayList<Data> newData) {
            this.oldData = oldData;
            this.newData = newData;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getOldListSize() {
            return oldData.size();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getNewListSize() {
            return newData.size();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldData.get(oldItemPosition).id == newData.get(newItemPosition).id;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            Data oldD =  oldData.get(oldItemPosition);
            Data newD = newData.get(newItemPosition);
            return oldD.name.equals(newD.name) && oldD.interest.equals(newD.interest);
        }
    }

    private class DiffUtilHelper {

        private ArrayList<Data> oldData;

        private DiffUtilHelper() {
            oldData = new ArrayList<>(ItemAdapter.this.data);
        }

        private void notifyAdapter() {
            DiffUtil.calculateDiff(new Callback(oldData, ItemAdapter.this.data))
                    .dispatchUpdatesTo(ItemAdapter.this);
        }

    }

}
