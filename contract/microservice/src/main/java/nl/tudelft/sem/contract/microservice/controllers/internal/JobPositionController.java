package nl.tudelft.sem.contract.microservice.controllers.internal;

import static nl.tudelft.sem.contract.commons.ApiData.INTERNAL_PATH;

import java.net.URI;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import nl.tudelft.sem.contract.commons.entities.JobPositionDto;
import nl.tudelft.sem.contract.commons.entities.SalaryScaleDto;
import nl.tudelft.sem.contract.commons.entities.utils.StringDto;
import nl.tudelft.sem.contract.microservice.database.entities.JobPosition;
import nl.tudelft.sem.contract.microservice.database.repositories.JobPositionRepository;
import nl.tudelft.sem.contract.microservice.exceptions.JobPositionNotFoundException;
import nl.tudelft.sem.contract.microservice.services.JobPositionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(INTERNAL_PATH + "/job-position")
public class JobPositionController {
    private final transient JobPositionRepository jobPositionRepository;
    private final transient JobPositionService jobPositionService;

    /**
     * Constructor for JobPositionController.
     *
     * @param jobPositionRepository repository for job contract
     * @param jobPositionService job position service
     */
    public JobPositionController(JobPositionRepository jobPositionRepository, JobPositionService jobPositionService) {
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
    @DeleteMapping("/{id}")
    ResponseEntity<JobPositionDto> deleteJobPosition(@PathVariable UUID id) {
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
        JobPosition jobPosition = jobPositionRepository.findById(id).orElseThrow(JobPositionNotFoundException::new);
        return ResponseEntity.ok(jobPositionService.editSalaryScale(jobPosition, salaryScaleDto).getDto());
    }

}
