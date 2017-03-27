package xyz.akshit.boostme;

import java.util.LinkedList;
import java.util.List;

import xyz.akshit.boostme.dummy.SharedDummyContent;

/**
 * Created by akshi on 3/26/2017.
 */

public class SharedDataStructure {

    static List<SharedDummyContent.SharedDummyItem> list;

    public static String hostIP;

    static {
        list = new LinkedList<>();
    }

    public static void addItem(SharedDummyContent.SharedDummyItem item){
        list.add(item);
    }

    public static List getList(){
        return list;
    }


}
