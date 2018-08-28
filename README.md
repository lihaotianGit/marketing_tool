# Marketing 项目使用手册

## 准备环境

> 以下为 Mac 环境，Windows 环境部分命令不同，但步骤一致。

##### 1. 访问 http://archive.apache.org/dist/zookeeper/zookeeper-3.4.9/ ，并点击下载。

![avatar](https://note.youdao.com/yws/public/resource/be3123a6b74a1404eec5df4e5c391dc2/xmlnote/4205228A49D94D499DAAB3773945E975/6768)

> 注：可以访问 http://archive.apache.org/dist/zookeeper/ 查看多个版本。

##### 2. 下载完成后解压到任意目录，cd到 /zookeeper-3.4.9/bin 目录，运行以下指令启动 ZooKeeper 服务端。

```
./zkServer.sh start
```

##### 3. cd 到 Workspace 目录，使用以下指令将 Marketing 项目 Demo 下载到本地，并等待编译完成。

```
git clone git@github.com:lihaotianGit/marketing_tool.git
```

##### 4. 执行以下命令构建 Marketing 项目。

```
./gradlew clean uA install
```

> 该指令会将项目中的 api 模块打包放到本地 maven 仓库，方便其他项目的引用。

> 注：将指定模块打包放到本地maven仓库的方法：在指定模块的 build.gradle 文件中添加如下 groovy 代码，并执行 uA 指令。

```
// uploadArchives 是一个groovy任务，对应 ./gradlew clean uA install 指令中的uA。
uploadArchives {
    repositories {
        mavenDeployer {
            repository(url: mavenLocal().getUrl())
        }
    }
}
```

##### 5. 检查 provider 项目的 application.properties 文件配置，找到 Application.java 文件，启动 provider 服务。

##### 6. 使用IDE打开 midas 项目，拉取 dubbo_demo 分支，点击 gradle 刷新按钮，并执行编译打包等指令。

##### 7. 打开 mvp 项目，切换到 dev 分支，执行编译打包等指令，并启动 midas 服务。

##### 8. 启动 Nginx MySQL 等其他服务，保证 p2p 库中 COUPONS 表里有数据，然后使用浏览器访问 http://localhost:8088/api/coupon?id=1 即可看到效果。

## 编码向导

#### Marketing 项目

1. 在 api 模块下新增或修改相应的 Service 接口和实体类，改动后需执行构建项目命令，将改动更新到本地maven仓库。

> midas（消费端）会引入 api 模块，通过调用 api 接口的方式通过 Dubbo 完成实际的远程过程调用。

2. 在 provider 模块下新增或修改相应的 Service 实现类或 Mapper 。

3. 如在步骤1和步骤2中新增了Service接口和实现类，需要在 provider dubbo-provider.xml 中按照例子新增相应配置。

4. 重启 provider 服务。

#### midas 项目

1. 如在 Marketing-api 中做了改动，需要重新点击 gradle 刷新按钮并执行编译指令，将新的 Marketing-api 的改动引入到 midas 。

2. 如在 Marketing 项目中新增了 Service 接口和实现类，需在相应的 Module 类中新增 Guice Provider ，参照如下代码。

```
// class DubboModule extends AbstractModule

@Provides
@Singleton
public CouponService couponService() {
    // couponServiceConf实例及couponServiceConf.get()内部封装了与注册中心和服务提供方的连接，所以比较重，且无状态，在此做成单例
    ReferenceConfig<CouponService> couponServiceConf = new ReferenceConfig<>();
    couponServiceConf.setApplication(applicationConfig);
    couponServiceConf.setRegistry(registryConfig); // 多个注册中心可以用setRegistries()
    couponServiceConf.setInterface(CouponService.class);
    couponServiceConf.setVersion("1.0.0");
    return couponServiceConf.get();
}
```

3. 在 midas 项目中使用注解 @Inject 注入相应 Service，就可以尽情的玩耍了。

> 注：注解必须是 javax.inject.Inject，不可以使用 guice 的 Inject。

4. 重新编译 midas 项目，切换到 mvp 中， 重启 midas 服务。

## RPC 服务注册流程浅析

### 服务注册流程

![avatar](https://note.youdao.com/yws/public/resource/be3123a6b74a1404eec5df4e5c391dc2/xmlnote/A67833E97291456F881899229DFEF071/6874)

> 服务提供者：Marketing-provider； 服务消费者：midas； 注册中心：ZooKeeper 。

0. 服务容器负责将服务提供者中配置的 Service 初始化为 Spring 的 Bean ，并监听服务端口暴露服务。
1. 服务提供者向注册中心注册自己提供的服务。
2. 服务消费者在启动时，初始化 Guice Provides ，根据配置生成 Service 代理类，向 ZooKeeper 注册自己并订阅自己所需的服务。
3. 注册中心返回服务提供者地址列表给服务消费者，注册中心不做服务调用中转，所以负载压力不大。如果服务提供者有变更，注册中心将基于长连接推送变更数据给服务消费者。
4. 服务消费者从提供者地址列表中，基于软负载均衡算法，选一台提供者进行调用，如果调用失败，再选另一台调用。
5. 服务消费者和提供者，在内存中累计调用次数和调用时间，定时每分钟发送一次统计数据到监控中心。

> 更多内容请参照 Dubbo 官网：http://dubbo.apache.org/zh-cn/

### ZooKeeper 内部数据结构

![avatar](https://note.youdao.com/yws/public/resource/be3123a6b74a1404eec5df4e5c391dc2/xmlnote/7E693C8EDE4B4FDBAAA0319493EF23DD/6908)

* ZooKeeper内部维护了一个树结构，节点叫做Znode，分为永久节点和临时节点。

1. 注册时，会在 ZooKeeper 中生成一个永久节点 dubbo。
2. dobbo 节点下是多个 Service 永久节点。
3. 每个 Service 节点下生成两个永久节点：providers 和consumers ，分别对应注册为服务提供者的节点和注册为服务消费者的节点。
4. 每个 provider 和 consumer 节点下会有多个临时节点，记录了各自的服务地址。

### ZooKeeper 临时节点特性

* ZooKeeper 客户端与服务端建立连接会话和节点，如建立持久节点，则会话失效节点依然存在；如建立临时节点则会话失效相应的临时节点即被删除。

> 注1：上述“服务注册流程”中的服务提供者（Marketing-provider）和服务消费者（midas）都是 ZooKeeper 的客户端。

> 注2：ZooKeeper 使用心跳监测与客户端的会话是否失效。

> 注3：如 ZooKeeper 客户端与服务端连接断开，只要在规定的时间内、允许的重试次数内重新连接到 ZooKeeper 服务端，则会话继续有效。如超出规定的时间和次数仍未重新连接成功，则视为会话失效，删除临时节点。

### Watcher 机制

> ZooKeeper 客户端可以使用 Watcher 监听服务端某节点下的所有子节点的变动通知，当被监听节点的子节点有增加、删除等操作时，ZooKeeper 服务端会将变动主动通知到监听该节点的客户端。

## RPC 服务调用流程浅析

1. 服务消费者调用上述生成的 Service 代理类，将调用地址及参数格式化为dubbo协议格式，远程调用服务提供者。
2. 服务提供者使用IO多路复用模型，非阻塞的接受调用请求，根据dubbo协议格式中的地址及 Service 参数信息转换为Java格式，调用真正的 Service 实现类，并将结果返回给服务消费者。
3. 期间如有调用失败，服务消费者会根据配置的重试次数及超时时间做出相应重试或者超时报错等反应。

## 后续正式开发改动建议

* midas 的 DubboModule 改名为 MarketingModule ，应当每一个服务提供者对应一个 Module ，Module 中初始化若干个该服务的 Service 。

* midas 的 Module 类原本放在 midas 的 ModelModule 类中进行初始化，可以优化为在 mvp 项目的 jiashi-agreements JiashiResourceConfig 类中进行配置。

> 由于定时任务也引用了 ModelModule ，如果定时任务并未用到该 Dubbo 服务，会造成资源浪费。如果定时任务想引用某个 Dubbo 服务的 Module ，可以在自己的IOC容器中初始化。


### 个人水平有限，如有错误，请及时指正。谢谢。

