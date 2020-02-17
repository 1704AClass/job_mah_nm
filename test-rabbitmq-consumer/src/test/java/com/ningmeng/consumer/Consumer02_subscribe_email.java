package com.ningmeng.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Consumer02_subscribe_email {

    //队列名称
    //发送邮件
    private static final String QUEUE_INFORM_EMAIL = "queue_inform_email";

    //交换器
    private static final String EXCHANGE_FANOUT_INFORM="exchange_fanout_inform";

    public static void main(String[] args) {
        try{
            //创建连接工厂
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("127.0.0.1");
            //浏览器端口为15672  后台为5672
            factory.setPort(5672);
            factory.setUsername("guest");
            factory.setPassword("guest");
            factory.setVirtualHost("/");
            //rabbitmq默认虚拟机名称为“/”，虚拟机相当于一个独立的mq服务 器            
            //创建与RabbitMQ服务的TCP连接         
            Connection connection = factory.newConnection();
            //创建与Exchange的通道，每个连接可以创建多个通道，每个通道代表一个会话任务             
            Channel channel = connection.createChannel();
            /**              
             * 声明队列，如果Rabbit中没有此队列将自动创建
             * param1:队列名称
             * param2:是否持久化
             * param3:队列是否独占此连接
             * param4:队列不再使用时是否自动删除此队列
             * param5:队列参数
             */
            channel.queueDeclare(QUEUE_INFORM_EMAIL, true, false, false, null);

            //交换机和队列绑定String queue, String exchange 交换机名称, String routingKey 交换机类型    
            channel.exchangeDeclare(EXCHANGE_FANOUT_INFORM, BuiltinExchangeType.FANOUT);
            /**             
             *  参数明细
             *  1、队列名称
             *  2、交换机名称
             *  3、路由key
             */
            //发布订阅不需要路由
            channel.queueBind(QUEUE_INFORM_EMAIL,EXCHANGE_FANOUT_INFORM,"");
            //定义消费方法         
            DefaultConsumer consumer = new DefaultConsumer(channel) {
                /**              
                 * 消费者接收消息调用此方法              
                 *  @param consumerTag 消费者的标签，在channel.basicConsume()去指定      
                 *  @param envelope 消息包的内容，可从中获取消息id，消息routingkey，交换机，消息和重传标志 (收到消息失败后是否需要重新发送)           
                 *   @param properties              
                 *   @param body             
                 *   @throws IOException
                 */
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    //交换机                 
                    String exchange = envelope.getExchange();
                    //路由key               
                    String routingKey = envelope.getRoutingKey();
                    //消息id    
                    long deliveryTag = envelope.getDeliveryTag();
                    //消息内容                
                    String msg = new String(body,"utf-8");
                    System.out.println("receive message.." + msg);
                }
            };
            channel.basicConsume(QUEUE_INFORM_EMAIL, true, consumer);
        }catch(Exception e){
            e.printStackTrace();
        }


    }
}
