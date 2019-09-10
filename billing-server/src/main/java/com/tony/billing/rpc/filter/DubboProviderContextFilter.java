package com.tony.billing.rpc.filter;

import com.tony.billing.constants.CommonConstants;
import com.tony.billing.util.UserIdContainer;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;

import java.util.Map;

/**
 * @author jiangwenjie 2019/8/28
 */
@Activate
public class DubboProviderContextFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Map<String, String> attachments = RpcContext.getContext().getAttachments();
        if (attachments != null) {
            String userId = attachments.get(CommonConstants.USER_ID);
            if (StringUtils.isNotEmpty(userId)) {
                UserIdContainer.setUserId(Long.valueOf(userId));
            }
        }
        return invoker.invoke(invocation);
    }
}
