package com.shopping.integration.repository;

import com.shopping.domain.model.Role;
import com.shopping.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

public class RoleRepositoryIT extends AbstractIT {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void it_should_save_role() {
        // given
        Role role = Role.builder()
                .name("ROLE_MODERATOR")
                .build();

        // when
        Role savedRole = roleRepository.save(role);
        Role expectedRole = testEntityManager.find(Role.class, savedRole.getId());

        // then
        assertNotNull(savedRole, "Returned must not be null");
        assertEquals(savedRole.getId(), expectedRole.getId(), "Id must be equal");

        testEntityManager.remove(role);
        testEntityManager.flush();
    }

    @Test
    public void it_should_return_role() {
        // given
        Role role = Role.builder()
                .name("ROLE_EDITOR")
                .build();

        testEntityManager.persistAndFlush(role);

        // when
        Optional<Role> expectedRole = roleRepository.findByName(role.getName());

        // then
        assertTrue(expectedRole.isPresent(), "Returned must not be null");
        assertEquals(role.getName(), expectedRole.get().getName(), "Name must be equal");

        testEntityManager.remove(role);
        testEntityManager.flush();
    }

    @Test
    public void it_should_throw_exception_when_save_role_with_existing_name() {
        // given
        Role roleOne = Role.builder()
                .name("ROLE_EDITOR")
                .build();

        Role roleTwo = Role.builder()
                .name("ROLE_EDITOR")
                .build();

        testEntityManager.persistAndFlush(roleOne);

        // when
        Throwable throwable = catchThrowable(() -> {
            roleRepository.saveAndFlush(roleTwo);
        });

        // then
        assertThat(throwable).isInstanceOf(DataIntegrityViolationException.class);
    }
}
