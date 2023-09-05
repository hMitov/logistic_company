package com.semestrial_project.logistic_company.domain.entity;

import com.semestrial_project.logistic_company.domain.exceptions.DomainException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Set;

import static com.semestrial_project.logistic_company.domain.exceptions.code.DomainErrorCode.DRIVING_CATEGORY_NOT_FOUND;
@SuperBuilder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "driving_category")
public class DrivingCategory {

    public static final Long A = 1L;
    public static final Long A1 = 2L;
    public static final Long B = 3L;
    public static final Long B1 = 4L;
    public static final Long BE = 5L;
    public static final Long C = 6L;
    public static final Long C1 = 7L;
    public static final Long CE = 8L;
    public static final Long D = 9L;
    public static final Long D1 = 10L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_name")
    private String categoryName;

    @ManyToMany(mappedBy = "drivingLicenseCategories", fetch = FetchType.LAZY)
    private Set<Supplier> suppliers;

    public static Long getCategory(String categoryName) throws DomainException {
        Long result;
        switch (categoryName) {
            case "A":
                result = A;
                break;
            case "A1":
                result = A1;
                break;
            case "B":
                result = B;
                break;
            case "B1":
                result = B1;
                break;
            case "BE":
                result = BE;
                break;
            case "C":
                result = C;
                break;
            case "C1":
                result = C1;
                break;
            case "CE":
                result = CE;
                break;
            case "D":
                result = D;
                break;
            case "D1":
                result = D1;
                break;
            default:
                throw new DomainException(DRIVING_CATEGORY_NOT_FOUND);
        }

        return result;
    }

}
