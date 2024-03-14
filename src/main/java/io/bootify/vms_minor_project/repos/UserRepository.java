package io.bootify.vms_minor_project.repos;

import io.bootify.vms_minor_project.domain.Address;
import io.bootify.vms_minor_project.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {

    User findFirstByAddress(Address address);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByPhoneNoIgnoreCase(String phoneNo);

}
