package com.example.beacon.vdf.application.vdfbeacon;

import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class VdfPulseDtoTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidator(){
        VdfPulseDtoPost dto = new VdfPulseDtoPost();
        dto.setSeed("ddddq");
        Set<ConstraintViolation<VdfPulseDtoPost>> constraintViolations = validator.validate(dto);
        assertEquals(0, constraintViolations.size());
    }

    @Test
    public void testValidatorOk(){
        VdfPulseDtoPost dto = new VdfPulseDtoPost();
        dto.setSeed("D31A8642F8AA222AB02817BABE33CFE3A1996EE404A54F694CFEFDC1A56CE0F0FF2FC5DBC4DCEBF32BED3EDA5437E79BD866F0A61773D7587F9798878CAC3B5B");
        Set<ConstraintViolation<VdfPulseDtoPost>> constraintViolations = validator.validate(dto);
        assertEquals(0, constraintViolations.size());

    }





}