package com.huumod.identity.mapper;

import com.huumod.identity.dto.request.UserCreationRequest;
import com.huumod.identity.dto.response.UserResponse;
import com.huumod.identity.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", ignore = true)
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);
}
