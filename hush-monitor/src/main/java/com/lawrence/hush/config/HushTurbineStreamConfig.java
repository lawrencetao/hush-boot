package com.lawrence.hush.config;

import com.netflix.turbine.aggregator.InstanceKey;
import com.netflix.turbine.aggregator.StreamAggregator;
import com.netflix.turbine.internal.JsonUtility;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.reactivex.netty.RxNetty;
import io.reactivex.netty.pipeline.PipelineConfigurators;
import io.reactivex.netty.protocol.http.server.HttpServer;
import io.reactivex.netty.protocol.http.sse.ServerSentEvent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.actuator.HasFeatures;
import org.springframework.cloud.netflix.turbine.stream.TurbineStreamProperties;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.SocketUtils;
import rx.Observable;
import rx.subjects.PublishSubject;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 自定义turbine-stream配置, 修复netty升级造成hystrix-dashboard不能展示聚合信息的bug
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({TurbineStreamProperties.class})
public class HushTurbineStreamConfig implements SmartLifecycle {

    private AtomicBoolean running = new AtomicBoolean(false);

    @Autowired
    private TurbineStreamProperties properties;
    @Getter
    private int turbinePort;

    public HushTurbineStreamConfig() {

    }

    @Bean
    public HasFeatures Feature() {

        return HasFeatures.namedFeature("Turbine (Stream)", TurbineStreamProperties.class);
    }

    @Bean
    public PublishSubject<Map<String, Object>> hystrixSubject() {

        return PublishSubject.create();
    }

    @Bean
    @SuppressWarnings("deprecation")
    public HttpServer<ByteBuf, ServerSentEvent> aggregatorServer() {
        Observable<Map<String, Object>> publishedStreams = StreamAggregator.aggregateGroupedStreams(this.hystrixSubject().groupBy((data) -> InstanceKey.create((String) data.get("instanceId")))).doOnUnsubscribe(() -> {
            log.info("Unsubscribing aggregation.");
        }).doOnSubscribe(() -> {
            log.info("Starting aggregation");
        }).flatMap((o) -> o).publish().refCount();
        Observable<Map<String, String>> ping = Observable.timer(1L, 10L, TimeUnit.SECONDS).map((Long count) -> Collections.singletonMap("type", "Ping")).publish().refCount();
        Observable<Map<String, ?>> output = Observable.merge(publishedStreams, ping);
        this.turbinePort = this.properties.getPort();
        if (this.turbinePort <= 0) {
            this.turbinePort = SocketUtils.findAvailableTcpPort(40000);
        }

        HttpServer<ByteBuf, ServerSentEvent> httpServer = RxNetty.createHttpServer(this.turbinePort, (request, response) -> {
            log.info("SSE Request Received");
            response.getHeaders().setHeader("Content-Type", "text/event-stream");
            return output.doOnUnsubscribe(() -> {
                log.info("Unsubscribing RxNetty server connection");
            })
                    // writeAndFlush方法中, ServerSentEvent更换构造函数, 定义EventType, bug fixed
                    .flatMap(data -> response.writeAndFlush(new ServerSentEvent(null,
                            Unpooled.copiedBuffer("message", StandardCharsets.UTF_8),
                            Unpooled.copiedBuffer(JsonUtility.mapToJson(data) + "\n", StandardCharsets.UTF_8))));
        }, PipelineConfigurators.serveSseConfigurator());

        return httpServer;
    }

    public boolean isAutoStartup() {

        return true;
    }

    public void stop(Runnable callback) {
        this.stop();
        callback.run();
    }

    public void start() {
        if (this.running.compareAndSet(false, true)) {
            this.aggregatorServer().start();
        }

    }

    public void stop() {
        if (this.running.compareAndSet(true, false)) {
            try {
                this.aggregatorServer().shutdown();
            } catch (InterruptedException var2) {
                log.error("Error shutting down", var2);
            }
        }

    }

    public boolean isRunning() {

        return this.running.get();
    }

    public int getPhase() {

        return 0;
    }

}
