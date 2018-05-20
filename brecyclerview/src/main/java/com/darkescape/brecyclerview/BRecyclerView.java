package com.darkescape.brecyclerview;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Checkable;

import java.util.Arrays;

/**
 * @author dark-escape
 * @version v1.0
 *          Created by dark-escape on 01-Oct-16.
 */
public class BRecyclerView extends RecyclerView {

    public BRecyclerView(Context context) {
        super(context);
    }

    public BRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.BRecyclerView);
        try {
            float ht = array.getDimension(R.styleable.BRecyclerView_dividerHeight, 0);
            int clr = array.getColor(R.styleable.BRecyclerView_dividerColor, Color.GRAY);
            DisplayMetrics metrics = new DisplayMetrics();
            ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int wt = metrics.widthPixels;
            ItemDecoration border = new ItemDecoration(getDivider(ht, clr, wt));
            addItemDecoration(border);
        } finally {
            array.recycle();
        }
    }

    public BRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.BRecyclerView);
        try {
            float ht = array.getDimension(R.styleable.BRecyclerView_dividerHeight, 3);
            int clr = array.getColor(R.styleable.BRecyclerView_dividerColor, Color.GRAY);
            DisplayMetrics metrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int wt = metrics.widthPixels;
            ItemDecoration border = new ItemDecoration(getDivider(ht, clr, wt));
            addItemDecoration(border);
        } finally {
            array.recycle();
        }
    }


    /**
     * This class is used to add divider which usually not seen in a normal RecyclerView.
     * You may not use it as you can set divider properties in your xml file.
     */
    public class ItemDecoration extends RecyclerView.ItemDecoration {
        private GradientDrawable mDivider;

        public ItemDecoration(GradientDrawable mDivider) {
            super();
            this.mDivider = mDivider;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
            super.getItemOffsets(outRect, view, parent, state);
            if (parent.getChildAdapterPosition(view) == 0) {
                return;
            }
            outRect.top = mDivider.getIntrinsicHeight();
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, State state) {
            super.onDraw(c, parent, state);
            int childCount = parent.getChildCount();
            int lDiv = parent.getPaddingLeft();
            int rDiv = parent.getWidth() - parent.getPaddingRight();

            for (int i = 0; i < childCount - 1; i++) {
                View child = parent.getChildAt(i);
                RecyclerView.LayoutParams params = (LayoutParams) child.getLayoutParams();
                int tDiv = child.getBottom() + params.bottomMargin;
                int bDiv = tDiv + mDivider.getIntrinsicHeight();
                mDivider.setBounds(lDiv, tDiv, rDiv, bDiv);
                mDivider.draw(c);
            }
        }
    }


    /**
     * BRecyclerView.ViewHolder enables checking/un-checking
     * of a view when used in conjunction with {@link ActionModeCallback}. It has methods to
     * set different backgrounds under different situations.
     */
    abstract public static class ViewHolder extends RecyclerView.ViewHolder implements Checkable {
        private boolean mSelected;
        private Drawable background;
        private Drawable checkedBackground;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            mSelected = false;
            background = itemView.getBackground();
            checkedBackground = new ColorDrawable(Color.parseColor("#664d90fe"));
        }

        @Override
        public void setChecked(boolean checked) {
            mSelected = checked;
            if (checked) {
                itemView.setBackground(checkedBackground);
            } else itemView.setBackground(background);
        }


        /**
         * This method allows to set a custom background to the provided itemView in the
         * {@link #ViewHolder(View)}.
         *
         * @param background Drawable to be set as background.
         */
        public void setBackground(Drawable background) {
            this.background = background;
        }

        /**
         * This method adds a ripple effect of a color along with the background
         * color of your choice. DO NOT SEND {@link Color#TRANSPARENT}. You can send
         * {@link Color#WHITE} if you want no background.
         *
         * @param pressed Ripple color on pressing the view.
         * @param back    Background color.
         */
        public void setTouchEffect(@ColorInt int pressed, @ColorInt int back) {
            background = getTouchEffect(pressed, back);
        }

        /**
         * This method sets to background to change when ActionModeCallback or Multi-Select is
         * activated.
         *
         * @param checkedBackground Drawable to be set when ActionModeCallback is activated.
         */
        public void setCheckedBackground(Drawable checkedBackground) {
            this.checkedBackground = checkedBackground;
        }

        @Override
        public boolean isChecked() {
            return mSelected;
        }

        @Override
        public void toggle() {
            mSelected = !mSelected;
            setChecked(mSelected);
        }
    }


    /**
     * This class incorporates methods to trigger ActionMode and swipe to remove.
     * You must call {@link #triggerActionMode(ViewHolder)} in the <code>onCreateViewHolder(ViewGroup, int)</code>
     * to enable multi-select along with the overloaded {@link Adapter} constructor.
     * <p>
     * <code>
     * triggerActionMode(holder);
     * </code>
     * </p>
     *
     * @param <VH> ViewHolder extending {@link ViewHolder}
     */
    abstract public static class Adapter<VH extends ViewHolder> extends RecyclerView.Adapter<VH>
            implements ActionModeCallback.ActionModeDestroyListener, SwipeDragCallback {
        private SwipeDragCallback swipeCallback;
        private SparseBooleanArray booleanArray;
        private Context context;
        private ActionMode mode;
        private BRecyclerView bView;
        private ActionModeCallback modeCallBack;
        private ItemTouchHelper helper;
        private boolean isSwipeRemoveEnabled;

        /**
         * Simple Constructor if ActionMode is not needed.
         */
        public Adapter() {
            isSwipeRemoveEnabled = false;
            mode = null;
            modeCallBack = null;
            booleanArray = new SparseBooleanArray();
        }


        /**
         * Constructor to be used when ActionMode is required.
         *
         * @param context      The context of your activity.
         * @param modeCallBack Instance of class extending {@link ActionModeCallback} where you have
         *                     defined various actions, that is, what happens when a button is clicked
         *                     on action bar.
         */
        public Adapter(Context context, @NonNull ActionModeCallback modeCallBack) {
            this.context = context;
            isSwipeRemoveEnabled = false;
            this.modeCallBack = modeCallBack;
            booleanArray = new SparseBooleanArray();
            modeCallBack.setActionModeListener(this);
        }

        /**
         * Returns the count of items selected.
         *
         * @return size of items selected items
         */
        public int getSelectedItemCount() {
            return booleanArray.size();
        }


        /**
         * Method to be called inside <code>onCreateViewHolder(ViewGroup, int)</code> so as to set up
         * listeners for initiating action mode at each ViewHolder.
         *
         * @param holder Instance of {@link ViewHolder} on which listeners are to be set
         */
        public void triggerActionMode(final VH holder) {

            if (modeCallBack != null) {

                holder.itemView.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        if (mode == null) {
                            removeOnSwipe(null);
                            mode = ((AppCompatActivity) context).startSupportActionMode(modeCallBack);
                            changeHolder(holder);
                        }
                        return true;
                    }
                });

                holder.itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mode != null) {
                            changeHolder(holder);
                        }
                    }
                });
            }
        }


        /**
         * Override this method manually.
         */
        @Override
        public void onBindViewHolder(VH holder, int position) {
            if (booleanArray.get(holder.getAdapterPosition()))
                holder.setChecked(true);
            else
                holder.setChecked(false);
        }


        /**
         * Method to enable or disable swipe to remove functionality at your whim.
         *
         * @param enable true to enable and false to disable swipe.
         * @param bView  Instance of {@link BRecyclerView} on which swipe functionality is to be set.
         *               It can be null when enable is set to false.
         */
        public void swipeToRemove(boolean enable, @Nullable BRecyclerView bView) {
            this.bView = bView;
            isSwipeRemoveEnabled = enable;
            if (enable && bView != null) removeOnSwipe(bView);
            else helper = null;
        }

        private void removeOnSwipe(BRecyclerView bView) {
            swipeCallback = this;
            helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    int pos = viewHolder.getAdapterPosition();
                    if (mode != null) adjustMultiSelectWithSwipe(pos);
                    notifyItemRemoved(pos);
                    swipeCallback.positionRemovedOnSwipe(pos);
                }
            });
            helper.attachToRecyclerView(bView);
        }


        private void adjustMultiSelectWithSwipe(int pos) {
            if (booleanArray.get(pos)) changeSelectionList(pos);
            for (int i = 0; i < booleanArray.size(); i++) {
                if (booleanArray.valueAt(i) && booleanArray.keyAt(i) > pos) {
                    int posi = booleanArray.keyAt(i);
                    booleanArray.delete(posi);
                    booleanArray.put(posi - 1, true);
                }
            }
        }

        /**
         * Override this method to get the position of recently remove item using swipe.
         *
         * @see SwipeDragCallback#positionRemovedOnSwipe(int)
         */
        @Override
        public void positionRemovedOnSwipe(int pos) {

        }

        private void changeHolder(VH holder) {
            changeSelectionList(holder.getAdapterPosition());
            holder.toggle();
        }


        @Override
        public void deselectAll() {
            clearSelection();
        }

        /**
         * This method is used to clear the list of selected items which is certainly what you want
         * in case delete is clicked. You can avoid calling it for e.g. in case of share.
         * No need to call this method separately just call mode.finish() before return statement
         * in <code>ActionModeCallback#onActionItemClicked(ActionMode, MenuItem)</code>.
         */
        public void clearSelection() {
            mode = null;
            booleanArray.clear();
            if (isSwipeRemoveEnabled) removeOnSwipe(bView);
            notifyDataSetChanged();
        }


        /**
         * This function returns an integer array containing the list of selected positions.
         * These are the positions in the ArrayList on which a certain action is to be performed.
         * If they are to be deleted please find objects corresponding to each positions as shown
         * in the sample app otherwise you'll end up deleting wrong items because on removing a
         * single object based on position, the positions of other objects will change causing
         * improper or deletions or even crashes.
         *
         * @return integer array containing positions
         */
        public int[] getSelectedIds() {
            int[] selectedPos = new int[booleanArray.size()];
            for (int i = 0; i < booleanArray.size(); i++) {
                if (booleanArray.valueAt(i))
                    selectedPos[i] = booleanArray.keyAt(i);
            }
            return selectedPos;
        }

        private void changeSelectionList(int pos) {
            if (!booleanArray.get(pos)) {
                booleanArray.put(pos, true);
            } else {
                booleanArray.delete(pos);
                if (getSelectedItemCount() == 0) {
                    mode.finish();
                    clearSelection();
                }
            }
            if (mode != null) mode.setTitle(getSelectedItemCount() + " selected");
        }
    }

    private static Drawable getTouchEffect(int pressed, int normal) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new RippleDrawable(ColorStateList.valueOf(pressed), getRippleMask(normal), null);

        } else return getStateListDrawable(pressed, normal);
    }


    /**
     * Helper class for Multi Select Action. This class must be extended to provide the menu
     * resource file and the action to be performed.
     * Also, for the action bar to take the primary color add actionModeBackground to theme inside
     * values/style.xml
     */
    public abstract static class ActionModeCallback implements ActionMode.Callback {
        private int menuResId;
        private ActionModeDestroyListener listener;

        private void setActionModeListener(ActionModeDestroyListener listener) {
            this.listener = listener;
        }

        /**
         * This constructor must be supplied with the menu resource file to be inflated.
         *
         * @param menuResId Id of menu resource file
         */
        public ActionModeCallback(@MenuRes int menuResId) {
            this.menuResId = menuResId;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(menuResId, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (listener != null) listener.deselectAll();
        }

        private interface ActionModeDestroyListener {
            void deselectAll();
        }
    }

    private GradientDrawable getDivider(final float ht, int color, int wt) {
        GradientDrawable mDivider;
        mDivider = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{color, color});
        mDivider.setShape(GradientDrawable.RECTANGLE);
        mDivider.setSize(wt, (int) ht);
        mDivider.setCornerRadius(4);
        return mDivider;
    }

    private static Drawable getRippleMask(int color) {
        float[] outerRadii = new float[8];
        Arrays.fill(outerRadii, 3);

        RoundRectShape s = new RoundRectShape(outerRadii, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(s);
        shapeDrawable.getPaint().setColor(color);
        return shapeDrawable;
    }

    private static ColorDrawable getColorDrawable(int color) {
        return new ColorDrawable(color);
    }

    private static StateListDrawable getStateListDrawable(int pressedColor, int normalColor) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_pressed}, getColorDrawable(pressedColor));
        drawable.addState(new int[]{android.R.attr.state_focused}, getColorDrawable(pressedColor));
        drawable.addState(new int[]{android.R.attr.state_activated}, getColorDrawable(pressedColor));
        drawable.addState(new int[]{}, getColorDrawable(normalColor));
        return drawable;
    }


    /**
     * Interface that provides the position that is removed on swipe.
     * Override the {@link #positionRemovedOnSwipe(int)} method in {@link Adapter} to
     * get the position of last item removed using swipe.
     */
    public interface SwipeDragCallback {
        /**
         * Gives the position of last item removed using swipe.
         * Use this function to update your ArrayList.
         *
         * @param pos The removed position.
         */
        void positionRemovedOnSwipe(int pos);
    }
}
