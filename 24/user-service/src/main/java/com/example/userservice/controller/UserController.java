package com.example.userservice.controller;

import com.example.userservice.dto.UserDto;
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

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final ModelMapper mapper;

    @GetMapping("/health-check")
    public String status() {
        return "It's Working in User Service";
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

}
