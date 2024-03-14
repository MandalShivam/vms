package io.bootify.vms_minor_project.repos;

import io.bootify.vms_minor_project.domain.Address;
import io.bootify.vms_minor_project.domain.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;


public interface VisitorRepository extends JpaRepository<Visitor, Long> {

    Visitor findFirstByAdress(Address address);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByPhoneNoIgnoreCase(String phoneNo);

    boolean existsByIdNumberIgnoreCase(String idNumber);

}
