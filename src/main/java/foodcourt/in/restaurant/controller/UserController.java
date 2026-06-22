package foodcourt.in.restaurant.controller;

import foodcourt.in.restaurant.dto.*;
import foodcourt.in.restaurant.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService service;
    UserController(UserService service){
        this.service=service;
    }

    @GetMapping("/users/{id}")
    public UserResponseDto getUser(@PathVariable("id") Long id)
    {
        return service.getUserData(id);
    }

    @PostMapping("/users/register")
    public AuthResponseDto createUser(@RequestBody @Valid UserRequestDto userRequestDto)
    {
        return service.saveUserData(userRequestDto);
    }

    @PostMapping("/users/login")
    public AuthResponseDto loginUser(@RequestBody @Valid LoginRequestDto dto)
    {
        AuthResponseDto responseDto = service.validateLogin(dto);
        return responseDto;
    }

    @PutMapping("/users/{id}")
    public UserResponseDto updateProfile(@PathVariable("id") Long id , @RequestBody @Valid UserRequestDto requestDto)
    {
        return service.updateUser(id,requestDto);
    }

    @DeleteMapping("/users/{id}")
    public DeleteUserDto deleteAcc(@PathVariable("id") Long id)
    {
        return service.deleteUser(id);
    }
}
