package org.wjw.rpc.center.loadbalance;

import org.apache.commons.collections.CollectionUtils;
import org.wjw.rpc.center.DefaultRegister;
import org.wjw.rpc.center.LoadBalanceStrategy;
import org.wjw.rpc.center.Register;

import java.security.SecureRandom;
import java.util.List;

public class RandBalance extends AbstractBalance implements LoadBalanceStrategy {

    private static final SecureRandom rd;
    private static final Register register;
    static {
        rd = new SecureRandom();
        register = new DefaultRegister();
    }

    @Override
    public String balance(String serviceUri) throws Exception {
        List<String> addrs = register.lookup(serviceUri);
        if (CollectionUtils.isEmpty(addrs)) {
            return null;
        }
        return addrs.get(rd.nextInt(addrs.size()));
    }
}
