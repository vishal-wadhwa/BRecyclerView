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
//theme change
#####3. Swipe to remove
Use this:
'ada.swipeToRemove(true,bView);' and done.
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
  
  //you can always see this app as a reference
