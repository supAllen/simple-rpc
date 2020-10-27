package org.wjw.rpc.manager.impl;

import org.wjw.rpc.manager.RecordException;

/**
 * 默认实现是打到es上去
 */
public class DefaultRecord implements RecordException {

    @Override
    public void record(String exceptionLog) {
        System.out.println(exceptionLog);
        // todo log to es
    }
}
