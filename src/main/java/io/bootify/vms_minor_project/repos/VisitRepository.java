package io.bootify.vms_minor_project.repos;

import io.bootify.vms_minor_project.domain.User;
import io.bootify.vms_minor_project.domain.Visit;
import io.bootify.vms_minor_project.domain.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;


public interface VisitRepository extends JpaRepository<Visit, Long> {

    Visit findFirstByVisitor(Visitor visitor);

    Visit findFirstByUser(User user);

    boolean existsByFlatId(Long id);

    boolean existsByUserId(Long id);

}
