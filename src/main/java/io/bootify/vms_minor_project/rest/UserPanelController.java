package io.bootify.vms_minor_project.rest;

import io.bootify.vms_minor_project.model.VisitDTO;
import io.bootify.vms_minor_project.service.VisitService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/user")
public class UserPanelController {
    @Autowired
    private VisitService visitService;

    @PutMapping("/approveVisit/{visitId}")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Void> approveVisit(@PathVariable @Valid Long visitId) {
        visitService.approve(visitId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/rejectVisit/{visitId}")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Void> rejectVisit(@PathVariable @Valid Long visitId) {
        visitService.reject(visitId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/allPendingVisits/{visitId}")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<List<VisitDTO>> getallPendingVisits() {
        visitService.getAllWaitingVisits();
        return ResponseEntity.ok().build();
    }


}
