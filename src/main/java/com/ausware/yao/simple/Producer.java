package com.ausware.yao.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @version 1.0
 * @author： 姚文宇
 * @date： 2021-04-17 18:36
 */
public class Producer {
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
            connection = connectionFactory.newConnection("生产者");
            //3、通过连接获取通道channel
            channel = connection.createChannel();
            //4、通过通道创建交换机、声明队列、绑定关系，发送消息，接收消息
            String queueName = "queue1";
            /**
             * @param1  队列名称
             * @param2  是否持久化
             * @param3  排他性，是否是一个独占队列
             * @param4  是否自动删除，随着最后一个消费者消费完毕消息以后是否自动删除队列
             * @param5
             */
            channel.queueDeclare(queueName,false,false,false,null);
            //5、准备消息内容
            String message = "Hello RabbitMQ";
            //6、发送消息给队列
            channel.basicPublish("",queueName,null,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("消息发送成功");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
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
