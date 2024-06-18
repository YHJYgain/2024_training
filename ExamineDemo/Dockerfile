# 使用官方的 OpenJDK 8 JDK 作为基础镜像
FROM openjdk:8-jdk

# 设置工作目录
WORKDIR /app

# 复制并解压缩打包好的 zip 文件
COPY target/universal/examinedemo-1.0-SNAPSHOT.zip /app/

# 安装 unzip 工具并解压 zip 文件
RUN apt-get update && apt-get install -y unzip && \
    unzip examinedemo-1.0-SNAPSHOT.zip && \
    rm examinedemo-1.0-SNAPSHOT.zip

# 使脚本可执行
RUN chmod +x /app/examinedemo-1.0-SNAPSHOT/bin/examinedemo

# 暴露应用端口
EXPOSE 9000

# 运行应用程序（堆内存最多 4GB，使用 G1 作为垃圾回收器）
ENTRYPOINT ["/app/examinedemo-1.0-SNAPSHOT/bin/examinedemo", "-J-Xmx4G", "-J-XX:+UseG1GC"]