# 使用 hseeberger/scala-sbt 镜像，它已经包含了必要的 JDK、Scala 和 sbt
FROM hseeberger/scala-sbt:8u222_1.3.5_2.13.1

# 设置工作目录
WORKDIR /app

# 复制项目文件到容器中
COPY . .

# 使用 sbt 编译和打包项目
RUN sbt stage

# 暴露应用运行的端口
EXPOSE 9000

# 设置容器启动时执行的命令
CMD ["target/universal/stage/bin/examine-demo", "-Dplay.http.secret.key=changeme"]
