package com.springcloud.chaincontext.parser;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

/**
 * The default key parser, either get key info from header or if null get from parameter
 * 
 * @author einarzhang
 * @email einarzhang@gmail.com
 */
public class DefaultRequestKeyParser implements IRequestKeyParser {

    @Override
    public String parse(String key, HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(key)).orElse(request.getParameter(key));
    }

}
