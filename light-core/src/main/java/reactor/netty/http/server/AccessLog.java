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

import reactor.util.Logger;
import reactor.util.Loggers;

import java.util.Objects;

/**
 * @author Violeta Georgieva
 * @author lihaipeng
 */
final class AccessLog {
    static final Logger log = Loggers.getLogger("reactor.netty.http.server.AccessLog");
    static final String COMMON_LOG_FORMAT =
            "{} {} {} \"{} {} {}\" {} {} {} {} ms";
    static final String MISSING = "-";

    String address;
    CharSequence method;
    CharSequence uri;
    String protocol;
    String user = MISSING;
    CharSequence status;
    long contentLength;
    boolean chunked;
    long startTime = System.currentTimeMillis();
    int port;
    String requestId;

    AccessLog() {
    }

    AccessLog address(String address) {
        this.address = Objects.requireNonNull(address, "address");
        return this;
    }

    AccessLog port(int port) {
        this.port = port;
        return this;
    }

    AccessLog method(CharSequence method) {
        this.method = Objects.requireNonNull(method, "method");
        return this;
    }

    AccessLog uri(CharSequence uri) {
        this.uri = Objects.requireNonNull(uri, "uri");
        return this;
    }

    AccessLog protocol(String protocol) {
        this.protocol = Objects.requireNonNull(protocol, "protocol");
        return this;
    }

    AccessLog status(CharSequence status) {
        this.status = Objects.requireNonNull(status, "status");
        return this;
    }

    AccessLog contentLength(long contentLength) {
        this.contentLength = contentLength;
        return this;
    }

    AccessLog increaseContentLength(long contentLength) {
        if (chunked) {
            this.contentLength += contentLength;
        }
        return this;
    }

    AccessLog chunked(boolean chunked) {
        this.chunked = chunked;
        return this;
    }

    AccessLog user(String user) {
        this.user = user;
        return this;
    }

    AccessLog requestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    long duration() {
        return System.currentTimeMillis() - startTime;
    }

    void log() {
        if (log.isInfoEnabled()) {
            log.info(COMMON_LOG_FORMAT, requestId, address, user,
                    method, uri, protocol, status, (contentLength > -1 ? contentLength : MISSING), port, duration());
        }
    }
}
