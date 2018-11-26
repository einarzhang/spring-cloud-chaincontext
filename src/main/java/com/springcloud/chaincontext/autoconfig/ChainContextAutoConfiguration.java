package com.springcloud.chaincontext.autoconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.springcloud.chaincontext.ChainContextProperties;
import com.springcloud.chaincontext.feign.FeignConfiguration;
import com.springcloud.chaincontext.hystrix.HystrixChainContextConfiguration;
import com.springcloud.chaincontext.mvc.ChainContextWebMvcConfigure;
import com.springcloud.chaincontext.mvc.ChainContexthandlerInterceptor;
import com.springcloud.chaincontext.parser.DefaultRequestKeyParser;
import com.springcloud.chaincontext.parser.IRequestKeyParser;
import com.springcloud.chaincontext.rest.ChainContextBeanPostProcessor;

/**
 * ChainContextAutoConfiguration
 * 
 * @author einarzhang
 * @email einarzhang@gmail.com
 */
@Configuration
@Import({FeignConfiguration.class, HystrixChainContextConfiguration.class})
@EnableConfigurationProperties(ChainContextProperties.class)
public class ChainContextAutoConfiguration implements WebMvcConfigurer {

    @Autowired
    private ChainContextProperties chainContextProperties;

    @ConditionalOnMissingBean
    @Bean
    public IRequestKeyParser requestKeyParser() {
        return new DefaultRequestKeyParser();
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer(IRequestKeyParser requestKeyParser) {
        ChainContexthandlerInterceptor interceptor = new ChainContexthandlerInterceptor(chainContextProperties, requestKeyParser);
        return new ChainContextWebMvcConfigure(interceptor);
    }

    @Bean
    public ChainContextBeanPostProcessor chainContextBeanPostProcessor() {
        return new ChainContextBeanPostProcessor();
    }
    
}
