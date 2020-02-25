//package com.example.tag;
//
//import androidx.annotation.Nullable;
//import androidx.recyclerview.selection.ItemKeyProvider;
//
//public class MyItemKeyProvider extends ItemKeyProvider {
//    private final List<Item> itemList;
//
//    public MyItemKeyProvider(int scope, List<Item> itemList) {
//        super(scope);
//        this.itemList = itemList;
//    }
//
//    @Nullable
//    @Override
//    public Object getKey(int position) {
//        return itemList.get(position);
//    }
//
//    @Override
//    public int getPosition(@NonNull Object key) {
//        return itemList.indexOf(key);
//    }
//}