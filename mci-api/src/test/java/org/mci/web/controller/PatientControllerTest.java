package org.mci.web.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mci.web.model.Address;
import org.mci.web.model.Patient;
import org.mci.web.service.PatientService;
import org.mci.web.utils.concurrent.PreResolvedListenableFuture;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PatientControllerTest {

    @Mock
    private PatientService patientService;
    private Patient patient;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new PatientController(patientService)).build();

        patient = new Patient();
        patient.setNationalId("nationalId-100");
        patient.setFirstName("Scott");
        patient.setLastName("Tiger");
        patient.setGender("1");
        patient.setDateOfBirth("2014-12-01");

        Address address = new Address();
        address.setAddressLine("house-10");
        address.setDivisionId("10");
        address.setDistrictId("1020");
        address.setUpazillaId("102030");
        address.setUnionId("10203040");
        patient.setAddress(address);
    }

    @Test
    public void shouldCreatePatientAndReturnHealthId() throws Exception {
        String json = new ObjectMapper().writeValueAsString(patient);
        String healthId = "healthId-100";
        when(patientService.create(patient)).thenReturn(new PreResolvedListenableFuture<String>(healthId));
        mockMvc.perform
                (
                        post("/patient").content(json).contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(request().asyncResult(healthId));
        verify(patientService).create(patient);
    }

    @Test
    public void shouldFindPatientByHealthId() throws Exception {
        String healthId = "healthId-100";
        when(patientService.findByHealthId(healthId)).thenReturn(new PreResolvedListenableFuture<Patient>(patient));
        mockMvc.perform(get("/patient/" + healthId))
                .andExpect(status().isOk())
                .andExpect(request().asyncResult(patient));
        verify(patientService).findByHealthId(healthId);
    }

    @Test
    public void shouldFindPatientByNationalId() throws Exception {
        String nationalId = "nationalId-123";
        when(patientService.findByNationalId(nationalId)).thenReturn(new PreResolvedListenableFuture<Patient>(patient));
        mockMvc.perform(get("/patient?nid=" + nationalId))
                .andExpect(status().isOk())
                .andExpect(request().asyncResult(patient));
        verify(patientService).findByNationalId(nationalId);
    }
}

