package nl.tudelft.sem.notification.commons.entities;

import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.notification.commons.entities.utils.Dto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto implements Dto {
    private UUID id;
    @NotNull
    private UUID userId;

    @NotBlank
    @NotNull
    private String fromEmail = "noreply@example.com";

    @NotNull
    private MessagePriority priority = MessagePriority.LOW;

    private boolean sent = false;

    @NotNull
    @NotBlank
    private String subject;

    @NotNull
    @NotBlank
    private String message;

    public static MessageDtoBuilder builder() {
        return new MessageDtoBuilder();
    }

    public static class MessageDtoBuilder {
        private transient UUID id;
        private transient @NotNull UUID userId;
        private transient @NotBlank @NotNull String fromEmail;
        private transient @NotNull MessagePriority priority;
        private transient boolean sent;
        private transient @NotNull @NotBlank String subject;
        private transient @NotNull @NotBlank String message;

        private MessageDtoBuilder() {
        }

        public MessageDtoBuilder setId(UUID id) {
            this.id = id;
            return this;
        }

        public MessageDtoBuilder setUserId(@NotNull UUID userId) {
            this.userId = userId;
            return this;
        }

        public MessageDtoBuilder setFromEmail(@NotBlank @NotNull String fromEmail) {
            this.fromEmail = fromEmail;
            return this;
        }

        public MessageDtoBuilder setPriority(@NotNull MessagePriority priority) {
            this.priority = priority;
            return this;
        }

        public MessageDtoBuilder setSent(boolean sent) {
            this.sent = sent;
            return this;
        }

        public MessageDtoBuilder setSubject(@NotNull @NotBlank String subject) {
            this.subject = subject;
            return this;
        }

        public MessageDtoBuilder setMessage(@NotNull @NotBlank String message) {
            this.message = message;
            return this;
        }

        public MessageDto build() {
            return new MessageDto(id, userId, fromEmail, priority, sent, subject, message);
        }
    }
}
