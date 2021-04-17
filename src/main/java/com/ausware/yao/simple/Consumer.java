package com.ausware.yao.simple;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @version 1.0
 * @author： 姚文宇
 * @date： 2021-04-17 18:36
 */
public class Consumer {
    public static void main(String[] args) {
        //所有的中间件技术都是基于tcp/ip协议之上构建的新型的协议规范，只不过rabbitmq遵循的是amqp
        //ip 端口

        //1、创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.98.135");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setVirtualHost("/");
        //2、创建连接connection
        Connection connection = null;
        Channel channel = null;
        try {
            connection = connectionFactory.newConnection("消费者");
            //3、通过连接获取通道channel
            channel = connection.createChannel();
            //4、通过通道创建交换机、声明队列、绑定关系，发送消息，接收消息

            channel.basicConsume("queue1", true, new DeliverCallback() {
                        @Override
                        public void handle(String consumerTag, Delivery delivery) throws IOException {
                            System.out.println("收到消息是：" + new String(delivery.getBody(), "UTF-8"));
                        }
                    }, new CancelCallback() {
                        @Override
                        public void handle(String consumerTag) throws IOException {
                            System.out.println("消息接收失败了....");
                        }
                    });
            System.out.println("开始接受消息");
            System.in.read();

        } catch (
                IOException e) {
            e.printStackTrace();
        } catch (
                TimeoutException e) {
            e.printStackTrace();
        }finally {
            //7、关闭连接，关闭通道
            if (channel != null && channel.isOpen()){
                try {
                    channel.close();
                } catch (IOException | TimeoutException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null && connection.isOpen()){
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
