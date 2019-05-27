package sg.gov.moh.iais.dao;


import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sg.gov.moh.iais.dto.SearchConditionDTO;
import sg.gov.moh.iais.entity.Message;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;


@Repository
public interface MessageDao extends JpaRepository<Message, Integer>, JpaSpecificationExecutor {
/*
   default List<Message> listMessageBySearchCondition(SearchConditionDTO searchCondition){
      List<Message> resultList = null;
      Specification querySpecifi = new Specification<Message>() {
         public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
            List<Predicate> predicates = new ArrayList<>();
            if (!searchCondition.getType().equals("")){
               predicates.add(criteriaBuilder.equal(root.get(Message.COL_TYPE.toLowerCase()), searchCondition.getType()));
            }

            if(!searchCondition.getMessageType().equals("")){
               predicates.add(criteriaBuilder.equal(root.get(Message.COL_MESSAGE_TYPE.toLowerCase()), searchCondition.getMessageType()));
               }

            if(!searchCondition.getModule().equals("")){
               predicates.add(criteriaBuilder.equal(root.get(Message.COL_MODULE.toLowerCase()), searchCondition.getModule()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
         }
      };
      resultList =  this.findAll(querySpecifi);
      return resultList;
   }

   @Modifying(clearAutomatically = true)
   @Query(value = "UPDATE ERRORMESSAGE SET DESCRIPTION = :desc WHERE CODE_ID = :id", nativeQuery = true)
   void updateDescriptionByCodeId(@Param("id") Integer codeId, @Param("desc") String description);*/
}
