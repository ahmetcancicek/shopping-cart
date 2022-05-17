package com.shopping.repository;

import com.shopping.domain.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RoleRepositoryTest extends BaseRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void it_should_save_role() {
        // given
        Role role = Role.builder()
                .name("MODERATOR")
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
    public void it_should_throw_exception_when_save_role_with_existing_name() {
        // given
        Role roleOne = Role.builder()
                .name("EDITOR")
                .build();

        Role roleTwo = Role.builder()
                .name("EDITOR")
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
