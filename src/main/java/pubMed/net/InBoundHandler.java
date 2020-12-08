package pubMed.net;

import com.mongodb.util.JSON;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import pubMed.service.BruteForce;
import pubMed.service.Search;
import pubMed.user.Interact;

public class InBoundHandler extends ChannelInboundHandlerAdapter {
    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        //ctx.fireChannelActive();
        System.out.println("channelActive!");
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object s) throws Exception {
        System.out.println("channelread!");
        Channel channel = ctx.channel();
        String query = ((TextWebSocketFrame) s).text();
        query = query.replace("\"","" );
        System.out.println(query);

        String res = BruteForce.bruteForce("C:\\Users\\Zeyu\\eclipse-workspace\\pubMed\\src\\main\\resources\\static\\xml\\testfile.xml",query);
        channel.writeAndFlush(new TextWebSocketFrame(res));
        System.out.println("success");
        System.out.println(res);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        super.channelWritabilityChanged(ctx);
    }
}