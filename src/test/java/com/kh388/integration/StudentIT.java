package com.kh388.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.kh388.student.Gender;
import com.kh388.student.Student;
import com.kh388.student.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Kevin on 5/19/21
 */

@SpringBootTest
@TestPropertySource(
        locations = "classpath:application-it.properties"
)
@AutoConfigureMockMvc
public class StudentIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StudentRepository studentRepository;

    private final Faker faker = new Faker();

    @Test
    void canRegisterNewStudent() throws Exception {

        // given
        String name = String.format(
                "%s %s",
                faker.name().firstName(),
                faker.name().lastName());
        name = StringUtils.trimAllWhitespace(name);
        Gender gender = Gender.values()[new Random().nextInt(Gender.values().length)];
        Student student = new Student(
                name,
                String.format("%s@email.com", name),
                gender
        );

        // when
        ResultActions resultActions = mockMvc
                .perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)));
        // then
        resultActions.andExpect(status().isOk());
        List<Student> students = studentRepository.findAll();
        assertThat(students)
                .usingElementComparatorIgnoringFields("id") // id is generate randomly
                .contains(student);
    }

    @Test
    void canDeleteStudent() throws Exception{

        // given
        String name = String.format(
                "%s %s",
                faker.name().firstName(),
                faker.name().lastName());
        name = StringUtils.trimAllWhitespace(name);
        Gender gender = Gender.values()[new Random().nextInt(Gender.values().length)];
        String email = String.format("%s@email.com", name);
        Student student = new Student(
                name,
                email,
                gender
        );

        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk());

        MvcResult getStudentResult = mockMvc.perform(get("/students")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = getStudentResult
                .getResponse()
                .getContentAsString();

        List<Student> students = objectMapper.readValue(
                contentAsString,
                new TypeReference<>() {
                }
        );

        long id = students.stream()
                .filter(s -> s.getEmail().equals(student.getEmail()))
                .map(Student::getId)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "student with eamil: " + email + " not found"
                        ));

        // when
        ResultActions resultActions = mockMvc
                .perform(delete("/students/" + id));

        // then
        resultActions.andExpect(status().isOk());
        boolean exists = studentRepository.existsById(id);
        assertThat(exists).isFalse();

    }
}
