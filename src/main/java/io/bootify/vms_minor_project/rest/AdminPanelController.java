package io.bootify.vms_minor_project.rest;

import io.bootify.vms_minor_project.model.AddressDTO;
import io.bootify.vms_minor_project.model.UserDTO;
import io.bootify.vms_minor_project.model.UserStatus;
import io.bootify.vms_minor_project.service.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminPanelController {

    @Autowired
    private UserService userService;

    Logger LOGGER = LoggerFactory.getLogger(AdminPanelController.class);

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createUser(@RequestBody @Valid final UserDTO userDTO) {
        final Long createdId = userService.create(userDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/markUserInActive/{id}")
    public ResponseEntity<Void> markUserInActive(@PathVariable("id") Long userId) {
        userService.markUserInActive(userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/markUserActive/{id}")
    public ResponseEntity<Void> markUserActive(@PathVariable("id") Long userId) {
        userService.markUserActive(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/allUsers")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PostMapping("/user-csv-upload")
    public ResponseEntity<List<String>> uploadFileForUserCreation(@RequestParam("file") MultipartFile file) {
        LOGGER.info("File available : {}" + file.getName());
        List<String> response = new ArrayList<String>();

        BufferedReader fileReader = null;
        try {
            fileReader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for(CSVRecord csvRecord : csvRecords) {
                UserDTO userDTO = new UserDTO();
                userDTO.setName(csvRecord.get("name"));
                userDTO.setEmail(csvRecord.get("email"));
                userDTO.setPhoneNo(csvRecord.get("phoneNo"));
                userDTO.setRole(csvRecord.get("role"));
                userDTO.setStatus(UserStatus.ACTIVE);
                userDTO.setFlatNumber(csvRecord.get("flatNumber"));

                AddressDTO addressDTO = new AddressDTO();
                addressDTO.setLine1(csvRecord.get("line1"));
                addressDTO.setLin2(csvRecord.get("line2"));
                addressDTO.setCity(csvRecord.get("city"));
                addressDTO.setCountry(csvRecord.get("country"));
                addressDTO.setPincode(csvRecord.get("pincode"));
                userDTO.setAddress(addressDTO);

                try{
                    Long id = userService.create(userDTO);
                    response.add("Created User "+userDTO.getName()+"with id"+id);
                } catch(Exception ex) {
                    response.add("Unable to create user"+ userDTO.getName()+" msg:" + ex.getMessage());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return ResponseEntity.ok(response);
    }
}
