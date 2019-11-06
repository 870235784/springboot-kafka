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
        
搭建springboot-kafka项目
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