package pt.seixal.carlos.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pt.seixal.carlos.model.Order;

@Service
public class EmailNotificationService {
    private static final Logger log = LoggerFactory.getLogger(EmailNotificationService.class);

    public void sendCompletionEmail(Order order) {
        // Simulação do envio de e-mail conforme especificação de log
        String message = String.format("Olá %s, a sua encomenda do item '%s' (Qtd: %d) foi concluída com sucesso!",
                order.getUser().getName(), order.getItem().getName(), order.getQuantity());

        log.info("[EMAIL SENT] Para: {} | Conteúdo: {}", order.getUser().getEmail(), message);
    }
}
