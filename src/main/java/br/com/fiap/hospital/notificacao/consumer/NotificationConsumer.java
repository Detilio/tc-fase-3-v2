//package br.com.fiap.hospital.notificacao.consumer;
//
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
//@Component
//public class NotificationConsumer {
//    @RabbitListener(queues = "consultas-event")
//    public void receberMensagem(String mensagem) {
//        System.out.println("📢 Notificação recebida: " + mensagem);
//    }
//}
