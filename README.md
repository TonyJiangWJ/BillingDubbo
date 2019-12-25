## BillingDubbo 账单管理后台
- 基于Dubbo2.7.3 SpringBoot2.0.1实现，数据库采用MySQL 需要Redis支持，zookeeper可选内置zookeeper，建议外置
- 开发IDE需要下载lombok插件，否则编译报错，具体可以百度
- 数据库结构见[doc/SQL_STRUCT.sql](./doc/SQL_STRUCT.sql)
- 接口文档[在线](https://app.swaggerhub.com/apis/TonyJiangWJ/bill-server-api/1.0.0)
- 前端工程[vue-bill-manager-wp4](https://github.com/TonyJiangWJ/vue-bill-manager-wp4)

## 项目结构说明
- billing-api 存放POJO 暴露给客户端的service接口等等
- billing-common 存放一些通用的工具类
- billing-server 是服务提供方，实现service接口，业务判断，数据持久化操作等等
- billing-client 是服务消费方，将http请求转换成对service的请求，并做简单的必填项判断，然后将service执行结果转换成json传递给前端
### client 和 server 之间的用户信息传递
- 通过自定义的DubboFilter，将userId放到RpcContext的attachments中

## 开发部署说明

### zookeeper安装和启动[可选]

- 下载安装zookeeper并启动，推荐docker方式，具体请Google或百度
- 不想安装zookeeper可以使用内置的zookeeper，修改 `billing-server` 的配置
  ```yaml
  embedded:
    zookeeper:
      # 开启内置zookeeper
      enable: true
  ```

### redis安装和启动[必选]

- 下载安装redis并启动，推荐docker方式，具体请Google或百度

### 数据库安装[必选]

- MySQL或者MariaDB都可以，推荐docker方式，具体请Google或百度
- 创建database `my_daily_cost` 并将 `doc/SQL_STRUCT.sql` 导入即可

### RSA秘钥对创建[必选]

- 针对密码加密使用的RSA秘钥对 开发和生产部署请使用不同的密钥对
- 执行`com.tony.billing.util.RSAEncrypt.main` 生成密钥对
  ```java
  public class RSAEncrypt {
      public static void main(String[] args) {
          // 默认生成到D:/ali/ 具体可以自行修改
          genKeyPair("D:/ali/");
      }
      //....
  }
  ```
- 将生成路径或者密钥对存放路径配置到 `billing-server` 的配置信息中，生产和开发分别在application-release.yml和application-dev.yml中
  ```yaml
  rsa:
    key:
      path: D:/ali/
  ```
- 同时前端项目的config.js中需要修改对应的公钥信息
  ```javascript
  export const rsaPubKeyRelease = '生产用公钥信息';
  export const rsaPubKeyDev = '开发用公钥信息';
  ```
  
### 完成以上步骤后，在IDE中导入项目代码并启动即可
- 先运行 `billing-server` 的 `com.tony.billing.bootstrap.DubboRegistryZooKeeperProviderApplication` 启动服务
- 然后运行 `billing-client` 的 `com.tony.billing.DubboConsumerApplication` 启动客户端

### 打包
- 默认profile是开发环境的，生产打包需要添加参数`-P release`
  ```shell script
  mvn clean install -Dmaven.test.skip -P release
  ```