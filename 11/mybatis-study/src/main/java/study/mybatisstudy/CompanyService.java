package study.mybatisstudy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyMapper companyMapper;

    private final EmployeeMapper employeeMapper;

    public List<Company> getAll() {
        List<Company> companies = companyMapper.getAllV1();
        if (companies != null && !companies.isEmpty()) {
            for (Company company : companies) {
                company.setEmployees(employeeMapper.getByCompanyId(company.getId()));
            }
        }

        return companies;
    }

    // company table 에 데이터를 입력하면서 다른 레거시 시스템에도 데이터를 입력하면서
    // 오류가 발생했을 시 전체 작업을 롤백 하려 한다. Transaction
    // @Transactional(rollbackFor) 파라미터로 특정 경우에 롤백하도록 할 수 있다.
    @Transactional
    public Company add(Company company) {
        companyMapper.insert(company);
        // add companu into legacy system
        if (true) {
            throw new RuntimeException("Legacy Exception");
        }
        return company;
    }
}
