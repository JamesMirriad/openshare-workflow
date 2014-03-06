package com.openshare.service.base.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
/**
 * Base class for mirriad result objects.
 * @author james.mcilroy
 *
 */
@ApiModel(value="MirriadDtoObject", description="Basic Mirriad Dto Base Object")
@XmlRootElement
public interface MirriadDtoObject extends Serializable{

}
