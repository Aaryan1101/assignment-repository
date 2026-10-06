package com.internal.tasktracker;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void appliesArchivedSearchAndStatusConditionsToEveryResult() throws Exception {
        mockMvc.perform(get("/api/tasks")
                        .param("q", "api")
                        .param("status", "OPEN")
                        .param("pageSize", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[*].status", everyItem(is("OPEN"))))
                .andExpect(jsonPath("$.items[*].title", not(hasItem("Legacy API cleanup"))));
    }

    @Test
    void paginatesResultsInTheDatabase() throws Exception {
        mockMvc.perform(get("/api/tasks").param("page", "2").param("pageSize", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(2))
                .andExpect(jsonPath("$.pageSize").value(5))
                .andExpect(jsonPath("$.items", hasSize(5)));
    }

    @Test
    void rejectsInvalidPagingAndStatusValues() throws Exception {
        mockMvc.perform(get("/api/tasks").param("page", "0"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(get("/api/tasks").param("pageSize", "101"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(get("/api/tasks").param("status", "unknown"))
                .andExpect(status().isBadRequest());
    }
}
