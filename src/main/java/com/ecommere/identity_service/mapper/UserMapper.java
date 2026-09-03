package com.ecommere.identity_service.mapper;

import com.ecommere.identity_service.dto.request.UserCreationRequest;
import com.ecommere.identity_service.dto.response.UserResponse;
import com.ecommere.identity_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", ignore = true)
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);
}
