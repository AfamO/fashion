package com.longbridge.services.implementations;

import com.longbridge.models.Code;
import com.longbridge.models.Response;
import com.longbridge.repository.CodeRepository;
import com.longbridge.services.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CodeServiceImpl implements CodeService {

    @Autowired
    CodeRepository codeRepository;

    @Override
    public Response createCode(Code code) {
        Code tempCode = codeRepository.findByNameAndType(code.getName(), code.getType());

        if(tempCode == null){
            codeRepository.save(code);
            return new Response("00", "Code created", null);
        }else{
            return new Response("99", "Code with the same type and name exist", null);
        }
    }

    @Override
    public Response updateCode(Code code) {
        Code tempCode = codeRepository.findOne(code.id);

        if(tempCode != null){
            tempCode.setName(code.getName());
            tempCode.setDescription(code.getDescription());
            tempCode.setType(code.getType());

            codeRepository.save(tempCode);
            return new Response("00", "Code updated", null);
        }else{
            return new Response("99", "Code not found", null);
        }
    }

    @Override
    public List<Code> findCodeByType(String type) {

        return codeRepository.findByType(type);
    }

    @Override
    public Code findCodeByNameAndType(String name, String type) {

        return codeRepository.findByNameAndType(name, type);
    }

    @Override
    public void deleteCode(Long id) {
        Code code = codeRepository.findOne(id);

        if(code != null){
            codeRepository.delete(code);
        }
    }

    @Override
    public Code findCodeById(Long id) {
        return codeRepository.findOne(id);
    }
}
