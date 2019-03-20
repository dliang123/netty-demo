package com.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @Description
 * @Author dengliang
 * @Email liang.deng@atzuche.cn
 * @Date Created in 16:15 2018/7/31
 */
public class NioServer {
    private int port;
    //声明通道
    private ServerSocketChannel server;
    //Selector
    private Selector selector;
    //缓冲区
    private ByteBuffer receiveBuffer = ByteBuffer.allocate(1024);

    public NioServer(int port) {
        this.port = port;
        try {
            server = ServerSocketChannel.open();
            server.configureBlocking(false);
            //服务端通道绑定地址
            server.bind(new InetSocketAddress("127.0.0.1", this.port));
            selector = Selector.open();
            //给服务器通道注册接收操作，可以在这个点接收客户端连接
            server.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("[系统消息提示]NIO服务器初始化完毕，监听端口" + this.port);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //监听客户端连接
    public void Listener() {
        while (true) {
            try {
                //这个方法可能会阻塞，直到至少有一个已注册的事件发生,或者当一个或者更多的事件发生时
                //selector.select(long timeout);可以设置超时时间，防止进程阻塞
                //selector.wakeup();可以唤醒阻塞状态下的selector
                //selector.selectNow();也可以立刻返回，不阻塞
                selector.select();
                //轮询所有选择器接收到的操作
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> selectionKeyIte = selectionKeys.iterator();
                while (selectionKeyIte.hasNext()) {
                    SelectionKey selectionKey = selectionKeyIte.next();
                    selectionKeyIte.remove();
                    //业务处理
                    handleKey(selectionKey);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //业务处理

    public void handleKey(SelectionKey selectionKey) throws IOException {
        //服务端的channel
        ServerSocketChannel server = null;
        //获得和客户端连接的通道
        SocketChannel channel = null;
        //接收到的信息
        String receiveText;
        //数据的标记位，判断客户端是否关闭
        int count;
        //客户端请求连接事件
        if (selectionKey.isAcceptable()) {
            try {
                server = (ServerSocketChannel) selectionKey.channel();
                //获得服务端和客户端连接的通道
                channel = server.accept();
                //将服务端和客户端连接的设置非阻塞
                channel.configureBlocking(false);
                //注册服务端和客户端通道的读事件
                channel.register(selector, SelectionKey.OP_READ);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (selectionKey.isReadable()) {
            //获取通道对象，方便后面将通道内的数据读入缓冲区
            channel = (SocketChannel) selectionKey.channel();
            receiveBuffer.clear();
            count = channel.read(receiveBuffer);
            //如果读出来的客户端数据不为空
            if (count > 0) {
                receiveText = new String(receiveBuffer.array(), 0, count);
                System.out.println("[系统消息提示]服务器发现[" + receiveText + "]玩家上线");
            } else {
                System.out.println("[系统消息提示]玩家下线");
                //检测到客户端关闭（玩家下线），删除该selectionKey监听事件，否则会一直收到这个selectionKey的动作
                selectionKey.cancel();
            }
        }
    }

    public static void main(String[] args) {
        int port = 7080;
        NioServer server = new NioServer(port);
        server.Listener();
    }

}
