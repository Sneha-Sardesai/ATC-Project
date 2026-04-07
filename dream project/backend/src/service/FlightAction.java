package service;

import model.Flight;

/**
 * Interface for flight-related actions to enable polymorphism
 * Different action implementations can be executed on flights
 */
public interface FlightAction {
    void execute(Flight flight) throws Exception;
}