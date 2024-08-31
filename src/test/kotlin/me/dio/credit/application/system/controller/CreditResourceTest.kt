package me.dio.credit.application.system.controller

import com.fasterxml.jackson.databind.ObjectMapper
import me.dio.credit.application.system.controller.CustomerResourceTest.Companion.URL
import me.dio.credit.application.system.dto.request.CreditDto
import me.dio.credit.application.system.entity.Credit
import me.dio.credit.application.system.entity.Customer
import me.dio.credit.application.system.fake.FakeData
import me.dio.credit.application.system.repository.CreditRepository
import me.dio.credit.application.system.repository.CustomerRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
class CreditResourceTest {

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var creditRepository: CreditRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    companion object {
        const val URL_CUSTOMER: String = "/api/customers"
        const val URL_CREDIT: String = "/api/credits"
    }

    @BeforeEach
    fun setup() {
        customerRepository.deleteAll()
        creditRepository.deleteAll()
    }

    @AfterEach
    fun tearDown() {
        customerRepository.deleteAll()
        creditRepository.deleteAll()
    }

    @Test
    fun `should create a credit and return 201 status`() {

        // Given
        val customer: Customer = customerRepository.save(FakeData().builderCustomerDto().toEntity())
        val creditDto: CreditDto = FakeData().buildCreditDto(customerId = customer.id!!)
        val valueAsStringCredit: String = objectMapper.writeValueAsString(creditDto)
        // When

        // Then
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL_CREDIT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsStringCredit)
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andDo(MockMvcResultHandlers.print())

    }

    @Test
    fun `should find credit by customerId and return 200 status`() {
        // Given
        val customer: Customer = customerRepository.save(FakeData().builderCustomerDto().toEntity())
        // When

        // then
        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL_CREDIT?customerId=${customer.id}")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should find credit by customerId and creditCode and return 200 status`() {
        // Given
        val customer: Customer = customerRepository.save(FakeData().builderCustomerDto().toEntity())
        val credit: Credit = creditRepository.save(FakeData().buildCreditDto(customerId = customer.id!!).toEntity())
        // When

        // then
        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL_CREDIT/${credit.creditCode}?customerId=${customer.id}")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
    }

}
