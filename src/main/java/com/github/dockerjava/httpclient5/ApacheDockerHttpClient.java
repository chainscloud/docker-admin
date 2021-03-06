package com.github.dockerjava.httpclient5;

import com.github.dockerjava.transport.SSLConfig;

import java.net.URI;
import java.util.Objects;

public final class ApacheDockerHttpClient extends ApacheDockerHttpClientImpl {

    public static final class Builder {

        private URI dockerHost = null;

        private SSLConfig sslConfig = null;

        private int maxConnections = Integer.MAX_VALUE;
        private String virtualHost = null;

        public Builder dockerHost(URI value) {
            this.dockerHost = Objects.requireNonNull(value, "dockerHost");
            return this;
        }

        public Builder virtualHost(String host) {
            this.virtualHost = host;
            return this;
        }

        public Builder sslConfig(SSLConfig value) {
            this.sslConfig = value;
            return this;
        }

        public Builder maxConnections(int value) {
            this.maxConnections = value;
            return this;
        }

        public ApacheDockerHttpClient build() {
            Objects.requireNonNull(dockerHost, "dockerHost");
            ApacheDockerHttpClient client = new ApacheDockerHttpClient(dockerHost, sslConfig, maxConnections);
            client.setVirtualHost(virtualHost);
            return client;
        }
    }

    private ApacheDockerHttpClient(URI dockerHost, SSLConfig sslConfig, int maxConnections) {
        super(dockerHost, sslConfig, maxConnections);
    }
}
