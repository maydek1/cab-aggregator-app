package com.software.modsen.passengerservice.component.steps;

import com.software.modsen.passengerservice.dto.request.ChargeMoneyRequest;
import com.software.modsen.passengerservice.dto.request.PassengerRequest;
import com.software.modsen.passengerservice.exception.EmailAlreadyExistException;
import com.software.modsen.passengerservice.exception.PassengerNotFoundException;
import com.software.modsen.passengerservice.exception.PhoneAlreadyExistException;
import com.software.modsen.passengerservice.mapper.PassengerMapper;
import com.software.modsen.passengerservice.model.Passenger;
import com.software.modsen.passengerservice.repositories.PassengerRepository;
import com.software.modsen.passengerservice.service.PassengerService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@CucumberContextConfiguration
public class PassengerSteps {

    @InjectMocks
    private PassengerService passengerService;
    @Mock
    private PassengerRepository passengerRepository;
    @Mock
    private PassengerMapper passengerMapper;

    private Passenger passenger;
    private Exception exception;
    private List<Passenger> passengerList = new ArrayList<>();

    @When("I create a passenger with email {string}, phone {string}, and money {string}")
    public void iCreateAPassenger(String email, String phone, String money) {
        PassengerRequest passengerRequest = new PassengerRequest();
        passengerRequest.setEmail(email);
        passengerRequest.setPhone(phone);
        passengerRequest.setMoney(new BigDecimal(money));

        when(passengerRepository.existsByEmail(email)).thenReturn(false);
        when(passengerRepository.existsByPhone(phone)).thenReturn(false);

        Passenger mockedPassenger = new Passenger();
        mockedPassenger.setEmail(email);
        mockedPassenger.setPhone(phone);
        mockedPassenger.setMoney(new BigDecimal(money));

        when(passengerMapper.passengerRequestToPassenger(passengerRequest)).thenReturn(mockedPassenger);
        when(passengerRepository.save(any(Passenger.class))).thenAnswer(invocation -> invocation.getArgument(0));

        passenger = passengerService.createPassenger(passengerRequest);
    }

    @Then("the passenger should be created with email {string} and phone {string}")
    public void thePassengerShouldBeCreatedWithEmailAndPhone(String email, String phone) {
        Assertions.assertEquals(email, passenger.getEmail());
        Assertions.assertEquals(phone, passenger.getPhone());
    }

    @Given("A passenger exists with email {string}")
    public void aPassengerExistsWithEmail(String email) {
        when(passengerRepository.existsByEmail(email)).thenReturn(true);
    }

    @When("I try to create a passenger with email {string}, phone {string}")
    public void iTryToCreateAPassengerWithEmail(String email, String phone) {
        PassengerRequest passengerRequest = new PassengerRequest();
        passengerRequest.setEmail(email);
        passengerRequest.setPhone(phone);

        try {
            passengerService.createPassenger(passengerRequest);
        } catch (EmailAlreadyExistException | PhoneAlreadyExistException e) {
            exception = e;
        }
    }

