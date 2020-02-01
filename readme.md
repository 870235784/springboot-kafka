环境准备:
    1.安装配置 zookeeper
        启动
    2.安装配置 kafka
        启动 
    3.创建 kafka topic
         .\bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 
         --replication-factor 1 --partitions 1 --topic ${topic_name}
    4.查看 kafka topic
        .\bin\windows\kafka-topics.bat --list --zookeeper localhost:2181
        
搭建springboot-kafka项目：
    1.引入依赖
        <!-- springboot集成kafka -->
        <dependency>
            <groupId>org.springframework.kafka</groupId>
            <artifactId>spring-kafka</artifactId>
        </dependency>
    2.添加application.yml配置
        spring:
          kafka:
            bootstrap-servers: localhost:9092
            producer:
              key-serializer: org.apache.kafka.common.serialization.StringSerializer
              value-serializer: org.apache.kafka.common.serialization.StringSerializer
            consumer:
              group-id: test
              enable-auto-commit: true
              auto-commit-interval: 1000
              key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
              value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
    3.添加生产者
        Producer类
    4.添加消费者
        Consumer类
        
基本概念：
    1.broker
        kafka集群包含一个或多个服务器节点, 每个节点是一个broker
    2.topic
        每条发布到Kafka集群的消息都有一个类别，这个类别被称为Topic。物理上不同Topic的消息分开存储，逻辑上一个Topic的消息虽然
        保存于一个或多个broker上但用户只需指定消息的Topic即可生产或消费数据而不必关心数据存于何处
    3.partition
        topic中的数据分割为一个或多个partition。每个topic至少有一个partition。每个partition中的数据使用多个segment文件存储。
        partition中的数据是有序的，不同partition间的数据丢失了数据的顺序。如果topic有多个partition，消费数据时就不能保证数据的
        顺序。在需要严格保证消息的消费顺序的场景下，需要将partition数目设为1。
    4.producer
        生产者即数据的发布者，该角色将消息发布到Kafka的topic中。broker接收到生产者发送的消息后，broker将该消息追加到当前用于
        追加数据的segment文件中。生产者发送的消息，存储到一个partition中，生产者也可以指定数据存储的partition。
    5.consumer
        消费者可以从broker中读取数据。消费者可以消费多个topic中的数据。
    6.consumer group
        每个Consumer属于一个特定的Consumer Group(可为每个Consumer指定group name，若不指定group name则属于默认的group)
    7.leader
        每个partition有多个副本，其中有且仅有一个作为Leader，Leader是当前负责数据的读写的partition。
    8.follower
        Follower跟随Leader，所有写请求都通过Leader路由，数据变更会广播给所有Follower，Follower与Leader保持数据同步。
        如果Leader失效，则从Follower中选举出一个新的Leader。当Follower与Leader挂掉、卡住或者同步太慢，leader会把这个follower
        从"in sync replicas"(ISR)列表中删除，重新创建一个Follower。

配置:
    kafka delivery guarantee
        at most once 消息可能丢失，但不会重复消费
        at least once 消息不会丢失, 但可能重复消费(默认)
        exactly once 消息不会丢失, 也不会重复消费
        
高可用：
    zookeeper节点结构
        admin
        brokers 存储活着的broker信息
        controller 

producer发布消息：
    1.producer采用push模式将消息发布到 broker, 每条消息都被append到 patition 中,属于顺序写磁盘(顺序写磁盘效率比随机写内存要
        高,保障kafka吞吐率)
    2.producer发送消息到 broker 时, 会根据分区算法选择将其存储到哪一个 partition
        2.1 指定了 patition, 则直接使用;
        2.2 未指定 patition 但指定 key, 通过对key的value进行hash选出一个 patition;
        2.3 patition 和 key 都未指定, 使用轮询选出一个 patition
    3.写入流程
        step1 producer先从zookeeper的"/brokers/.../state"节点找到该partition的leader
        step2 producer将消息发送给该leader
        step3 leader将消息写入本地log
        step4 followers从leader pull消息, 写入本地log后, 向leader发送ACK 
        step5 leader收到所有ISR中的replica的ACK后, 增加HW(high watermark, 即最后commit的offset), 并向producer发送ACK    
        
broker保存消息:
    1.存储方式
        物理上把topic分成一个或多个patition, 每个patition物理上对应一个文件夹(该文件夹存储该 patition 的所有消息和索引文件)
    2.存储策略
        无论消息是否被消费, kafka都会保留所有消息。有两种策略可以删除旧数据:
            基于时间: log.retention.hours=168
            基于大小: log.retention.bytes=1073741824
            
topic创建和删除:
    1.topic创建
        step1: controller在ZooKeeper的/brokers/topics节点上注册watcher, 当topic被创建, 则controller会通过watch得到该topic
            的partition/replica分配
        step2: controller从/brokers/ids读取当前所有可用的broker列表, 对于set_p中的每一个partition:
            从分配给该partition的所有replica(称为AR)中任选一个可用的broker作为新的leader，并将AR设置为新的 ISR 
            将新的leader和ISR写入/brokers/topics/[topic]/partitions/[partition]/state
        step3: controller通过RPC向相关的broker发送LeaderAndISRRequest
    2.topic删除
        step1: controller在zooKeeper的/brokers/topics节点上注册watcher, 当topic被删除, 则controller会通过watch得到该topic
            的partition/replica分配
        step2: 若delete.topic.enable=false结束; 否则controller注册在/admin/delete_topics上的watch被fire, controller通过回
            调向对应的broker发送StopReplicaRequest