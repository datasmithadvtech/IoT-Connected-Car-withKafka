/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ram.repository;

import com.ram.domain.CarInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
/**
 *
 * @author ram
 */
// extend CrudRepository to be able to perform CRUD(GET POST PUT DELETE) operation on carinfo region
@RepositoryRestResource(collectionResourceRel = "carinfo", path = "carinfo")
public interface CarInfoRepository  extends CrudRepository<CarInfo, String> {
}