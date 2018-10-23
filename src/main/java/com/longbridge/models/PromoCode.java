package com.longbridge.models;

import javax.persistence.Entity;
import java.util.Date;

/**
 * Created by Longbridge on 18/10/2018.
 */
@Entity
public class PromoCode extends CommonFields{

    private Date expiryDate;
    private String value;

}
