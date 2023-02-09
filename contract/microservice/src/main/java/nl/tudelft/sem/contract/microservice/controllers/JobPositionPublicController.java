package nl.tudelft.sem.contract.microservice.controllers;

import static nl.tudelft.sem.contract.commons.ApiData.INTERNAL_PATH;

import java.net.URI;
import java.util.UUID;
import nl.tudelft.sem.contract.commons.entities.JobPositionDto;
import nl.tudelft.sem.contract.commons.entities.SalaryScaleDto;
import nl.tudelft.sem.contract.commons.entities.utils.StringDto;
import nl.tudelft.sem.contract.microservice.database.entities.JobPosition;
import nl.tudelft.sem.contract.microservice.database.repositories.JobPositionRepository;
import nl.tudelft.sem.contract.microservice.exceptions.ActionNotAllowedException;
import nl.tudelft.sem.contract.microservice.exceptions.JobPositionNotFoundException;
import nl.tudelft.sem.contract.microservice.services.JobPositionService;
import nl.tudelft.sem.contract.microservice.services.RoleAuthenticationService;
import nl.tudelft.sem.user.commons.entities.utils.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/job-position")
public class JobPositionPublicController {
    private final transient JobPositionRepository jobPositionRepository;
    private final transient JobPositionService jobPositionService;
    private final transient RoleAuthenticationService authenticationService;

    /**
     * Constructor.
     *
     * @param authenticationService authentication service
     * @param jobPositionRepository repository for job position
     * @param jobPositionService   service for job position
     */
    public JobPositionPublicController(RoleAuthenticationService authenticationService,
                                       JobPositionRepository jobPositionRepository,
                                       JobPositionService jobPositionService) {
        this.authenticationService = authenticationService;
        this.jobPositionRepository = jobPositionRepository;
        this.jobPositionService = jobPositionService;
    }

    /**
     * Find and get job position from database.
     *
     * @param id id of job position
     * @return job position
     */
    @GetMapping("/{id}")
    ResponseEntity<JobPositionDto> getJobPosition(@PathVariable UUID id) {
        return ResponseEntity.ok(jobPositionRepository.findById(id).orElseThrow(JobPositionNotFoundException::new).getDto());
    }

    /**
     * Add new job position to database.
     *
     * @param jobPositionDto job position to be added
     * @return added job position
     */
    @PostMapping("")
    ResponseEntity<JobPositionDto> addJobPosition(@RequestBody JobPositionDto jobPositionDto) {
        if (!authenticationService.hasAtLeastRole(Role.HR)) {
            throw new ActionNotAllowedException("You do not have the required role to add a job position.");
        }
        JobPosition jobPosition = jobPositionRepository.save(new JobPosition(jobPositionDto));
        return ResponseEntity.created(URI.create(INTERNAL_PATH + "/job-position/" + jobPosition.getId()))
                .body(jobPosition.getDto());
    }

    /**
     * Remove job position from database.
     *
     * @param id id of salary scale to be removed
     * @return removed salary scale
     */
    @DeleteMapping("/{id}/delete")
    ResponseEntity<JobPositionDto> deleteJobPosition(@PathVariable UUID id) {
        if (!authenticationService.hasAtLeastRole(Role.HR)) {
            throw new ActionNotAllowedException("You do not have the required role to delete a job position.");
        }
        JobPositionDto jobPositionDto = jobPositionRepository.findById(id)
                .orElseThrow(JobPositionNotFoundException::new).getDto();
        jobPositionRepository.deleteById(id);
        return ResponseEntity.ok(jobPositionDto);
    }

    /**
     * Change name of job position.
     *
     * @param id id of job position
     * @param name new name
     * @return changed job position
     */
    @PutMapping("/{id}/edit-name")
    ResponseEntity<JobPositionDto> editName(@PathVariable UUID id, @RequestBody StringDto name) {
        if (!authenticationService.hasAtLeastRole(Role.HR)) {
            throw new ActionNotAllowedException("You do not have the required role to edit a job position.");
        }
        JobPosition jobPosition = jobPositionRepository.findById(id).orElseThrow(JobPositionNotFoundException::new);
        return ResponseEntity.ok(jobPositionService.editName(jobPosition, name).getDto());
    }

    /**
     * Change the salary scale connected to the job position.
     *
     * @param id id of job position
     * @param salaryScaleDto new salary scale
     * @return changed job position
     */
    @PutMapping("/{id}/edit-salary-scale")
    ResponseEntity<JobPositionDto> editSalaryScale(@PathVariable UUID id, @RequestBody SalaryScaleDto salaryScaleDto) {
        if (!authenticationService.hasAtLeastRole(Role.HR)) {
            throw new ActionNotAllowedException("You do not have the required role to edit a job position.");
        }
        JobPosition jobPosition = jobPositionRepository.findById(id).orElseThrow(JobPositionNotFoundException::new);
        return ResponseEntity.ok(jobPositionService.editSalaryScale(jobPosition, salaryScaleDto).getDto());
    }

}
