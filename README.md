# lion

a RPC framework
  
TODO List
consumer 注册zk 
lion-regitry-redis

lion-amidn-web 
配置注册中心信息，获得provider consumer 信息
服务治理：服务 与消费者查询，路由修改


管控控制台
1、通过阅读代码，熟悉现有配置和监控系统dubbo-admin， dubbokeeper，dubbo-monitor-simple
1.1 学习webx ，http://openwebx.org/docs/index.html
    dubbo-admin 基于webx开发，故需要先熟悉
1.2 阅读代码，熟悉dubbo-admin如何进行配置，
1.3 阅读dubbo-monitor-simple，了解如何进行监控    
     
JMX 

neural：放通率控制、流量控制、服务降级、幂等机制、泛化容错、SLA熔断、隔离舱壁、超时控制和慢性尝试功能。

CUP

other
1、熟悉（过程，主键，及关系），学习spring 自定义标签、自定义注解开发 
2、支持zk注册 DONE
3、拆解成组件：网络通信、注册、
4、网络层支持心跳


考虑将register 支持 spring 获得注册对象
考虑 admin-web变成 spring boot服务


分布式事务：
7、超时
8、二阶段，回顾或提交顺序问题
10、幂等处理  
9、本地异常，外部异常 传播异常处理，回滚
1、持久化
2、恢复
5、二阶段重试，告警
3、传播问题
4、监控管理（tps，调用关系）
11、考虑是否区分 远程二阶段 本地二阶段
12、本地模式 与 中心模式
6、抽象、解耦

限制：
1、dubbo 
2、基于spring
3、服务接口不能有多个实现
4、









