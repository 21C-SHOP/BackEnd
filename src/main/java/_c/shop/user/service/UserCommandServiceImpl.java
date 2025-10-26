package _c.shop.user.service;

import _c.shop.apiPayload.code.status.ErrorStatus;
import _c.shop.apiPayload.exception.ExceptionHandler;
import _c.shop.jwt.service.JwtService;
import _c.shop.user.domain.User;
import _c.shop.user.dto.UserRequestDto;
import _c.shop.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCommandServiceImpl implements UserCommandService {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public void createUserInfo(String accessToken, UserRequestDto.InitUserInfoDto userInfoDto) {
        String email = jwtService.extractEmail(accessToken).orElseThrow(() ->
                new ExceptionHandler(ErrorStatus._ACCESSTOKEN_NOT_FOUND));
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new ExceptionHandler(ErrorStatus._USER_NOT_FOUND));
        user.updateUserInfo(userInfoDto);
    }

    @Transactional
    @Override
    public void updateUserInfo(String accessToken, UserRequestDto.UpdateUserInfoDto updateUserInfoDto) {
        String email = jwtService.extractEmail(accessToken).orElseThrow(() ->
                new ExceptionHandler(ErrorStatus._ACCESSTOKEN_NOT_FOUND));
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new ExceptionHandler(ErrorStatus._USER_NOT_FOUND));
        user.updateUserInfo(updateUserInfoDto);
    }
}
