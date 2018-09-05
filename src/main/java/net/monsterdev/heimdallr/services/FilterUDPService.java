package net.monsterdev.heimdallr.services;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import net.monsterdev.heimdallr.model.NetworkSlot;

import java.net.DatagramPacket;

public class FilterUDPService extends FilterService {
    public class FilterHandler extends SimpleChannelInboundHandler<DatagramPacket> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
            //
        }
    }

    public FilterUDPService(NetworkSlot slot) {
        super(slot);
    }

    @Override
    public void run() {
        final EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new ChannelInitializer<NioDatagramChannel>() {
                        @Override
                        protected void initChannel(NioDatagramChannel ch) throws Exception {
                            ch.pipeline().addLast(new FilterHandler());
                        }
                    });
            ChannelFuture f = bootstrap.bind(getSlot().getAddr(), getSlot().getPort()).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
