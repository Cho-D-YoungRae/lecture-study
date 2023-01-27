package com.example.userservice.controller;

import com.example.userservice.dto.UserDto;
import com.example.userservice.entity.UserEntity;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.RequestUser;
import com.example.userservice.vo.ResponseUser;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

//@RequestMapping("/user-service")  // API Gateway 에 RewritePath filter 추가함으로써 제거 가능
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final ModelMapper mapper;

    @GetMapping("/health-check")
    public String status(@Value("${local.server.port}") int port) {
        return "It's Working in User Service on PORT " + port;
    }

    @GetMapping("/welcome")
    public String welcome(@Value("${greeting.message}") String message) {
        return message;
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody @Valid RequestUser user) {
        UserDto userDto = mapper.map(user, UserDto.class);
        userService.createUser(userDto);
        ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseUser);
    }

    @GetMapping("/users")
    public ResponseEntity<List<ResponseUser>> getUsers() {
        Iterable<UserEntity> userList = userService.getAllUser();

        List<ResponseUser> result = new ArrayList<>();
        userList.forEach(v -> {
            result.add(mapper.map(v, ResponseUser.class));
        });

        return ResponseEntity.ok(result);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseUser> getUser(@PathVariable("userId") String userId) {
        UserDto userDto = userService.getUserByUserId(userId);
        ResponseUser returnValue = mapper.map(userDto, ResponseUser.class);
        return ResponseEntity.ok(returnValue);
    }

}
