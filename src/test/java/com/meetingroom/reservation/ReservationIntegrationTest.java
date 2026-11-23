package com.meetingroom.reservation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ReservationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;
    private UUID roomId;

    @BeforeEach
    void setUp() throws Exception {
        token = login();
        roomId = createRoom(token, "Sala Integración");
    }

    @Test
    void createReservation_overlapReturns409_andListIsPublic() throws Exception {
        String body = """
                {
                  "roomId": "%s",
                  "responsible": "Carlos Ruiz",
                  "startAt": "2026-12-10T09:00:00Z",
                  "endAt": "2026-12-10T10:00:00Z"
                }
                """.formatted(roomId);

        mockMvc.perform(post("/api/reservations")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        mockMvc.perform(post("/api/reservations")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("La sala ya está ocupada en ese horario"));

        mockMvc.perform(post("/api/reservations/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("page", "0")
                        .header("size", "10")
                        .content("""
                                {
                                  "roomId": "%s",
                                  "date": "2026-12-10"
                                }
                                """.formatted(roomId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pagination.total").value(1))
                .andExpect(jsonPath("$.data[0].responsible").value("Carlos Ruiz"));
    }

    @Test
    void cancelReservation_requiresAuth_andMarksCancelled() throws Exception {
        MvcResult created = mockMvc.perform(post("/api/reservations")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "roomId": "%s",
                                  "responsible": "Lucía Gómez",
                                  "startAt": "2026-12-11T14:00:00Z",
                                  "endAt": "2026-12-11T15:00:00Z"
                                }
                                """.formatted(roomId)))
                .andExpect(status().isCreated())
                .andReturn();

        UUID reservationId = UUID.fromString(
                objectMapper.readTree(created.getResponse().getContentAsString()).get("id").asText()
        );

        mockMvc.perform(delete("/api/reservations/" + reservationId))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(delete("/api/reservations/" + reservationId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    private String login() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "admin",
                                  "password": "admin123"
                                }
                                """))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
        String accessToken = json.get("accessToken").asText();
        assertThat(accessToken).isNotBlank();
        return accessToken;
    }

    private UUID createRoom(String jwt, String name) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/rooms")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "%s",
                                  "location": "Piso 2",
                                  "capacity": 6
                                }
                                """.formatted(name)))
                .andExpect(status().isCreated())
                .andReturn();

        return UUID.fromString(objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asText());
    }
}
