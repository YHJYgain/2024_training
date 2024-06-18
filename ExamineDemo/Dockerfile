# 使用 hseeberger/scala-sbt 镜像，它已经包含了必要的 JDK、Scala 和 sbt
FROM hseeberger/scala-sbt:8u242_1.3.10_2.11.12

# 设置工作目录
WORKDIR /app

# 复制项目文件到容器中
COPY . .

# 打包本地项目
RUN sbt dist

# 安装 unzip 工具并解压 zip 文件
RUN apt-get update &&  \
    apt-get install -y unzip && \
    unzip target/universal/examinedemo-1.0-SNAPSHOT.zip -d /app && \
    rm target/universal/examinedemo-1.0-SNAPSHOT.zip

# 使脚本可执行
RUN chmod +x /app/examinedemo-1.0-SNAPSHOT/bin/examinedemo

# 暴露应用端口
EXPOSE 9000

# 运行应用程序
ENTRYPOINT ["/app/examinedemo-1.0-SNAPSHOT/bin/examinedemo"]