# hush-boot
Spring-cloud项目hush-boot

包含子项目如下：
    hush-registry——注册中心，通过闭环注册实现高可用eureka注册中心集群。
        主要组件：spring cloud eureka，spring security
    hush-config——配置中心，统一管理服务和客户端以及网关的配置，可实现高可用配置中心集群。
        主要组件：spring cloud config，spring cloud bus，spring security
    hush-gateway——网关，对服务和客户端请求进行路由，过滤，处理，降级等。
        主要组件：spring cloud config，spring cloud zuul
    hush-turbine——聚合，通过turbine聚合hystrix发送的客户端metrics，整合hystrix仪表盘，实现客户端访问情况监控。
        主要组件：spring cloud hystrix dashboard，spring cloud turbine，spring security
    hush-monitor——监控，通过消息中间件实现的turbine聚合，整合仪表盘，实习那客户端访问情况监控。
        主要组件：spring cloud turbine，spring cloud stream，spring security
    hush-zipkin——链路追踪，实现分布式系统中网关->客户端->服务端完整链路的记录。
        主要组件：spring cloud sleuth，spring security
    hush-admin——服务端，注册到eureka作为服务提供者，实现配置集中化，动态更新。
        主要组件：spring cloud config，spring cloud bus
    hush-webapp——客户端，注册到eureka作为服务消费者，实现超时重试，客户端负载均衡，熔断，配置集中化，动态更新。
        主要组件：spring cloud hystrix，spring cloud ribbon，spring cloud feign
    hush-core——核心依赖，整合了admin，webapp的公用依赖规范，处理类。
        主要组件：spring cloud sleuth