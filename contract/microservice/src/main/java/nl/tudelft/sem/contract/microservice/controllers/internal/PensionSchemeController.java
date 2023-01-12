package nl.tudelft.sem.contract.microservice.controllers.internal;

import static nl.tudelft.sem.contract.commons.ApiData.INTERNAL_PATH;

import java.net.URI;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import nl.tudelft.sem.contract.commons.entities.PensionSchemeDto;
import nl.tudelft.sem.contract.commons.entities.utils.StringDto;
import nl.tudelft.sem.contract.microservice.database.entities.PensionScheme;
import nl.tudelft.sem.contract.microservice.database.repositories.PensionSchemeRepository;
import nl.tudelft.sem.contract.microservice.exceptions.PensionSchemeNotFoundException;
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
@RequestMapping(INTERNAL_PATH + "/pension-scheme")
public class PensionSchemeController {
    private final transient PensionSchemeRepository pensionSchemeRepository;

    public PensionSchemeController(PensionSchemeRepository pensionSchemeRepository) {
        this.pensionSchemeRepository = pensionSchemeRepository;
    }

    @GetMapping("/{id}")
    ResponseEntity<PensionSchemeDto> getPensionScheme(@PathVariable UUID id) {
        return ResponseEntity.ok(pensionSchemeRepository.findById(id)
                .orElseThrow(PensionSchemeNotFoundException::new).getDto());
    }

    @PostMapping("")
    ResponseEntity<PensionSchemeDto> addPensionScheme(@RequestBody PensionSchemeDto pensionSchemeDto) {
        PensionScheme pensionScheme = pensionSchemeRepository.save(new PensionScheme(pensionSchemeDto));
        return ResponseEntity.created(URI.create(INTERNAL_PATH + "/pension-scheme/" + pensionScheme.getId()))
                .body(pensionScheme.getDto());
    }

    @DeleteMapping("/{id}")
    ResponseEntity<PensionSchemeDto> deletePensionScheme(@PathVariable UUID id) {
        PensionSchemeDto pensionSchemeDto = pensionSchemeRepository.findById(id)
                .orElseThrow(PensionSchemeNotFoundException::new).getDto();
        pensionSchemeRepository.deleteById(id);
        return ResponseEntity.ok(pensionSchemeDto);
    }

    @PutMapping("/{id}/edit-name")
    ResponseEntity<PensionSchemeDto> editName(@PathVariable UUID id, @RequestBody StringDto nameDto) {
        PensionScheme pensionScheme = pensionSchemeRepository.findById(id).orElseThrow(PensionSchemeNotFoundException::new);
        pensionScheme.setName(nameDto.getData());
        pensionSchemeRepository.save(pensionScheme);
        return ResponseEntity.ok(pensionScheme.getDto());
    }

}
