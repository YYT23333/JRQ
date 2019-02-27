package njurestaurant.njutakeout.data.dao.qualification;

import njurestaurant.njutakeout.entity.qualification.IOSQualification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOSQualificationDao extends JpaRepository<IOSQualification,String> {
    IOSQualification getIOSQualificationById(int id);
}
