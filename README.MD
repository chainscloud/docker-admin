# 项目背景
DaoCloud 网站开始停用DCS，只能自己做一个了

#
加速器2
https://a64gnqd2.mirror.aliyuncs.com


https://gofrp.org/docs/examples/stcp/


需要持久化 frpc.ini




# TODO 
## 
让作业节点自行拉取代码， 需要agent 支持
注意：应该可以使用frp locations 功能，这样少配置一个端口
git clone depth 优化代码下载大小

## 
使用嵌入式数据库


构建主机必须是用加速器，如阿里或者daocloud的加速器


docker run -d --name docker-agent \
    -v /var/run/docker.sock:/var/run/docker.sock \
    -e hostname=qylc-test-2 -e server_addr=39.105.74.223 -e server_port=7700 \
    registry.cn-hangzhou.aliyuncs.com/chainscloud/docker-agent
    
    
# 代码
代码放大gitlab上了


# TODO 
端口冲突时，没有提示

删除流水线时，同时删除硬盘上的日志

冲突检测

取消构建

GIT lab SDK

标准的注册中心 SDK

帐号权限


项目的bug数量统计


删除主机，删除前，判断是否已经卸载监控程序