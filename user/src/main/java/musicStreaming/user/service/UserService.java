package musicStreaming.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import musicStreaming._global.event.SignUpCompleted;
import musicStreaming._global.event.UserNameUpdated;
import musicStreaming._global.security.JwtTokenService;

import musicStreaming.domain.User;
import musicStreaming.domain.UserRepository;
import musicStreaming.user.UserNotFoundException;
import musicStreaming.user.reqDtos.SignInReqDto;
import musicStreaming.user.reqDtos.SignUpReqDto;
import musicStreaming.user.reqDtos.UpdateNameReqDto;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenService jwtTokenService;
    private final PasswordEncoder passwordEncoder; // Spring Security에서 사용하는 PasswordEncoder를 활용하기 위해서

    
    public User signUp(SignUpReqDto signUpReqDto) {
        User savedUser = this.userRepository.save(
                User.builder()
                    .email(signUpReqDto.getEmail())
                    .password(this.passwordEncoder.encode(signUpReqDto.getPassword()))
                    .name(signUpReqDto.getName())
                    .role("User")
                    .build()
            );

        SignUpCompleted signUpCompleted = new SignUpCompleted(savedUser);
        signUpCompleted.publishAfterCommit();
        
        return savedUser;
    }

    public String tokenBySignIn(SignInReqDto signInReqDto) {
        return this.jwtTokenService.tokenValue(signInReqDto);
    }


    public void updateName(@NonNull Long userId, UpdateNameReqDto updateNameReqDto) {
        User userToUpdate = this.userRepository.findById(userId)
                                .orElseThrow(() -> new UserNotFoundException());
        
        userToUpdate.setName(updateNameReqDto.getName());
        User savedUser = this.userRepository.save(userToUpdate);

        
        (new UserNameUpdated(savedUser)).publishAfterCommit();
    }
}
