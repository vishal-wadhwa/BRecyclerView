# BRecyclerView
<p>
BrecyclerView (or "Better" RecyclerView) is an attempt to make the RecyclerView more handy with added features of ListView.
<a href="https://developer.android.com/reference/android/widget/ListView.html">ListView</a> is a view group that displays a list of scrollable items and so is the <a href="https://developer.android.com/reference/android/support/v7/widget/RecyclerView.html">RecyclerView</a>. But, RecyclerView was introduced as an improvement to the ListView providing better performance than the latter. While RecyclerView showed promising results in terms of performance and efficiency, it lacked some of the basic features that ListView has like touch effect, dividers, ease to set multi select and action. 
<p>
In order to tackle this situation I made this library. It offers the following features:
<p>
1. Touch effect (ripple effect for supported apis otherwise normal touch effect)  
2. Multi Select and action (<a href="https://developer.android.com/guide/topics/ui/menus.html#CAB">Contextual Action Menu</a>)  
3. Swipe to remove  
4. Adding divider:
  * Height
  * Color

PS: Since, this is my first library, I cannot assure that it'll offer ultimate efficiency and performance but it is still worth a try. Report bugs, if any. :) 

To include this library into you project add this dependency to app build.gradle:

####Gradle  
<code>compile 'com.github.dark-escape:brecyclerview:1.0.2'</code>

####Maven
```
<dependency>
  <groupId>com.github.dark-escape</groupId>
  <artifactId>brecyclerview</artifactId>
  <version>1.0.2</version>
  <type>pom</type>
</dependency>
```
##Usage
It is very simple to use this library, you can use it the same way you use RecyclerView, just that your adapter must extend `BRecyclerView.Adapter`, your ViewHolder must extend `BRecyclerView.ViewHolder` and if you want to use ContextualActionMode, create another class that extends `BrecyclerView.ActionModeCallback` and send its instance in 2nd constructor of your `BRecyclerView.Adapter` (look down for reference):
#####1. TouchEffect
```
ViewHolder holder=new ViewHolder(view);
holder.setTouchEffect(Color.GRAY,Color.WHITE);
//holder.setBackground(new ColorDrawable(Color.BLUE)); --> Custom Background
//holder.setCheckedBackground(new ColorDrawable(Color.GREEN)); --> background when item is selected in multi select
```
#####2. MultiSelect and Action
######Step 1:
Extend class `ActionModeCallback` and implement `onActionItemClicked`.
######Step 2:
Very important to call `mode.finish()` at the end of each ActionItemClicked.
######Step 3:
If you are adding a delete button please follow the following method to remove items from ArrayList:

```
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
```
######Step 4:
Create instance of this class and send to second constructor of `BRecyclerView.Adapter` like this:
```
public MyAdapter(Context context, @NonNull BRecyclerView.ActionModeCallback modeCallBack, ArrayList<String> name) {
       super(context, modeCallBack);
       this.name = name;
}
```
######Step 5:
Do not forget to call `triggerActionMode(holder)` just before return in `onCreateViewHolder`.
######Step 6:
If you do not want your ActionMode Toolbar to look shitty add `<item name="actionModeBackground">@color/your_color</item>` to your activity theme at values/styles.xml

#####3. Swipe to remove
Use this:
`swipeToRemove(boolean,BRecyclerView);`
Next step is to update the list, that is to remove the item at that position and for this override `positionRemovedOnSwipe` and
remove that item from your ArrayList, e.g. `name.remove(pos);`

> Althought, swipe to remove is configured to adjust itself in conjunction with Multi-Select and Action but still it is **NOT** recommended to use them together.

#####4. Adding divider height and color:
Easy af. Add `app:dividerColor"` and `app:dividerHeight` attribute and done.
```
   <com.darkescape.brecyclerview.BRecyclerView
        android:layout_width="match_parent"
        android:id="@+id/c1"
        app:dividerColor="@color/colorAccent"
        app:dividerHeight="3dp"
        android:layout_height="wrap_content"/>
  ```
  
You can always look into the sample app for reference.
