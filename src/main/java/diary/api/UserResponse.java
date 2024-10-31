package diary.api;

public class UserResponse {
    private Long user_id;

    public UserResponse(Long user_id){
        this.user_id = user_id;
    }

    public long getUser_id() {
        return user_id;
    }
}
