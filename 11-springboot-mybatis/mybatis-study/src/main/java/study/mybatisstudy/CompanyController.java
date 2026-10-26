package study.mybatisstudy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/company")
public class CompanyController {

    private final CompanyMapper companyMapper;

    private final CompanyService companyService;

    @PostMapping("/v1")
    public int post(@RequestBody Company company) {
        log.info("company={}", company);
        return companyMapper.insertV1(company);
    }

    @PostMapping
    public Company postV2(@RequestBody Company company) {
        companyMapper.insert(company);
        log.info("company={}", company);
        return company;
    }

    @GetMapping("/v1")
    public List<Company> getAllV1() {
        return companyMapper.getAllV1();
    }

    @GetMapping("/v2")
    public List<Company> getAllV2() {
        return companyService.getAll();
    }

    @GetMapping
    public List<Company> getAll() {
        return companyMapper.getAll();
    }

    @GetMapping("/{id}")
    public Company getById(@PathVariable("id") Long id) {
        return companyMapper.getById(id);
    }
}
