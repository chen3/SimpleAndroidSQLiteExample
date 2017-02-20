package cn.qiditu.guet.android.sqliteopeartorexample;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qiditu.signalslot.slots.Slot0;
import cn.qiditu.signalslot.slots.Slot1;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initRecyclerView();
        this.setSupportActionBar(toolbar);
        swipeRefreshLayout.setOnRefreshListener(
            new SwipeRefreshLayout.OnRefreshListener() {
                /**
                 * {@inheritDoc}
                 */
                @Override
                public void onRefresh() {
                    MainActivity.this.adapterRefresh();
                }
            }
        );
    }

    private void initRecyclerView() {
        adapter = new ItemAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        final ItemTouchHelper helper =
                        new ItemTouchHelper(new ItemTouchCallBack(adapter));
        helper.attachToRecyclerView(recyclerView);
        adapter.itemLongClick.connect(new Slot1<Integer>() {
            @Override
            public void accept(@Nullable Integer integer) {
                if(integer != null) {
                    recyclerViewItemLongClicked(integer);
                }
            }
        });
        adapter.orderLongClick.connect(new Slot1<RecyclerView.ViewHolder>() {
            @Override
            public void accept(@Nullable RecyclerView.ViewHolder viewHolder) {
                helper.startDrag(viewHolder);
            }
        });
    }

    void recyclerViewItemLongClicked(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.confirm_delete);
        builder.setMessage(R.string.delete_display_content);
        builder.setIcon(R.drawable.delete);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            /**
             * {@inheritDoc}
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter.remove(position);
                Snackbar.make(coordinatorLayout, R.string.delete_over,
                                Snackbar.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private void adapterRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        adapter.updateFinished.connect(new Slot0() {
            @Override
            public void accept() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1);
        adapter.refresh();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.fab_refresh)
    void onFabRefreshClick(@NonNull View view) {
        adapterRefresh();
    }

//    public final int inputRequestCode = 1;
    public static final int inputResultCode = 1;

    @SuppressWarnings("unused")
    @OnClick(R.id.fab_add)
    void onFabAddClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.add);
        builder.setIcon(R.drawable.add);
        final View inputView = LayoutInflater.from(this).inflate(R.layout.activity_input, null);
        builder.setView(inputView);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            /**
             * {@inheritDoc}
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText etUsername = (EditText)inputView.findViewById(R.id.username);
                EditText etInterest = (EditText)inputView.findViewById(R.id.interest);
                String username = etUsername.getText().toString();
                String interest = etInterest.getText().toString();
                adapter.insert(username, interest);
                Snackbar.make(coordinatorLayout, R.string.add_over,
                                Snackbar.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
//        startActivityForResult(new Intent(this, InputActivity.class), inputRequestCode);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.menu_refresh: {
//
//            } break;
//            default: {
//                return super.onOptionsItemSelected(item);
//            }
//        }
//        return true;
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(requestCode != inputRequestCode) {
//            return;
//        }
//        switch (resultCode) {
//            case inputResultCode: {
//                String username = data.getStringExtra(DataHelper.name);
//                String interest = data.getStringExtra(DataHelper.interest);
//                adapter.insert(username, interest);
//            } break;
//        }
//    }

}
