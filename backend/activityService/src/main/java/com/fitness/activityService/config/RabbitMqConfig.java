package com.fitness.activityService.config;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    @Bean
    public Queue activityQueue(){
        return new Queue("activity.queue",true);  //true means if rabitmq restart the queue remains it doesnt goes off
    }

    @Bean
    public DirectExchange activityExchange(){
        return new DirectExchange("fitness.exchange");
    }
    @Bean
    public Binding activityBinding(Queue activityQueue,DirectExchange activityExchange){
        return BindingBuilder.bind(activityQueue).to(activityExchange).with("activity.tracking");
    }

    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}
