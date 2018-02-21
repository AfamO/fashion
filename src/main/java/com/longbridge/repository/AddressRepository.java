package com.longbridge.repository;

import com.longbridge.models.Address;
import com.longbridge.models.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by longbridge on 11/5/17.
 */
@Repository
public interface AddressRepository extends PagingAndSortingRepository<Address,Long>{
    List<Address> findByUser(User user);

}
