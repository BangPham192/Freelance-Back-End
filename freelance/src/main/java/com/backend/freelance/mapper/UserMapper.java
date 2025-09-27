package com.backend.freelance.mapper;

import com.backend.freelance.models.User;
import com.backend.freelance.request.UserCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toEntity(UserCreateRequest request);

}
