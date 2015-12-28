package com.jhipster.sample.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by pm01 on 12/25/15.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CityDTO extends BaseDTO {

    private CountryDTO country;
    public CityDTO(){

    }
    public CityDTO(Long id){
        this(id,null,null,null);
    }
    public CityDTO(String name, String code){
        this(null,name,code,null);
    }
    public CityDTO(String name, String code,CountryDTO country){
        this(null,name,code,country);
    }
    public CityDTO(Long id, String name, String code,CountryDTO country){
        this.setId(id);
        this.setCode(code);
        this.setName(name);
        this.country=country;
    }

    public CountryDTO getCountry() {
        return country;
    }

    public void setCountry(CountryDTO country) {
        this.country = country;
    }
}
