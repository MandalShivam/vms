package io.bootify.vms_minor_project.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class VisitorDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    @Size(max = 255)
    @VisitorEmailUnique
    private String email;

    @NotNull
    @Size(max = 255)
    @VisitorPhoneNoUnique
    private String phoneNo;

    @NotNull
    @Size(max = 255)
    @VisitorIdNumberUnique
    private String idNumber;

    private AddressDTO address;

}
