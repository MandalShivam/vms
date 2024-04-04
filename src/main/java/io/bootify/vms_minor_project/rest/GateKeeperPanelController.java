package io.bootify.vms_minor_project.rest;

import io.bootify.vms_minor_project.model.VisitDTO;
import io.bootify.vms_minor_project.model.VisitorDTO;
import io.bootify.vms_minor_project.service.VisitService;
import io.bootify.vms_minor_project.service.VisitorService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gateKeeper")
public class GateKeeperPanelController {

    @Autowired
    private VisitorService visitorService;

    @Autowired
    private VisitService visitService;

    @PostMapping("/createVisitor")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createVisitor(@RequestBody @Valid final VisitorDTO visitorDTO) {
        final Long createdId = visitorService.create(visitorDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PostMapping("/createVisit")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createVisit(@RequestBody @Valid final VisitDTO visitDTO) throws BadRequestException {
        final Long createdId = visitService.create(visitDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/markEntry/{visitId}")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Void> markEntry(@PathVariable @Valid Long visitId) {
        visitService.updateInTime(visitId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/markExit/{visitId}")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Void> markExit(@PathVariable @Valid Long visitId) {
        visitService.updateOutTime(visitId);
        return ResponseEntity.ok().build();
    }

}
