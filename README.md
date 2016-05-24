# jpcap4router

dealing routing protocol package with jpcap

##简介

鉴于jpcap不提供路由协议包的解析类而c语言的quagga又在路由数据包分析和发送上过于复杂，因此进行基于jpcap的路由协议包解析和构造代码的开发。

##结构

###file projectLog
 - TODO list 
 - LOG list 一些环境问题的解决办法

###package util
 - 提供各类校验和计算方法
 - 提供常用数据结构的输出方法
 - 提供数据结构间转换方法

###package routerCapture
 - 程序入口

###package protocolPacket
 - 提供路由协议包基类
 - 提供路由数据包生成方法

###package protocolPacket.OSPF
 - 提供OSPF协议定义的各种数据包解析和生成方法
 - 提供OSPF各种数据包类

###package packetTool
 - 提供自定义生成各种数据包的流程控制

###package jpcap_util
 - 提供jpcap receiver的回调
 - 使用单例模式封装了jpcap

###arp_searcher
 - 利用arp协议获取ip对应mac的工具类
