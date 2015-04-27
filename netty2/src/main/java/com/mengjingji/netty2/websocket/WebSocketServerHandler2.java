package com.mengjingji.netty2.websocket;

import static org.jboss.netty.handler.codec.http.HttpResponseStatus.FORBIDDEN;
import static org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import org.jboss.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import org.jboss.netty.util.CharsetUtil;

public class WebSocketServerHandler2 extends SimpleChannelUpstreamHandler {
	String WEBSOCKET_PATH="/websocket";
	@Override
	public void messageReceived(ChannelHandlerContext ctx,MessageEvent e){
		Object msg=e.getMessage();
		if(msg instanceof HttpRequest){
			handleHttpRequest(ctx,(HttpRequest)msg);
			
		}else if(msg instanceof WebSocketFrame){
			System.out.println("web socket frame");
			
		}
	}
	
	private void handleHttpRequest(ChannelHandlerContext ctx,HttpRequest req) {
		System.out.println("http req="+req.toString());
		DefaultHttpResponse response=new DefaultHttpResponse(HTTP_1_1, FORBIDDEN);
		if(response.getStatus().getCode()!=200){
			response.setContent(ChannelBuffers.copiedBuffer(response.getStatus().toString(),CharsetUtil.UTF_8));
			HttpHeaders.setContentLength(response,response.getContent().readableBytes());
		}
		
		//shackHand
		WebSocketServerHandshakerFactory factory=new WebSocketServerHandshakerFactory("ws://" + req.getHeader(HttpHeaders.Names.HOST) + WEBSOCKET_PATH, null, false);
		WebSocketServerHandshaker shaker=factory.newHandshaker(req);
		if(shaker==null){}else {
			shaker.handshake(ctx.getChannel(), req);
			System.out.println("handShak ok!");
			return;
		}
		ChannelFuture future=ctx.getChannel().write(response);
		
	}
}
