package com.longbridge.repository;

import com.longbridge.models.Items;
import com.longbridge.models.Products;
import org.hibernate.sql.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by Longbridge on 21/12/2017.
 */
@Repository
public interface ItemRepository extends JpaRepository<Items, Long> {

    List<Items> findByDesignerId(Long designerId);


//    @Query("select productId, count(productId) as 'mcount', productId from Items group by productId order by 'mcount' desc")
//    Page<Long> findTopByCustomQuery(Pageable pageable);

}
