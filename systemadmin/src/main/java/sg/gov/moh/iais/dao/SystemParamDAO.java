package sg.gov.moh.iais.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sg.gov.moh.iais.entity.SystemParam;

import java.util.List;

@Repository
public interface SystemParamDAO extends JpaRepository<SystemParam, String> {

    @Query(value = "SELECT * FROM SYSTEM_PARAMETERS", nativeQuery = true)
    List<SystemParam> listSystemParam();


    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE SYSTEM_PARAMETERS SET VALUE = :value WHERE ROWGUID= :guid", nativeQuery = true)
    int updateDescriptionByKey(@Param("guid") String guid, @Param("value") Integer value);

}