    @Then("I should receive an error {string}")
    public void iShouldReceiveAnError(String expectedMessage) {
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Given("a passenger exists with id {long}, email {string}, and phone {string}")
    public void aPassengerExistsWithIdEmailAndPhone(long id, String email, String phone) {
        Passenger existingPassenger = new Passenger();
        existingPassenger.setId(id);
        existingPassenger.setEmail(email);
        existingPassenger.setPhone(phone);
        existingPassenger.setMoney(BigDecimal.ZERO);

        passengerList.add(existingPassenger);

        when(passengerRepository.findById(id)).thenReturn(Optional.of(existingPassenger));
        when(passengerRepository.findAll()).thenReturn(passengerList);
    }

    @When("I update the passenger with id {long}, email {string}, and phone {string}")
    public void iUpdateThePassengerWithId(long id, String email, String phone) {
        PassengerRequest passengerRequest = new PassengerRequest();
        passengerRequest.setEmail(email);
        passengerRequest.setPhone(phone);

        Passenger mockedPassenger = new Passenger();
        mockedPassenger.setEmail(email);
        mockedPassenger.setPhone(phone);

        when(passengerMapper.passengerRequestToPassenger(any())).thenReturn(mockedPassenger);
        when(passengerRepository.save(mockedPassenger)).thenAnswer(invocation -> invocation.getArgument(0));
        try {
            passenger = passengerService.updatePassenger(id, passengerRequest);
        } catch (EmailAlreadyExistException | PhoneAlreadyExistException e) {
            exception = e;
        }
    }

    @Then("the passenger should be updated with email {string} and phone {string}")
    public void thePassengerShouldBeUpdatedWithEmailAndPhone(String email, String phone) {
        Assertions.assertEquals(email, passenger.getEmail());
        Assertions.assertEquals(phone, passenger.getPhone());
    }

    @When("I delete the passenger with id {long}")
    public void iDeleteThePassengerWithId(long id) {
        passenger = passengerList.get(passengerList.size()-1);
        when(passengerRepository.findById(id)).thenReturn(Optional.of(passenger));
        doNothing().when(passengerRepository).deleteById(id);

        try {
            passengerService.deletePassengerById(id);
            passenger = null;
        } catch (PassengerNotFoundException e) {
            exception = e;
        }
    }

    @Then("the passenger should be deleted")
    public void thePassengerShouldBeDeleted() {
        Assertions.assertNull(passenger);
    }

    @When("I request the passenger by id {long}")
    public void iRequestThePassengerById(long id) {
        when(passengerRepository.findById(id)).thenReturn(Optional.of(passengerList.get((int) (id - 1))));

        try {
            passenger = passengerService.getPassengerById(id);
        } catch (PassengerNotFoundException e) {
            exception = e;
        }
    }

    @Then("I should receive the passenger with email {string}")
    public void iShouldReceiveThePassengerWithEmail(String email) {
        Assertions.assertEquals(email, passenger.getEmail());
    }

    @When("I request a passenger by id {long}")
    public void iRequestAPassengerById(long id) {
        when(passengerRepository.findById(id)).thenReturn(Optional.empty());

        try {
            passengerService.getPassengerById(id);
        } catch (PassengerNotFoundException e) {
            exception = e;
        }
    }

    @Given("a passenger exists with id {long} and current money {string}")
    public void aPassengerExistsWithIdAndCurrentMoney(long id, String money) {
        Passenger existingPassenger = new Passenger();
        existingPassenger.setId(id);
        existingPassenger.setMoney(new BigDecimal(money));

        passengerList.add(existingPassenger);

        when(passengerRepository.findById(id)).thenReturn(Optional.of(existingPassenger));
        when(passengerRepository.findAll()).thenReturn(passengerList);
    }

    @When("I charge {string} to the passenger with id {long}")
    public void iChargeToThePassengerWithId(String amount, long id) {
        ChargeMoneyRequest chargeMoneyRequest = new ChargeMoneyRequest();
        chargeMoneyRequest.setPassengerId(id);
        chargeMoneyRequest.setMoney(new BigDecimal(amount));

        when(passengerRepository.findById(id)).thenReturn(Optional.of(passengerList.get((int) (id - 1))));

        when(passengerRepository.save(any(Passenger.class))).thenAnswer(invocation -> {
            Passenger updatedPassenger = invocation.getArgument(0);
            passengerList.set((int) (id - 1), updatedPassenger);
            return updatedPassenger;
        });

        try {
            passengerService.chargeMoney(chargeMoneyRequest);
        } catch (PassengerNotFoundException e) {
            exception = e;
        }
    }

    @Then("the passenger's money should be {string}")
    public void thePassengersMoneyShouldBe(String expectedAmount) {
        Passenger updatedPassenger = passengerService.getPassengerById(1L);
        Assertions.assertEquals(new BigDecimal(expectedAmount), updatedPassenger.getMoney());
    }

    @When("I try to charge {string} to a passenger with id {long}")
    public void iTryToChargeToANonExistentPassenger(String amount, long id) {
        ChargeMoneyRequest chargeMoneyRequest = new ChargeMoneyRequest();
        chargeMoneyRequest.setPassengerId(id);
        chargeMoneyRequest.setMoney(new BigDecimal(amount));

        when(passengerRepository.findById(id)).thenReturn(Optional.empty());

        try {
            passengerService.chargeMoney(chargeMoneyRequest);
        } catch (PassengerNotFoundException e) {
            exception = e;
        }
    }

    @Given("A passenger exists with phone {string}")
    public void aPassengerExistsWithPhone(String phone) {
        when(passengerRepository.existsByPhone(phone)).thenReturn(true);
    }

    @And("Email unique is: {string}")
    public void emailUnique(String unique) {
        when(passengerRepository.existsByEmail(any())).thenReturn(Boolean.valueOf(unique));
    }

    @And("Phone unique is: {string}")
    public void phoneUnique(String unique) {
        when(passengerRepository.existsByPhone(any())).thenReturn(Boolean.valueOf(unique));
    }
}
