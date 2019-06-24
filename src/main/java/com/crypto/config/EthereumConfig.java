package com.crypto.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class EthereumConfig {

    @Value("${ethereum.url}")
    private String url;

    @Bean
    @Scope("singleton")
    public Web3j web3j() {
        log.info("Building web3j service for endpoint: {}", url);
        return Web3j.build(new HttpService(url));
    }

}
