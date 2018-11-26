package com.springcloud.chaincontext.feign;

import com.springcloud.chaincontext.ChainContextHolder;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * RequestInterceptor to add chain context to feign request
 * 
 * @author einarzhang
 * @email einarzhang@gmail.com
 */
public class ChainContextRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        if (ChainContextHolder.getCurrentContext() != null) {
            ChainContextHolder.getCurrentContext().forEach((key, value) -> {
                template.header(key, value.toString());
            });
        }
    }

}
