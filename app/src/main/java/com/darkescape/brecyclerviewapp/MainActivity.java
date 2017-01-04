package com.darkescape.brecyclerviewapp;

import android.content.Intent;
import android.support.annotation.NonNull;
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
    MyAdapter ada;
    BRecyclerView bView;
    ArrayList<String> names = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for (int i = 0; i < 20; i++) {
            names.add("Name "+(i+1));
        }

        bView= (BRecyclerView) findViewById(R.id.view);
        bView.setLayoutManager(new LinearLayoutManager(this));
        ada=new MyAdapter(this,new MultiSelect(R.menu.cam_menu),names);
        ada.swipeToRemove(true,bView);
        bView.setAdapter(ada);
    }

    class MyAdapter extends BRecyclerView.Adapter<MyAdapter.ViewHolder> {

        class ViewHolder extends BRecyclerView.ViewHolder {

            private TextView tv;
            public ViewHolder(View itemView) {
                super(itemView);
                tv= (TextView) itemView.findViewById(R.id.tv);
            }
        }

        private ArrayList<String > name;

        public MyAdapter(Context context, @NonNull BRecyclerView.ActionModeCallback modeCallBack, ArrayList<String> name) {
            super(context, modeCallBack);
            this.name = name;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            super.onBindViewHolder(holder, position);
            holder.tv.setText(name.get(position));
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= getLayoutInflater().inflate(R.layout.card_view,parent,false);
            ViewHolder holder=new ViewHolder(view);
            holder.setTouchEffect(Color.GRAY,Color.WHITE);
            //holder.setBackground(new ColorDrawable(Color.WHITE)); //--> Custom Background
            //holder.setCheckedBackground(new ColorDrawable(Color.GREEN)); //--> background when card_view is selected in multi select
            triggerActionMode(holder);
            return holder;
        }

        @Override
        public void positionRemovedOnSwipe(int pos) {
            super.positionRemovedOnSwipe(pos);
            name.remove(pos);
        }

        @Override
        public int getItemCount() {
            return name.size();
        }

    }

    class MultiSelect extends BRecyclerView.ActionModeCallback {

        MultiSelect(int menuResId) {
            super(menuResId);
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId()==R.id.del) {
                //perform action
                int[] selections=ada.getSelectedIds();
                ArrayList<String> toRemoveName=new ArrayList<>();
                for (int i = 0; i < selections.length; i++) {
                    toRemoveName.add(names.get(selections[i]));
                }

                for (String nm :
                        toRemoveName) {
                    names.remove(nm);
                }

                mode.finish(); //very important
                return true;
            } else if (item.getItemId()==R.id.share) {
                //just an example no app takes an arraylist of strings
                int[] selections=ada.getSelectedIds();
                ArrayList<String> toShare=new ArrayList<>();
                for (int i = 0; i < selections.length; i++) {
                    toShare.add(names.get(i));
                }
                Intent share=new Intent(Intent.ACTION_SEND_MULTIPLE);
                share.putStringArrayListExtra(Intent.EXTRA_TEXT,toShare);
                share.setType("text/plain");
                startActivity(Intent.createChooser(share,"Select:"));
                mode.finish();
                return true;
            }
            return false;
        }

    }
}
