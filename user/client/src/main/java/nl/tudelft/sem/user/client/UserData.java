package nl.tudelft.sem.user.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.user.commons.entities.utils.RoleDto;
import nl.tudelft.sem.user.commons.entities.utils.UserDto;
import nl.tudelft.sem.user.commons.entities.utils.UuidDto;

@RequiredArgsConstructor
public class UserData {
    /**
     * Client instance to use (need to access its HTTP client).
     */
    @NonNull
    private final transient UserClient client;

    /**
     * Get a user by its ID.
     *
     * @param userId the user ID to get.
     * @return a future that will contain the user.
     */
    public CompletableFuture<UserDto> getuser(@NonNull UUID userId) {
        return client.get("/user/" + userId.toString(), UserDto.class);
    }

    public CompletableFuture<UserDto> createUser(@NonNull String netId) throws JsonProcessingException {

        return client.post("/user/saveUser/" + netId, null, UserDto.class);
    }

    public CompletableFuture<RoleDto> getUserRoleUuid(@NonNull UUID id) {
        return client.get("/user/getRoleByUUID/" + id.toString(), RoleDto.class);
    }

    public CompletableFuture<RoleDto> getUserRoleNetId(@NonNull String netId) {
        return client.get("/user/getRoleByNetId/" + netId, RoleDto.class);
    }

    public CompletableFuture<UuidDto> getUuid(@NonNull String netId) {
        return client.get("/user/getUUID/" + netId, UuidDto.class);
    }
}
