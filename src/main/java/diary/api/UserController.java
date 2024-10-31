package diary.api;

import diary.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/auth/signup")
    ResponseEntity<String> signUp(@RequestBody UserRequest userRequest){

        userService.createUser(userRequest.nickname(), userRequest.username(), userRequest.password());

        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입을 환영합니다.");
    }

    @PostMapping("/auth/login")
    ResponseEntity<UserResponse> login(@RequestBody UserRequest userRequest){

        Long user_id = userService.verifyUser(userRequest.nickname(), userRequest.password());

        return ResponseEntity.ok(new UserResponse(user_id));
    }

}