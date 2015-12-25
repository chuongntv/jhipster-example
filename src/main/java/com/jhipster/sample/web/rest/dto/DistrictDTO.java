package com.jhipster.sample.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by pm01 on 12/25/15.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DistrictDTO extends BaseDTO {
    private CityDTO city;
    public DistrictDTO(){

    }
    public DistrictDTO(Long id){
        this(id,null,null,null);
    }
    public DistrictDTO(String name, String code){
        this(null,name,code,null);
    }
    public DistrictDTO(String name, String code,CityDTO city){
        this(null,name,code,city);
    }
    public DistrictDTO(Long id, String name, String code,CityDTO city){
        this.setId(id);
        this.setCode(code);
        this.setName(name);
        this.city=city;
    }

    public CityDTO getCity() {
        return city;
    }

    public void setCity(CityDTO country) {
        this.city = country;
    }
}
