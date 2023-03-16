
---
# Transfer <img src="./favicon.svg" width="35" height="35">

---
## Transfer Overview
1. 以Dos Web PC 等多客户端进行传输
2. 采用多种零拷贝技术来完成网络数据传输
3. 支持压缩包、文件夹、安装包等文件格式传输

---
## Transfer Client
1. 支持多线程数据传输
2. 支持断点续传


---
## Transfer Server
1. 支持多客户端数据传输
2. 支持多客户端断点续传

---
## Transfer Configuration (settings.properties)
1. `transfer_home` 传输路径
2. `transfer_thread` 线程数量
3. `transfer_pattern` 传输模式

---
## Transfer Configuration Priority
Dos -> Settings -> Code

---
## Transfer Dos Use
1. Server Dos `server ${fileName}`
2. Client Dos `client ${remoteIP}`
3. `${fileName}` is transfer_home in exists
4. `${remoteIP}` is Reachable available





