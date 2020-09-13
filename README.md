# Light-SpringCloud

## 介绍
Light-SpringCloud是一个开源的分布式微服务快速开发框架，基于Spring Cloud Hoxton、Spring Boot 2.3、Spring Cloud Alibaba 2.2.1。

「架构图」

### 为什么选择Light-SpringCloud

* 内置企业级解决方案（统一日志管理、灰度发布模式等）
* 使用目前主流的开源框架，核心代码简单，方便学习和扩展
* 致力于帮助更多人学习和掌握微服务技术栈
* 遵循阿里巴巴编码规范
* 采用GPL开源许可证

如果您正在处于学习或实践"微服务技术架构转型"阶段，参考下本项目会是一个不错的选择。此项目正是从传统服务架构向微服务架构转型阶段，研究和应用的产物。

### 核心功能
* 服务注册与发现（Nacos）
* 统一配置管理（Nacos）
* 统一服务调用日志管理
* 统一接口管理(Swagger，Yapi)
* 优雅停机
* 灰度发布（gray）
* 链路跟踪（requestId）

### 技术选型
dependencies version
Spring Boot	2.3
Spring Cloud Hoxton
Spring Cloud Alibaba 2.1.1
Nacos
Feign
RestTemplate
Ribbon
Spring Cloud Gateway
Alibaba Druid
MyBatis-Plus
Redis
RocketMQ

## 项目结构
```
Light-SpringCloud
├── light-core -- 框架核心代码
├── light-common -- 框架公共代码
├── service-archetype -- 业务模块原型
├── light-examples -- 示例
    ├── light-gateway -- 网关服务
    ├── light-demo-service -- 业务模块服务实例
    ├── light-demo-dto -- 业务模块数据传输对象
    ├── light-demo-api -- 业务服务API（Feign调用）
```


## 未来计划支持
* Docker
* Kubernetes
