package me.dio.credit.application.system.fake

import me.dio.credit.application.system.dto.request.CreditDto
import me.dio.credit.application.system.dto.request.CustomerDto
import me.dio.credit.application.system.dto.request.CustomerUpdateDto
import java.math.BigDecimal
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.random.Random

class FakeData {

    private fun generateRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    private fun generateRandomCpf(): String {
        val random = Random(System.currentTimeMillis())
        val digits = (1..9).map { random.nextInt(0, 10) }
        val firstDigit = calculateVerifierDigit(digits, 10)
        val digitsWithFirstVerifier = digits + firstDigit
        val secondDigit = calculateVerifierDigit(digitsWithFirstVerifier, 11)
        val cpf = (digits + listOf(firstDigit, secondDigit))
            .joinToString("") { it.toString() }
            .let { "${it.substring(0, 3)}.${it.substring(3, 6)}.${it.substring(6, 9)}-${it.substring(9)}" }
        return cpf
    }

    private fun generateRandomEmail(length: Int): String {
        val allowedChars = ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    private fun calculateVerifierDigit(digits: List<Int>, factor: Int): Int {
        val sum = digits
            .mapIndexed { index, digit -> digit * (factor - index) }
            .sum()
        val remainder = sum % 11
        return if (remainder < 2) 0 else 11 - remainder
    }

    private fun generateRandomDate(): LocalDate {
        val startDate = LocalDate.now()
        val endDate = startDate.plusMonths(3)
        val daysBetween = ChronoUnit.DAYS.between(startDate, endDate)
        val randomDays = Random.nextLong(daysBetween + 1)
        return startDate.plusDays(randomDays)
    }

    public fun builderCustomerDto(
        firstName: String = this.generateRandomString(Random.nextInt(1, 255)),
        lastName: String = this.generateRandomString(Random.nextInt(1, 255)),
        cpf: String = this.generateRandomCpf(),
        email: String = this.generateRandomEmail(Random.nextInt(1, 50)) + "@mail.org",
        income: BigDecimal = BigDecimal(
            Random.nextDouble(100.0, 100000.0)
        ).setScale(
            2, BigDecimal.ROUND_HALF_UP
        ),
        password: String = this.generateRandomString(Random.nextInt(1, 255)),
        zipCode: String = this.generateRandomString(Random.nextInt(1, 255)),
        street: String = this.generateRandomString(Random.nextInt(1, 255))
    ) = CustomerDto(
        firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        email = email,
        income = income,
        password = password,
        zipCode = zipCode,
        street = street
    )

    public fun builderCustomerUpdateDto(
        firstName: String = this.generateRandomString(Random.nextInt(1, 255)),
        lastName: String = this.generateRandomString(Random.nextInt(1, 255)),
        income: BigDecimal = BigDecimal(
            Random.nextDouble(100.0, 100000.0)
        ).setScale(
            2, BigDecimal.ROUND_HALF_UP
        ),
        zipCode: String = this.generateRandomString(Random.nextInt(1, 255)),
        street: String = this.generateRandomString(Random.nextInt(1, 255))
    ): CustomerUpdateDto = CustomerUpdateDto(
        firstName = firstName,
        lastName = lastName,
        income = income,
        zipCode = zipCode,
        street = street
    )


    public fun buildCreditDto(
        creditValue: BigDecimal = BigDecimal(
            Random.nextDouble(100.0, 100000.0)
        ).setScale(
            2, BigDecimal.ROUND_HALF_UP
        ),
        dayFirstOfInstallment: LocalDate = this.generateRandomDate(),
        numberOfInstallments: Int = Random.nextInt(1, 49),
        customerId: Long = 1L
    ): CreditDto = CreditDto(
        creditValue = creditValue,
        dayFirstOfInstallment = dayFirstOfInstallment,
        numberOfInstallments = numberOfInstallments,
        customerId = customerId
    )

}
