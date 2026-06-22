package foodcourt.in.restaurant.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import foodcourt.in.restaurant.dto.AuthResponseDto;
import foodcourt.in.restaurant.dto.DeleteUserDto;
import foodcourt.in.restaurant.dto.LoginRequestDto;
import foodcourt.in.restaurant.dto.UserRequestDto;
import foodcourt.in.restaurant.dto.UserResponseDto;
import foodcourt.in.restaurant.entity.User;
import foodcourt.in.restaurant.entity.UserProfile;
import foodcourt.in.restaurant.repository.UserRepository;
import foodcourt.in.restaurant.security.CustomUserDetailsService;
import foodcourt.in.restaurant.security.JwtService;


@Service
public class UserService {
    UserRepository userRepository;
    AuthenticationManager authenticationManager;
    JwtService jwtService;
    CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    @org.springframework.beans.factory.annotation.Autowired
    UserService(UserRepository userRepository, AuthenticationManager authenticationManager, JwtService jwtService, CustomUserDetailsService customUserDetailsService, PasswordEncoder passwordEncoder)
    {
        this.userRepository=userRepository;
        this.authenticationManager=authenticationManager;
        this.jwtService=jwtService;
        this.customUserDetailsService=customUserDetailsService;
        this.passwordEncoder=passwordEncoder;
    }

    public UserResponseDto getUserData(Long id)
    {
        User user = userRepository.findById(id).
                orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setName(user.getName());
        userResponseDto.setEmail(user.getEmail());
        if (user.getUserProfile() != null) {
            userResponseDto.setPhone(user.getUserProfile().getPhone());
            userResponseDto.setAddress(user.getUserProfile().getAddress());
        }
        return userResponseDto;
    }

    public AuthResponseDto saveUserData(UserRequestDto userRequestDto)
    {
        User user = new User();
        user.setEmail(userRequestDto.getEmail());
        user.setName(userRequestDto.getName());
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));

        UserProfile userProfile = new UserProfile();
        userProfile.setAddress(userRequestDto.getAddress());
        userProfile.setPhone(userRequestDto.getPhoneNumber());
        userProfile.setUser(user);

        user.setUserProfile(userProfile);

        User savedUser = userRepository.save(user);
        //userProfileRepository.save(userProfile);

        AuthResponseDto responseDto = new AuthResponseDto();
        responseDto.setId(savedUser.getId());
        responseDto.setRole(savedUser.getRole());

        return responseDto;
    }

    public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto)
    {
        User user = userRepository.findById(id).
                orElseThrow(()->new RuntimeException("Id do not exist: "+id));
        //update user
        user.setEmail(userRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        user.setName(userRequestDto.getName());

        //update userProfile
        UserProfile userProfile = user.getUserProfile();
        if(userProfile == null) {
            userProfile = new UserProfile();
            userProfile.setUser(user);
            user.setUserProfile(userProfile);
        }
        userProfile.setPhone(userRequestDto.getPhoneNumber());
        userProfile.setAddress(userRequestDto.getAddress());

        userRepository.save(user);
        //create responseDto
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setName(user.getName());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setPhone(userProfile.getPhone());
        userResponseDto.setAddress(userProfile.getAddress());
        
        return userResponseDto;
    }

    public DeleteUserDto deleteUser(Long id)
    {
        User user =userRepository.findById(id).
                orElseThrow(()->new RuntimeException("Invalid Id: "+id));

        userRepository.delete(user);
        return  new DeleteUserDto("Your Account Deleted Successfully");
    }

    public AuthResponseDto validateLogin(LoginRequestDto loginRequestDto)
    {
        // Authenticate using email and password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getEmail(),
                        loginRequestDto.getPassword()
                )
        );

        // If authentication successful, get user details
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(()->new RuntimeException("User not found with email: "+loginRequestDto.getEmail()));

        // Load user details for JWT generation
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequestDto.getEmail());

        // Generate JWT token
        String token = jwtService.generateToken(userDetails);

        // Build response with id, role, and token
        AuthResponseDto responseDto = new AuthResponseDto();
        responseDto.setId(user.getId());
        responseDto.setRole(user.getRole());
        responseDto.setToken(token);

        return responseDto;
    }
}
