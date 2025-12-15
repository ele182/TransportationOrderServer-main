package es.upm.dit.apsv.transportationorderserver;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import es.upm.dit.apsv.transportationorderserver.controller.TransportationOrderController;
import es.upm.dit.apsv.transportationorderserver.model.TransportationOrder;
import es.upm.dit.apsv.transportationorderserver.repository.TransportationOrderRepository;

@WebMvcTest(TransportationOrderController.class)
public class TransportationOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransportationOrderRepository repository;

    @Test
    public void testGetOrders() throws Exception {

        List<TransportationOrder> orders = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            orders.add(new TransportationOrder());
        }

        when(repository.findAll()).thenReturn(orders);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/transportationorders")
                        .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(20)));
    }

    @Test
    public void testGetOrder() throws Exception {

        TransportationOrder order = new TransportationOrder();
        order.setTruck("8962ZKR");

        when(repository.findById("8962ZKR")).thenReturn(Optional.of(order));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/transportationorders/8962ZKR")
                        .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk());

        when(repository.findById("NOEXISTE")).thenReturn(Optional.empty());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/transportationorders/NOEXISTE")
        )
        .andExpect(status().isNotFound());
    }
}
