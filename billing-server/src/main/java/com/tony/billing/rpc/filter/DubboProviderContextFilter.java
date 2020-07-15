package com.tony.billing.rpc.filter;

import com.tony.billing.constants.CommonConstants;
import com.tony.billing.util.UserIdContainer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.ListenableFilter;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;

import java.util.Map;

/**
 * @author jiangwenjie 2019/8/28
 */
@Slf4j
@Activate
public class DubboProviderContextFilter extends ListenableFilter {

    public DubboProviderContextFilter() {
        super.listener = new ExecuteListener();
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Map<String, String> attachments = RpcContext.getContext().getAttachments();
        if (attachments != null) {
            String userId = attachments.get(CommonConstants.USER_ID);
            if (StringUtils.isNotEmpty(userId)) {
                UserIdContainer.setUserId(Long.valueOf(userId));
            }
        }
        log.debug("invoke start");
        return invoker.invoke(invocation);
    }

    static class ExecuteListener implements Listener {
        @Override
        public void onResponse(Result appResponse, Invoker<?> invoker, Invocation invocation) {
            log.debug("invoke done");
            UserIdContainer.removeUserId();
        }

        @Override
        public void onError(Throwable t, Invoker<?> invoker, Invocation invocation) {
            log.debug("invoke error");
            UserIdContainer.removeUserId();
        }
    }
}
