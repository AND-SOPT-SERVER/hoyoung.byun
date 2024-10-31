package diary.api;


public record  UserRequest(
        Long id,
        String username,
        String password,
        String nickname
) {
}