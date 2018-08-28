package com.longbridge.Util;

import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Created by Longbridge on 28/08/2018.
 */
@Service
public class UniqueNumberUtil {


        public char[] OTP(int len)
        {
            // Using numeric values
            String numbers = "0123456789";

            // Using random method
            Random rndm_method = new Random();

            char[] otp = new char[len];

            for (int i = 0; i < len; i++)
            {
                // Use of charAt() method : to get character value
                // Use of nextInt() as it is scanning the value as int
                otp[i] =
                        numbers.charAt(rndm_method.nextInt(numbers.length()));
            }
            return otp;
        }
}


