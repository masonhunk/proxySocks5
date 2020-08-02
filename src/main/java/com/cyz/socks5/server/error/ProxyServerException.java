package com.cyz.socks5.server.error;

import com.cyz.socks5.server.enums.ClientErrorEnum;

public class ProxyServerException extends RuntimeException{

    public ProxyServerException(ClientErrorEnum errorEnum){
        super(errorEnum.getMessage());
    }

}