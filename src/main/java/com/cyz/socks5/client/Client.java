package com.cyz.socks5.client;

import com.cyz.socks5.server.HostResolver;
import com.cyz.socks5.server.enums.AddrTypeEnum;
import com.cyz.socks5.server.enums.AuthenticationMethod;
import com.cyz.socks5.server.enums.CommandResponseEnum;
import com.cyz.socks5.server.enums.CommandTypeEnum;
import com.cyz.socks5.server.message.*;

import java.io.IOException;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("127.0.0.1", 1080);
        testHandshake(socket);
        testAuthenticate(socket);
        testCmd(socket);
        testReplay(socket);
    }

    private static void testReplay(Socket socket) throws IOException {
        byte[] bytes = "Hi".getBytes();
        socket.getOutputStream().write(bytes);
        System.in.read();
    }

    private static void testHandshake(Socket socket) throws IOException {
        System.out.println("发送握手请求");
        HandshakeRequest handshakeRequest = new HandshakeRequest();
        handshakeRequest.setVersion((byte)0x05);
        handshakeRequest.setMethodCount((byte)2);
        handshakeRequest.setMethods(new byte[] {
                (byte) AuthenticationMethod.NONE.getCode(),
                (byte)AuthenticationMethod.USERPASSWORD.getCode()});
        handshakeRequest.serialize(socket.getOutputStream());


        HandshakeResponse response = new HandshakeResponse();
        response.deserialize(socket.getInputStream());
        System.out.println("握手结果:"+response.getMethod());
    }

    private static void testAuthenticate(Socket socket) throws IOException {
        System.out.println("发送认证请求");
        String username = "zhangsan";
        String pwd = "123456";
        UserPasswordAuthenticationRequest request = new UserPasswordAuthenticationRequest();
        request.setVersion((byte)0x01);
        request.setUsername(username);
        request.setUsernameLength((byte)username.getBytes().length);
        request.setPassword(pwd);
        request.setPasswordLength((byte)pwd.getBytes().length);

        request.serialize(socket.getOutputStream());
        //socket.getOutputStream().flush();
        System.out.println("Sending msg complete");

        AuthenticationResultResponse result = new AuthenticationResultResponse();
        result.deserialize(socket.getInputStream());
        System.out.println(result.getResult()==0x00?"认证成功":"认证失败");
    }


    private static void testCmd(Socket socket) throws IOException {
        System.out.println("发送命令");
        CommandRequest request = new CommandRequest();
        request.setCmd((byte)CommandTypeEnum.CONNECT.getCode());
        request.setAddressType((byte)AddrTypeEnum.DOMAIN.getCode());
        request.setDstAddr(new HostResolver().hostToBytes(AddrTypeEnum.DOMAIN.getCode(), "www.baidu.com"));
        //request.setDstAddr(new HostResolver().hostToBytes(AddrTypeEnum.DOMAIN.getCode(), "www.baiduahefa.com"));
        request.setDstPort(80);
        //request.setDstPort(777);
        request.serialize(socket.getOutputStream());
        System.out.println("Sending cmd msg complete");

        CommandResponse result = new CommandResponse();
        result.deserialize(socket.getInputStream());
        System.out.println(CommandResponseEnum.fromCode(result.getResponse()).name());
    }

}
