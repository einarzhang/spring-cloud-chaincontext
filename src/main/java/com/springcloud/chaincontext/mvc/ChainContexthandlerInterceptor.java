package com.springcloud.chaincontext.mvc;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.springcloud.chaincontext.ChainContextProperties;
import com.springcloud.chaincontext.ChainContextHolder;
import com.springcloud.chaincontext.parser.IRequestKeyParser;

/**
 * The handler interceptor of chaincontext, use to set chaincontext with request headers
 * 
 * @author einarzhang
 * @email einarzhang@gmail.com
 */
public class ChainContexthandlerInterceptor implements HandlerInterceptor {

    private ChainContextProperties chainContextProperties;

    private IRequestKeyParser requestKeyParser;

    public ChainContexthandlerInterceptor(ChainContextProperties chainContextProperties, IRequestKeyParser requestKeyParser) {
        this.chainContextProperties = chainContextProperties;
        this.requestKeyParser = requestKeyParser;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (CollectionUtils.isEmpty(chainContextProperties.getKeys())
                && StringUtils.isEmpty(chainContextProperties.getKeyPrefix())) {
            // No chain context rule config
            return true;
        } else if (CollectionUtils.isEmpty(chainContextProperties.getKeys())) {
            // All keys start with keyprefix will set to chain context
            Enumeration<String> headerEnumeration = request.getHeaderNames();
            while (headerEnumeration.hasMoreElements()) {
                String header = headerEnumeration.nextElement().toUpperCase();
                if (header.startsWith(chainContextProperties.getKeyPrefix())) {
                    String value = requestKeyParser.parse(header, request);
                    if (value != null) {
                        // Add key-value to current context
                        ChainContextHolder.getCurrentContext().put(header, value);
                    }
                }
            }
        } else {
            chainContextProperties.getKeys().forEach(key -> {
                String header = chainContextProperties.getKeyPrefix() + key;
                String value = requestKeyParser.parse(header, request);
                if (value != null) {
                    // Add key-value to current context
                    ChainContextHolder.getCurrentContext().put(header, value);
                }
            });
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        if (ChainContextHolder.getCurrentContext() != null) {
            ChainContextHolder.getCurrentContext().unset();
        }
    }

}
