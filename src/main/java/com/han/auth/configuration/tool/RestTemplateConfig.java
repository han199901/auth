package com.han.auth.configuration.tool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpClient;

@Configuration
public class RestTemplateConfig {

//
//    @Bean
//    public ClientHttpRequestFactory clientHttpRequestFactory() {
//        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
//        // 连接超时时间/毫秒（连接上服务器(握手成功)的时间，超出抛出connect timeout）
//        clientHttpRequestFactory.setConnectTimeout(5 * 1000);
//        // 数据读取超时时间(socketTimeout)/毫秒（务器返回数据(response)的时间，超过抛出read timeout）
//        clientHttpRequestFactory.setReadTimeout(10 * 1000);
//        // 连接池获取请求连接的超时时间，不宜过长，必须设置/毫秒（超时间未拿到可用连接，会抛出org.apache.http.conn.ConnectionPoolTimeoutException: Timeout waiting for connection from pool）
//        clientHttpRequestFactory.setConnectionRequestTimeout(10 * 1000);
//        return clientHttpRequestFactory;
//    }
    
    @Bean
    public RestTemplate restTemplate() {
        // boot中可使用RestTemplateBuilder.build创建
        RestTemplate restTemplate = new RestTemplate();
        // 配置请求工厂
//        restTemplate.setRequestFactory(clientHttpRequestFactory);
        return restTemplate;
    }
    
}