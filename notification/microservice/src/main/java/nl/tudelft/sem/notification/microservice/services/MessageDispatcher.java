package nl.tudelft.sem.notification.microservice.services;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import nl.tudelft.sem.notification.commons.entities.MessageDto;
import nl.tudelft.sem.notification.microservice.database.entities.MessageEntity;
import nl.tudelft.sem.notification.microservice.database.repositories.MessageEntityRepository;
import nl.tudelft.sem.notification.microservice.utils.MessageQueueEntry;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageDispatcher {
    private final transient MessageEntityRepository messageEntityRepository;
    private final transient JavaMailSender mailSender;
    private final transient PriorityBlockingQueue<MessageQueueEntry> messageQueue = new PriorityBlockingQueue<>();

    public MessageDispatcher(MessageEntityRepository messageEntityRepository, JavaMailSender mailSender) {
        this.messageEntityRepository = messageEntityRepository;
        this.mailSender = mailSender;
    }

    /**
     * Send a message to a user.
     *
     * @param messageDto the message to send
     * @return the UUID of the message
     */
    public UUID addMessageToQueue(@Valid @NonNull MessageDto messageDto) {
        MessageEntity messageEntity = messageEntityRepository.save(new MessageEntity(messageDto));
        if (!messageDto.isSent()) {
            messageQueue.add(new MessageQueueEntry(messageEntity));
        }
        return messageEntity.getId();
    }

    /**
     * Add a list of messages to the queue.
     *
     * @param messageDtos the messages to add to the queue.
     * @return the UUIDs of the messages that were added to the queue.
     */
    public List<UUID> addMessagesToQueue(@NonNull Collection<@NonNull @Valid MessageDto> messageDtos) {
        Collection<MessageEntity> messageEntities = messageEntityRepository.saveAll(
                messageDtos.stream().map(MessageEntity::new).collect(Collectors.toList()));
        this.messageQueue.addAll(
                messageEntities.stream()
                        .filter(m -> !m.isSent())
                        .map(MessageQueueEntry::new)
                        .collect(Collectors.toList()));
        return messageEntities.stream().map(MessageEntity::getId).collect(Collectors.toList());
    }

    /**
     * Send all messages in the queue.
     */
    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.MINUTES)
    public void sendMessages() {
        while (!messageQueue.isEmpty()) {
            MessageQueueEntry messageQueueEntry = messageQueue.poll();
            MessageEntity messageEntity = messageEntityRepository.findById(messageQueueEntry.getId()).orElse(null);
            if (messageEntity == null) {
                log.warn("Received null message entity from database");
                continue;
            }

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            // Get user email
            mailMessage.setTo("example@example.com");
            mailMessage.setSubject(messageEntity.getSubject());
            mailMessage.setText(messageEntity.getMessage());
            try {
                mailSender.send(mailMessage);
                messageEntity.setSent(true);
                messageEntityRepository.save(messageEntity);
            } catch (Exception e) {
                log.error("Failed to send email", e);
            }
        }
    }

    /**
     * Get the size of the message queue.
     *
     * @return Size of the message queue.
     */
    public int getQueueSize() {
        return messageQueue.size();
    }
}
