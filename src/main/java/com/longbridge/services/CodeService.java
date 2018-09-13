package com.longbridge.services;

import com.longbridge.models.Code;
import com.longbridge.models.Response;

import java.util.List;

public interface CodeService {

    Response createCode(Code code);
    Response updateCode(Code code);
    List<Code> findCodeByType(String type);
    Code findCodeByNameAndType(String name, String type);
    void deleteCode(Long id);
    Code findCodeById(Long id);
}
