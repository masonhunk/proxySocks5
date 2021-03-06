package com.cyz.socks5.server.enums;


/**
 * 命令响应类型
 */
public enum  CommandResponseEnum {
    SUCCESS(0x00),
    PROXY_FAILED(0x01),
    FORBIDDEN(0x02),
    BAD_NETWORK(0x03),
    INVALID_HOST(0x04),
    REFUSED(0x05),
    TTL_EXPIRED(0x06),
    UNKNOWN_CMD(0x07),
    UNKNOWN_ADDRTYPE(0x08);

    private int code;

    CommandResponseEnum(int code){
        this.code = code;
    }

    public static CommandResponseEnum fromCode(int response) {
        for(CommandResponseEnum val: CommandResponseEnum.values()){
            if(val.code == response){
                return val;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }
}
