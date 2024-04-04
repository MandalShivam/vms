package io.bootify.vms_minor_project.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    @Size(max = 255)
  //  @UserEmailUnique
    private String email;

    @NotNull
    @Size(max = 255)
    //@UserPhoneNoUnique
    private String phoneNo;

    @NotNull
    @Size(max = 255)
    private String role;

    @NotNull
    private UserStatus status;

    private String flatNumber;

    private AddressDTO address;

}
