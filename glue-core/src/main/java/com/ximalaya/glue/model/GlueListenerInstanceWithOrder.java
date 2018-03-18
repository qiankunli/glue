package com.ximalaya.glue.model;

import com.google.common.base.Objects;

public class GlueListenerInstanceWithOrder implements Comparable<GlueListenerInstanceWithOrder> {

    private Object instance;
    private int order;

    public GlueListenerInstanceWithOrder(Object instance, int order) {
        this.instance = instance;
        this.order = order;
    }

    public Object getInstance() {
        return instance;
    }

    public int getOrder() {
        return order;
    }

    /*
        按order排序
     */
    public int compareTo(GlueListenerInstanceWithOrder o) {
        int sub = this.order - o.getOrder();
        /*
            如果不相等，采用原有的顺序，否则，后put 的优先级低
         */
        return 0 == sub ? (Objects.equal(instance, o.getInstance()) ? 0 : 1) : sub;
    }
}
