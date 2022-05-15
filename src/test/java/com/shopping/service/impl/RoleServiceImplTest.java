package com.shopping.service.impl;

import com.shopping.domain.exception.AlreadyExistsElementException;
import com.shopping.domain.model.Role;
import com.shopping.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;


    @Test
    public void it_should_save_role() {
        // given
        Role role = Role.builder()
                .id(1)
                .name("USER")
                .build();

        given(roleService.save(any())).willReturn(role);

        // when
        Role savedRole = roleService.save(role);

        // then
        assertNotNull(savedRole, "Returned must not be null");
        assertEquals(role.getId(), savedRole.getId(), "Id must be equal");
        assertEquals(role.getName(), savedRole.getName(), "Name must be equal");
    }

    @Test
    public void it_should_throw_exception_when_save_role_with_existing_name() {
        // given
        Role role = Role.builder()
                .id(1)
                .name("USER")
                .build();

        given(roleRepository.findByName(any())).willReturn(Optional.ofNullable(role));

        // when
        Throwable throwable = catchThrowable(() -> {
            roleService.save(role);
        });

        // then
        verify(roleRepository, times(1)).findByName(any());
        assertThat(throwable).isInstanceOf(AlreadyExistsElementException.class);

    }
}
