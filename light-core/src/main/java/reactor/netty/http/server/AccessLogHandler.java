/*
 * Copyright (c) 2011-Present VMware, Inc. or its affiliates, All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package reactor.netty.http.server;

import com.light.constant.LightConstants;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;
import org.springframework.util.StringUtils;

/**
 * @author Violeta Georgieva
 * @author lihaipeng
 */
final class AccessLogHandler extends ChannelDuplexHandler {

    AccessLog accessLog = new AccessLog();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof HttpRequest) {
            final HttpRequest request = (HttpRequest) msg;
            final SocketChannel channel = (SocketChannel) ctx.channel();

            accessLog = new AccessLog()
                    .address(channel.remoteAddress().getHostString())
                    .port(channel.localAddress().getPort())
                    .method(request.method().name())
                    .uri(request.uri())
                    .protocol(request.protocolVersion().text());
        }
        ctx.fireChannelRead(msg);
    }

    @Override
    @SuppressWarnings("FutureReturnValueIgnored")
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        if (msg instanceof HttpResponse) {
            final HttpResponse response = (HttpResponse) msg;
            final HttpResponseStatus status = response.status();

            if (status.equals(HttpResponseStatus.CONTINUE)) {
                //"FutureReturnValueIgnored" this is deliberate
                ctx.write(msg, promise);
                return;
            }
            String requestId = response.headers().get(LightConstants.X_LIGHT_REQUESTID);
            if (!StringUtils.isEmpty(requestId)) {
                response.headers().remove(LightConstants.X_LIGHT_REQUESTID);
                accessLog.requestId(requestId);
            }
            String userName = response.headers().get(LightConstants.X_LIGHT_USERNAME);
            if (!StringUtils.isEmpty(userName)) {
                response.headers().remove(LightConstants.X_LIGHT_USERNAME);
                accessLog.user(userName);
            }
            final boolean chunked = HttpUtil.isTransferEncodingChunked(response);
            accessLog.status(status.codeAsText())
                    .chunked(chunked);
            if (!chunked) {
                accessLog.contentLength(HttpUtil.getContentLength(response, -1));
            }
        }
        if (msg instanceof LastHttpContent) {
            accessLog.increaseContentLength(((LastHttpContent) msg).content().readableBytes());
            ctx.write(msg, promise.unvoid())
                    .addListener(future -> {
                        if (future.isSuccess()) {
                            accessLog.log();
                        }
                    });
            return;
        }
        if (msg instanceof ByteBuf) {
            accessLog.increaseContentLength(((ByteBuf) msg).readableBytes());
        }
        if (msg instanceof ByteBufHolder) {
            accessLog.increaseContentLength(((ByteBufHolder) msg).content().readableBytes());
        }
        //"FutureReturnValueIgnored" this is deliberate
        ctx.write(msg, promise);
    }
}
