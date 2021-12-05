package com.tony.billing.rpc.filter;

import com.tony.billing.constants.CommonConstants;
import com.tony.billing.util.UserIdContainer;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jiangwenjie 2019/8/28
 */
@Activate
@Slf4j
public class DubboConsumerContextFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        if (UserIdContainer.isSet()) {
            Map<String, String> attachments = new HashMap<>(16);
            attachments.put(CommonConstants.USER_ID, String.valueOf(UserIdContainer.getUserId()));
            log.debug("设置RPC参数user_id:{}", UserIdContainer.getUserId());
            RpcContext.getContext().setAttachments(attachments);
        }
        return invoker.invoke(invocation);
    }
}
