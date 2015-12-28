package com.jhipster.sample.web.rest.dto;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;

/**
 * Created by pm01 on 12/25/15.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseDTO {

    public BaseDTO(){

    }
    private Long id;
    @NotNull
    @NotBlank
    private String name;
    @NotBlank
    @NotNull
    private String code;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


}
