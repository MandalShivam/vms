package io.bootify.vms_minor_project.repos;

import io.bootify.vms_minor_project.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AddressRepository extends JpaRepository<Address, Long> {
}
