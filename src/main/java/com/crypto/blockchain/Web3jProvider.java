package com.crypto.blockchain;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;

@Component
public abstract class Web3jProvider {

    @Lookup
    public abstract Web3j get();

}
