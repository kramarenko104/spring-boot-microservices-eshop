package com.gmail.kramarenko104.userservice.controllers;

import com.gmail.kramarenko104.userservice.models.RoleEnum;
import com.gmail.kramarenko104.userservice.models.User;
import com.gmail.kramarenko104.userservice.models.UserDTO;
import com.gmail.kramarenko104.userservice.services.UserServiceImpl;
import org.hamcrest.Matchers;
import org.json.JSONObject;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    private List<UserDTO> mockedUsers;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        this.mockedUsers = new ArrayList<>();

        // Setup mocked users
        UserDTO mockedUser = new UserDTO();
        mockedUser.setUser_id(1L);
        mockedUser.setLogin("login@test.com");
        mockedUser.setName("testName");
        mockedUser.setAddress("test address");
        Set<RoleEnum> roles = new HashSet<>();
        roles.addAll(Arrays.asList(RoleEnum.ROLE_ADMIN, RoleEnum.ROLE_USER));
        mockedUser.setRoles(roles);
        this.mockedUser = mockedUser;

        UserDTO mockedUser2 = new UserDTO();
        mockedUser2.setUser_id(2L);
        mockedUser2.setLogin("test@test.com");
        mockedUser2.setName("Mike Kovalenko");
        mockedUser2.setAddress("Kiev, Proreznaya str., 21");
        Set<RoleEnum> roles2 = new HashSet<>();
        roles2.addAll(Arrays.asList(RoleEnum.ROLE_USER));
        mockedUser2.setRoles(roles2);

        this.mockedUsers.addAll(Arrays.asList(mockedUser, mockedUser2));
    }

    @Test
    public void testGetUserByUserIdFound() throws Exception {
        doReturn(Optional.of(mockedUser)).when(mockedUserService).getUser(1);

        // Execute the GET request
        mockMvc.perform(get("/users/{id}", 1))
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

        verify(mockedUserService, times(1)).getUser(1);
        verifyNoMoreInteractions(mockedUserService);
    }

    @Test
    public void testGetUserByUserIdNotFound() throws Exception {
        doReturn(Optional.empty()).when(mockedUserService).getUser(23);
        // Execute the GET request
        mockMvc.perform(get("/users/{id}", 23))
                // validate the response code
                .andExpect(status().isNotFound());
        verify(mockedUserService, times(1)).getUser(23);
        verifyNoMoreInteractions(mockedUserService);
    }

    @Test
    public void testGetUserByLoginFound() throws Exception {
        String testLogin = this.mockedUser.getLogin();
        doReturn(Optional.of(mockedUser)).when(mockedUserService).getUserByLogin(testLogin);

        // Execute the GET request
        mockMvc.perform(get("/users?login={login}", testLogin))
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

        verify(mockedUserService, times(1)).getUserByLogin(testLogin);
        verifyNoMoreInteractions(mockedUserService);
    }

    @Test
    public void testGetAllUsersFound() throws Exception {
        doReturn(Optional.of(mockedUsers)).when(mockedUserService).getAllUsers();

        // Execute the GET request
        mockMvc.perform(get("/users"))
                // validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                // validate the returned body:
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].user_id", is(1)))
                .andExpect(jsonPath("$[0].login", is("login@test.com")))
                .andExpect(jsonPath("$[0].name", is("testName")))
                .andExpect(jsonPath("$[0].address", is("test address")))
                .andExpect(jsonPath("$[0].roles").isArray())
                .andExpect(jsonPath("$[0].roles", Matchers.containsInAnyOrder("ROLE_ADMIN", "ROLE_USER")));

        verify(mockedUserService, times(1)).getAllUsers();
        verifyNoMoreInteractions(mockedUserService);
    }

    @Test
    public void testCreateUserPassed() throws Exception {
        User createUser = new User();
        createUser.setUser_id(3L);
        createUser.setLogin("test@test.com");
        createUser.setName("Mike Kovalenko");
        createUser.setAddress("Kiev, Proreznaya str., 21");
        Set<RoleEnum> roles = new HashSet<>();
        roles.add(RoleEnum.ROLE_USER);
        createUser.setRoles(roles);
        UserDTO createdMockedUser = createUser.createDTO();

        doReturn(createdMockedUser).when(mockedUserService).createUser(createUser);

        // Execute the POST request
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new JSONObject(createUser).toString())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())

                // validate the returned body:
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.user_id", is(3)))
                .andExpect(jsonPath("$.login", is("test@test.com")))
                .andExpect(jsonPath("$.name", is("Mike Kovalenko")))
                .andExpect(jsonPath("$.address", is("Kiev, Proreznaya str., 21")))
                .andExpect(jsonPath("$.roles").isArray())
                .andExpect(jsonPath("$.roles", Matchers.containsInAnyOrder("ROLE_USER")))
                .andReturn();

        verify(mockedUserService, times(1)).createUser(createUser);
        verifyNoMoreInteractions(mockedUserService);
    }

    @Test
    public void testDeleteUserPassed() throws Exception {
        doNothing().when(mockedUserService).deleteUser(1);

        // Execute the GET request
        mockMvc.perform(delete("/users/{id}", 1))
                // validate the response code and content type
                .andExpect(status().isOk());

        verify(mockedUserService, times(1)).deleteUser(1);
        verifyNoMoreInteractions(mockedUserService);
    }

    @Test
    public void testDeleteUserFailed() throws Exception {
        doNothing().when(mockedUserService).deleteUser(5);

        // Execute the GET request
        mockMvc.perform(delete("/users/{id}", 5))
                // validate the response code and content type
                .andExpect(status().isOk());

        verify(mockedUserService, times(1)).deleteUser(5);
        verifyNoMoreInteractions(mockedUserService);
    }

    @Test
    public void testUpdateUserPassed() throws Exception {
        User updateUser = new User();
        updateUser.setUser_id(1L);
        updateUser.setLogin("test@test.com");
        updateUser.setName("updated name");
        updateUser.setAddress("updated address");
        Set<RoleEnum> roles = new HashSet<>();
        roles.add(RoleEnum.ROLE_ADMIN);
        updateUser.setRoles(roles);
        UserDTO updatedMockedUser = updateUser.createDTO();

        doReturn(Optional.of(updatedMockedUser)).when(mockedUserService).getUser(1L);
        doReturn(1).when(mockedUserService).updateUser(1L, updateUser);

        // Execute the PUT request
        mockMvc.perform(put("/users/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new JSONObject(updateUser).toString()))
                .andDo(MockMvcResultHandlers.print())

                // validate the returned body:
                .andExpect(status().isOk());

        verify(mockedUserService, times(1)).updateUser(1L, updateUser);
        verifyNoMoreInteractions(mockedUserService);
    }

    @Test
    public void testUpdateUserFailed() throws Exception {
        User updateUser = new User();
        updateUser.setUser_id(5L);
        updateUser.setLogin("test@test.com");
        updateUser.setName("updated name");
        updateUser.setAddress("updated address");
        Set<RoleEnum> roles = new HashSet<>();
        roles.add(RoleEnum.ROLE_ADMIN);
        updateUser.setRoles(roles);

        doReturn(Optional.ofNullable(null)).when(mockedUserService).getUser(5L);
        doReturn(0).when(mockedUserService).updateUser(5L, updateUser);

        // Execute the PUT request
        mockMvc.perform(put("/users/{id}", 5)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new JSONObject(updateUser).toString()))
                .andDo(MockMvcResultHandlers.print())

                // validate the returned body:
                .andExpect(status().isNotFound());

        verify(mockedUserService, times(1)).updateUser(5L, updateUser);
        verifyNoMoreInteractions(mockedUserService);
    }
}