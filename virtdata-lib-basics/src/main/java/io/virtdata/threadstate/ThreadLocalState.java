package io.virtdata.threadstate;

import java.util.HashMap;

/**
 * This provides common thread local instancing for sharing a thread local map across classes.
 */
public class ThreadLocalState {

    public static ThreadLocal<HashMap<String,Object>> tl_ObjectMap = ThreadLocal.withInitial(HashMap::new);

}
