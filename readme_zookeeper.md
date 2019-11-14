1.分布式环境特点
    分布, 并发, 无序

2.分布式常见问题
    网络通信
    网络分区(脑裂):
        当网络发生异常导致分布式系统中部分节点之间的网络延时不断增大，最终导致组成分布式架构的所有节点，只有部分节点
        能够正常通信
    三态:
        成功, 失败, 超时
    分布式事务

3.zookeeper
    3.1 作用
        数据发布/订阅
        负载均衡
        命名服务
        master选举
        分布式队列
        分布式锁
    3.2 特点
        顺序一致性: 从同一个客户端发起的事务请求，最终会严格按照顺序被应用到zookeeper中
        原子性: 所有的事务请求的处理结果在整个集群中的所有机器上的应用情况是一致的，也就是说，要么整个集群中的所有
                机器都成功应用了某一事务,要么全都不应用
        可靠性: 一旦服务器成功应用了某一个事务数据，并且对客户端做了响应，那么这个数据在整个集群中一定是同步并且保
                留下来的
        实时性: 一旦一个事务被成功应用，客户端就能够立即从服务器端读取到事务变更后的最新数据状态(zookeeper仅仅保证
                在一定时间内，近实时)
    3.3 安装
        3.3.1 单机安装
            第一步: 下载zookeeper的安装包
                http://apache.fayea.com/zookeeper/stable/zookeeper-3.4.10.tar.gz
            第二步: 解压zookeeper 
                tar -zxvf zookeeper-3.4.10.tar.gz
            第三步: cd 到 ZK_HOME/conf  , copy一份zoo.cfg
                cp  zoo_sample.cfg  zoo.cfg
            第四步：服务端启动/终止等 
                zkServer.sh {start|start-foreground|stop|restart|status|upgrade|print-cmd}
            第五步: 客户端启动
                zkCli.sh -server  ip:port
        3.3.2 集群安装 (暂略)
    3.4 zoo.cfg 主配置文件
        tickTime=2000  zookeeper中最小的时间单位长度 （ms）
        initLimit=10   follower节点启动后与leader节点完成数据同步的时间
        syncLimit=5    leader节点和follower节点进行心跳检测的最大延时时间
        dataDir=/tmp/zookeeper  表示zookeeper服务器存储快照文件的目录
        dataLogDir     表示配置 zookeeper事务日志的存储路径，默认指定在dataDir目录下
        clientPort     表示客户端和服务端建立连接的端口号： 2181

        
