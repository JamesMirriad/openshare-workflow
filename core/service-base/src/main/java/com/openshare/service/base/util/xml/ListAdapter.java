package com.openshare.service.base.util.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public final class ListAdapter extends XmlAdapter<ListAdapter.AdaptedArray, List<Object>> {
    
   public static class AdaptedArray {
        
       public Entry [] entry = null ;
 
   }
    
   public static class Entry {
        
       public Object value;
  
   }

   @Override
   public List<Object> unmarshal(AdaptedArray adaptedArray) throws Exception {
       List<Object> list = new ArrayList<Object>();
       if(adaptedArray.entry!=null){
	       for(Entry entry : adaptedArray.entry) {
	           list.add(entry.value);
	       }
       }
       return list;
   }

   @Override
   public AdaptedArray marshal(List<Object> list) throws Exception {
	   AdaptedArray adaptedList = new AdaptedArray();
       if(list!=null){
    	   adaptedList.entry = new Entry [list.size()];
	       for(int i = 0 ; i < list.size() ; i++) {
	           Entry entry = new Entry();
	           entry.value = list.get(i);
	           adaptedList.entry[i] = entry;
	       }
       }
       return adaptedList;
   }

}