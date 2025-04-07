package com.pg.mbti;

import com.pg.mbti.controllers.RegistrationController;
import com.pg.mbti.dto.RegistrationRequestDto;
import com.pg.mbti.dto.RegistrationResponseDto;
import com.pg.mbti.entity.User;
import com.pg.mbti.exceptions.EmailSendingFailedException;
import com.pg.mbti.exceptions.InvalidTokenException;
import com.pg.mbti.exceptions.ResourceNotFoundException;
import com.pg.mbti.exceptions.UserAlreadyExistsException;
import com.pg.mbti.mappers.RegistrationMapper;
import com.pg.mbti.services.RegistrationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationControllerTest {

    @Mock
    private RegistrationService registrationService;

    @Mock
    private RegistrationMapper registrationMapper;

    @InjectMocks
    private RegistrationController registrationController;


}