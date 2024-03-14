package io.bootify.vms_minor_project.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class VisitDTO {

    private Long id;

    @NotNull
    private VisitStatus status;

    private LocalDateTime inTime;

    private LocalDateTime outTime;

    @Size(max = 255)
    private String urlOfImage;

    @NotNull
    @Size(max = 255)
    private String purpose;

    @NotNull
    private Integer noOfPeople;

    private Long visitor;

    @VisitFlatUnique
    private Long flat;

    @VisitUserUnique
    private Long user;

}
