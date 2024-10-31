package diary.service;

import diary.repository.DiaryRepository;
import diary.repository.UserEntity;
import diary.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Transactional
    public void createUser(String nickname, String username, String password){

        // nickname 중복 처리
        if (userRepository.findByNickname(nickname).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다.");
        }

        // 중복이 없는 경우 새로운 User 생성
        UserEntity userEntity = new UserEntity(username, password, nickname);
        userRepository.save(userEntity);
    }

    @Transactional
    public long verifyUser(String nickname, String password){

        // 닉네임으로 유저 조회
        UserEntity userEntity = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "존재하지 않는 사용자입니다."));

        // 비밀번호 검증
        if (!userEntity.getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        // 유저 ID 반환
        return userEntity.getId();
    }
}
