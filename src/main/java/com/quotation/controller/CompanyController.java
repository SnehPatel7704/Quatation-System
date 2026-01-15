package com.quotation.controller;

import com.quotation.model.Company;
import com.quotation.repository.CompanyRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyRepository companyRepository;

    public CompanyController(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN', 'USER')")
    public ResponseEntity<List<Company>> listCompanies() {
        return ResponseEntity.ok(companyRepository.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN', 'USER')")
    public ResponseEntity<Company> getCompany(@PathVariable Long id) {
        return companyRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
    public ResponseEntity<Company> createCompany(@RequestBody Company company) {
        return ResponseEntity.ok(companyRepository.save(company));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
    public ResponseEntity<Company> updateCompany(@PathVariable Long id, @RequestBody Company company) {
        company.setId(id);
        return ResponseEntity.ok(companyRepository.save(company));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
    public ResponseEntity<?> deleteCompany(@PathVariable Long id) {
        companyRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
