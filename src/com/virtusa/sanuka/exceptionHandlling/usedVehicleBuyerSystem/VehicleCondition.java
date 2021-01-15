package com.virtusa.sanuka.exceptionHandlling.usedVehicleBuyerSystem;


import com.virtusa.sanuka.exceptionHandlling.exceptions.VehicleConditionException;
import com.virtusa.sanuka.exceptionHandlling.exceptions.VehicleHistoryException;

public class VehicleCondition {

    public static void getCondition(int vehicleSelector) throws VehicleConditionException {
        try {
            VehicleHistory.getVehicleHistory(vehicleSelector);
        } catch (VehicleHistoryException e) {
            throw new VehicleConditionException("Vehicle condition details are not available", e);
        }

    }
}
