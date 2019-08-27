package com.tony.billing.filters.wapper;

import org.apache.catalina.util.ParameterMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

/**
 * @author by TonyJiang on 2017/7/2.
 */
public class TokenServletRequestWrapper extends HttpServletRequestWrapper {

    private Map<String, String[]> params;

    public TokenServletRequestWrapper(HttpServletRequest request) {
        super(request);
        params = new ParameterMap<>();
        if (super.getParameterMap() != null && super.getParameterMap().size() != 0) {
            params.putAll(super.getParameterMap());
        }
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        if (super.getParameterMap() != null && super.getParameterMap().size() != 0) {
            params.putAll(super.getParameterMap());
        }
        return params;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(getParameterMap().keySet());
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = params.get(name);
        if (values != null) {
            int valuesLength = values.length;
            String[] encodeValues = new String[valuesLength];
            for (int i = 0; i < valuesLength; i++) {
                encodeValues[i] = xssEncode(values[i]);
            }
            return encodeValues;
        }
        return null;
    }

    public void addParameter(String name, String... value) {
        params.put(name, value);
    }

    @Override
    public String getParameter(String name) {
        String[] value = getParameterMap().get(name);
        return value == null ? null : xssEncode(value[0]);
    }

    @Override
    public String getHeader(String name) {
        String header = super.getHeader(name);
        return xssEncode(header);
    }

    /**
     * 将容易引起xss漏洞的半角字符直接替换成全角字符
     *
     * @param src
     * @return
     */
    private String xssEncode(String src) {
        if (StringUtils.isBlank(src)) {
            return src;
        }
        return StringUtils.trim(HtmlUtils.htmlEscape(src));
    }
}
