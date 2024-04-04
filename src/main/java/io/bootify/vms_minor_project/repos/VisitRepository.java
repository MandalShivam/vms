package io.bootify.vms_minor_project.repos;

import io.bootify.vms_minor_project.domain.User;
import io.bootify.vms_minor_project.domain.Visit;
import io.bootify.vms_minor_project.domain.Visitor;
import io.bootify.vms_minor_project.model.VisitStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface VisitRepository extends JpaRepository<Visit, Long> {

    Visit findFirstByVisitor(Visitor visitor);

    Visit findFirstByUser(User user);

    boolean existsByFlatId(Long id);

    boolean existsByUserId(Long id);

    List<Visit> findByStatus(VisitStatus status);

}
