#!/bin/bash
set -e

# 运行 sbt 打包命令
sbt dist

# 删除之前构建的同名容器（如果存在）
if [ "$(docker ps -aq -f name=examine-demo-container)" ]; then
    docker rm -f examine-demo-container
fi

# 删除之前构建的同名镜像（如果存在）
if [ "$(docker images -q examine-demo:v1.0)" ]; then
    docker rmi examine-demo:v1.0
fi

# 构建 Docker（版本请自行修改）
docker build -t examine-demo:v1.0 .

# 启动 Docker 容器
docker run -d -p 9000:9000 --name examine-demo-container examine-demo:v1.0
