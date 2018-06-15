package io.virtdata.threadstate;

import java.util.HashMap;

public class ThreadLocalState {

    public static ThreadLocal<HashMap<String,Object>> tl_ObjectMap = ThreadLocal.withInitial(HashMap::new);
    public static ThreadLocal<HashMap<String,Long>> tl_LongMap = ThreadLocal.withInitial(HashMap::new);
    public static ThreadLocal<long[]> tl_longAry = ThreadLocal.withInitial(()->new long[10]);

}
