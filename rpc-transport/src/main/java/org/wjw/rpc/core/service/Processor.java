package org.wjw.rpc.core.service;

import org.wjw.rpc.core.command.RequestCommand;
import org.wjw.rpc.core.command.Response;

import java.lang.reflect.InvocationTargetException;

/**
 * @description:
 * @author: wang.jianwen
 * @create: 2020-09-29 17:19
 **/
public interface Processor {

    /**
     * 方法执行
     * @param command
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @return
     */
    Response handler(RequestCommand command) throws InvocationTargetException, IllegalAccessException;
}
