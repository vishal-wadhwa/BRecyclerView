package com.darkescape.brecyclerviewapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.darkescape.brecyclerview.BRecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private BRecyclerView bview;
    private MyAdapter ada;
    ArrayList<String> names = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.darkescape.brecyclerviewapp.R.layout.activity_main);

        for (int i = 0; i < 20; i++) {
            names.add(i, "Name " + (i+1));
        }
        bview = (BRecyclerView) findViewById(com.darkescape.brecyclerviewapp.R.id.view);
        ada = new MyAdapter(names, this, new MultiSelect(com.darkescape.brecyclerviewapp.R.menu.cam_menu));
        bview.setLayoutManager(new LinearLayoutManager(this));
        ada.swipeToRemove(true, bview);
        bview.setAdapter(ada);
    }

    class MyAdapter extends BRecyclerView.Adapter<MyAdapter.ViewHolder> {

        ArrayList<String> list;

        MyAdapter(ArrayList<String> list, Context context, BRecyclerView.ActionModeCallback modeCallBack) {
            super(context, modeCallBack);
            this.list = list;
        }


        class ViewHolder extends BRecyclerView.ViewHolder {

            private TextView textView;

            ViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(com.darkescape.brecyclerviewapp.R.id.tv);
            }

        }

        @Override
        public void positionRemovedOnSwipe(int pos) {
            super.positionRemovedOnSwipe(pos);
            list.remove(pos);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(com.darkescape.brecyclerviewapp.R.layout.item, parent, false);
            view.setClickable(true);
            ViewHolder holder=new ViewHolder(view);
            holder.setTouchEffect(Color.YELLOW,Color.WHITE);
            triggerActionMode(holder);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            super.onBindViewHolder(holder,position);
            holder.textView.setText(list.get(position));
        }


        @Override
        public int getItemCount() {
            return list.size();
        }


    }

    class MultiSelect extends BRecyclerView.ActionModeCallback {


        MultiSelect(int menuResId) {
            super(menuResId);
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            if (item.getItemId() == com.darkescape.brecyclerviewapp.R.id.del) {
                ArrayList<String > toRemove=new ArrayList<>();
                int[] items=ada.getSelectedIds();
                for (int i = 0; i < items.length; i++) {
                    toRemove.add(names.get(items[i]));
                }
                for (String nm:
                        toRemove) {
                    names.remove(nm);
                }
                mode.finish();
                return true;
            }
            return false;
        }
    }
}
