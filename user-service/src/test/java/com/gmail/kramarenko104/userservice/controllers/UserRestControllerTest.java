package com.gmail.kramarenko104.userservice.controllers;

import com.gmail.kramarenko104.userservice.models.RoleEnum;
import com.gmail.kramarenko104.userservice.models.UserDTO;
import com.gmail.kramarenko104.userservice.services.UserServiceImpl;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserRestControllerTest {

    // Test real controller
    @Autowired
    private UserRestController userController;

    // with mocked UserService
    @MockBean
    private UserServiceImpl mockedUserService;

    // and return mocked User from mocked UserService
    private UserDTO mockedUser;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        // Setup mocked user
        UserDTO mockedUser = new UserDTO();
        mockedUser.setUser_id(1L);
        mockedUser.setLogin("login@test.com");
        mockedUser.setName("testName");
        mockedUser.setAddress("test address");
        Set<RoleEnum> roles = new HashSet<>();
        roles.addAll(Arrays.asList(RoleEnum.ROLE_ADMIN, RoleEnum.ROLE_USER));
        mockedUser.setRoles(roles);
        this.mockedUser = mockedUser;
    }

    @Test
    public void testGetUserByUserId() throws Exception {
        doReturn(Optional.of(mockedUser)).when(mockedUserService).getUser(1);

        // Execute the GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", 1))
        // validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                // validate the returned body:
                .andExpect(jsonPath("$.user_id", is(1)))
                .andExpect(jsonPath("$.login", is("login@test.com")))
                .andExpect(jsonPath("$.name", is("testName")))
                .andExpect(jsonPath("$.address", is("test address")))
                .andExpect(jsonPath("$.roles").isArray())
                .andExpect(jsonPath("$.roles", Matchers.containsInAnyOrder("ROLE_ADMIN", "ROLE_USER")));

        verify(mockedUserService, times(1)).getUser(1L);
        verifyNoMoreInteractions(mockedUserService);
    }

    @Test
    public void getUserByLogin() {
    }

    @Test
    public void update() {
    }

    @Test
    public void deleteUser() {
    }

    @Test
    public void getUserAPI() {
    }

    @Test
    public void getAllUsers() {
    }

    @Test
    public void createUser() {
    }

}