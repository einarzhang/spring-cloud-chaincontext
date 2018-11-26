package com.springcloud.chaincontext.rest;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import com.springcloud.chaincontext.ChainContextHolder;

/**
 * HttpRequestInterceptor for chain context
 * 
 * @author einarzhang
 * @email einarzhang@gmail.com
 */
public class ChainContextHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        if (ChainContextHolder.getCurrentContext() != null) {
            ChainContextHolder.getCurrentContext().forEach((key, value) -> {
                request.getHeaders().add(key, value.toString());
            });
        }
        return execution.execute(request, body);
    }

}
