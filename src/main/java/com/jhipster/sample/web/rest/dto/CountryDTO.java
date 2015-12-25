package com.jhipster.sample.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by pm01 on 12/25/15.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CountryDTO extends BaseDTO{

    public CountryDTO(){

    }
    public CountryDTO(Long id){
        this(id,null,null);
    }
    public CountryDTO(String name, String code){
        this(null,name,code);
    }
    public CountryDTO(Long id, String name, String code){
        this.setId(id);
        this.setCode(code);
        this.setName(name);
    }
}
