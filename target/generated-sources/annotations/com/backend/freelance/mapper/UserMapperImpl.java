package com.backend.freelance.mapper;

import com.backend.freelance.dtos.UserDto;
import com.backend.freelance.models.User;
import com.backend.freelance.request.UserCreateRequest;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-13T21:58:21+0700",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 21.0.2 (GraalVM Community)"
)
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setPublicId( user.getPublicId() );
        userDto.setUsername( user.getUsername() );
        userDto.setEmail( user.getEmail() );
        userDto.setExpiredAt( user.getExpiredAt() );
        userDto.setCreatedAt( user.getCreatedAt() );

        return userDto;
    }

    @Override
    public User toEntity(UserCreateRequest request) {
        if ( request == null ) {
            return null;
        }

        User user = new User();

        user.setUsername( request.getUsername() );
        user.setEmail( request.getEmail() );
        user.setPassword( request.getPassword() );

        return user;
    }
}
