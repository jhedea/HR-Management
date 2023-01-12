package nl.tudelft.sem.contract.microservice.controllers;

import java.net.URI;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import nl.tudelft.sem.contract.commons.entities.SalaryScaleDto;
import nl.tudelft.sem.contract.commons.entities.utils.StringDto;
import nl.tudelft.sem.contract.microservice.database.entities.SalaryScale;
import nl.tudelft.sem.contract.microservice.database.repositories.SalaryScaleRepository;
import nl.tudelft.sem.contract.microservice.exceptions.ActionNotAllowedException;
import nl.tudelft.sem.contract.microservice.exceptions.SalaryScaleNotFoundException;
import nl.tudelft.sem.contract.microservice.services.RoleAuthenticationService;
import nl.tudelft.sem.contract.microservice.services.SalaryScaleService;
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

@Slf4j
@RestController
@RequestMapping("/salary-scale")
public class SalaryScalePublicController {
    private final transient RoleAuthenticationService authenticationService;
    private final transient SalaryScaleRepository salaryScaleRepository;
    private final transient SalaryScaleService salaryScaleService;

    /**
     * Constructor.
     *
     * @param salaryScaleRepository repository for salary scale
     */
    public SalaryScalePublicController(RoleAuthenticationService authService,
                                   SalaryScaleRepository salaryScaleRepository,
                                   SalaryScaleService salaryScaleService) {
        this.authenticationService = authService;
        this.salaryScaleRepository = salaryScaleRepository;
        this.salaryScaleService = salaryScaleService;
    }

    /**
     * Find and get salary scale from database.
     *
     * @param id id of salary scale
     * @return salary scale
     */
    @GetMapping("/{id}")
    ResponseEntity<SalaryScaleDto> getSalaryScale(@PathVariable UUID id) {
        return ResponseEntity.ok(salaryScaleRepository.findById(id).orElseThrow(SalaryScaleNotFoundException::new).getDto());
    }

    /**
     * Add new salary scale to database.
     *
     * @param salaryScaleDto salary scale to be added
     * @return added salary scale
     */
    @PostMapping
    ResponseEntity<SalaryScaleDto> addSalaryScale(@RequestBody SalaryScaleDto salaryScaleDto) {
        if (!authenticationService.hasAtLeastRole(Role.HR)) {
            throw new ActionNotAllowedException("You are not allowed to add a salary scale");
        }
        SalaryScale salaryScale = salaryScaleRepository.save(new SalaryScale(salaryScaleDto));
        return ResponseEntity.created(URI.create("/salary-scale/" + salaryScale.getId()))
                .body(salaryScale.getDto());
    }

    /**
     * Remove salary scale from database.
     *
     * @param id id of salary scale to be removed
     * @return removed salary scale
     */
    @DeleteMapping("/{id}/delete")
    ResponseEntity<SalaryScaleDto> deleteSalaryScale(@PathVariable UUID id) {
        if (!authenticationService.hasAtLeastRole(Role.HR)) {
            throw new ActionNotAllowedException("You are not allowed to delete a salary scale");
        }
        SalaryScaleDto salaryScaleDto = salaryScaleRepository.findById(id)
                .orElseThrow(SalaryScaleNotFoundException::new).getDto();
        salaryScaleRepository.deleteById(id);
        return ResponseEntity.ok(salaryScaleDto);
    }

    /**
     * Change minimum pay of salary scale.
     *
     * @param id id of salary scale to edit
     * @param minimumPay new mimimum pay
     * @return changed salary scale
     */
    @PutMapping("/{id}/edit-minimum-pay")
    ResponseEntity<SalaryScaleDto> editMinimumPay(@PathVariable UUID id, @RequestBody StringDto minimumPay) {
        if (!authenticationService.hasAtLeastRole(Role.HR)) {
            throw new ActionNotAllowedException("You are not allowed to edit a salary scale");
        }
        SalaryScale salaryScale = salaryScaleRepository.findById(id).orElseThrow(SalaryScaleNotFoundException::new);
        return ResponseEntity.ok(salaryScaleService.editMinimumPay(salaryScale, minimumPay).getDto());
    }

    /**
     * Change maximum pay of salary scale.
     *
     * @param id id of salary scale to edit
     * @param maximumPay new maximum pay
     * @return changed salry scale
     */
    @PutMapping("/{id}/edit-maximum-pay")
    ResponseEntity<SalaryScaleDto> editMaximumPay(@PathVariable UUID id, @RequestBody StringDto maximumPay) {
        if (!authenticationService.hasAtLeastRole(Role.HR)) {
            throw new ActionNotAllowedException("You are not allowed to edit a salary scale");
        }
        SalaryScale salaryScale = salaryScaleRepository.findById(id).orElseThrow(SalaryScaleNotFoundException::new);
        return ResponseEntity.ok(salaryScaleService.editMaximumPay(salaryScale, maximumPay).getDto());
    }

}
