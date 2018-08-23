/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ram.domain;

/**
 *
 * @author ram
 */
import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.annotation.Region;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
@Region(value = "car-info")
public class CarInfo implements Serializable {

    private static final long serialVersionUID = 1;

    @Id
    private String vin;

    private Double absoluteThrottlePosB;
    private Double acceleration;
    private Double acceleratorThrottlePosD;
    private Double acceleratorThrottlePosE;
    private Integer barometricPressure;
    private Double bearing;
    private Double catalystTemp;
    private Double controlModuleVoltage;
    private Integer coolantTemp;
    private Integer distanceWithMilOn;
    private Double engineLoad;
    private Integer fuelLevelInput;
    private Integer[] fuelSystemStatus;
    private Integer intakeAirTemp;
    private Integer intakeManifoldPressure;
    private Double latitude;
    private Double longTermFuel;
    private Double longitude;
    private Double mafAirflow;
    private Double mpgInstantaneous;
    private Integer obdStandards;
    private Double relativeThrottlePos;
    private Double rpm;
    private Double shortTermFuel;
    private Integer throttlePosition;
    private long timeSinceEngineStart;
    private long timestamp;
    private Double vehicleSpeed;

    private String journeyId;

    private Integer toInteger(Object value) {
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof String && StringUtils.hasText((String) value)) {
            return Integer.parseInt((String) value);
        } else {
            return null;
        }
    }

    private Long toLong(Object value) {
        if (value instanceof Long) {
            return (Long) value;
        } else if (value instanceof String && StringUtils.hasText((String) value)) {
            return Long.parseLong((String) value);
        } else {
            return null;
        }
    }

    private Double toDouble(Object value) {
        if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof String && StringUtils.hasText((String) value)) {
            return Double.parseDouble((String) value);
        } else if (value instanceof Integer) {
            return ((Integer) value).doubleValue();
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public CarInfo(Map<String, Object> values) {
        this.journeyId = String.valueOf(values.get("journey_id"));
        this.absoluteThrottlePosB = toDouble(values.get("absolute_throttle_pos_b"));
        this.acceleration = toDouble(values.get("acceleration"));
        this.acceleratorThrottlePosD = toDouble(values.get("accelerator_throttle_pos_d"));
        this.acceleratorThrottlePosE = toDouble(values.get("accelerator_throttle_pos_e"));
        this.barometricPressure = toInteger(values.get("barometric_pressure"));
        this.bearing = toDouble(values.get("bearing"));
        this.catalystTemp = toDouble(values.get("catalyst_temp"));
        this.controlModuleVoltage = toDouble(values.get("control_module_voltage"));
        this.coolantTemp = toInteger(values.get("coolant_temp"));
        this.distanceWithMilOn = toInteger(values.get("distance_with_mil_on"));
        this.engineLoad = toDouble(values.get("engine_load"));
        this.fuelLevelInput = toInteger(values.get("fuel_level_input"));

        List<Integer> fuelSystemStatus = (List) values.get("fuel_system_status");
        if (fuelSystemStatus != null) {
            Integer[] fuelSystemValues = new Integer[fuelSystemStatus.size()];
            for (int i = 0; i < fuelSystemStatus.size(); i++) {
                fuelSystemValues[i] = fuelSystemStatus.get(i);
            }

            this.fuelSystemStatus = fuelSystemValues;
        } else {
            this.fuelSystemStatus = new Integer[0];
        }

        this.intakeAirTemp = toInteger(values.get("intake_air_temp"));
        this.intakeManifoldPressure = toInteger(values.get("intake_manifold_pressure"));
        this.latitude = toDouble(values.get("latitude"));
        this.longTermFuel = toDouble(values.get("long_term_fuel"));
        this.longitude = toDouble(values.get("longitude"));
        this.mafAirflow = toDouble(values.get("maf_airflow"));
        this.mpgInstantaneous = toDouble(values.get("mpg_instantaneous"));
        this.obdStandards = Integer.parseInt(String.valueOf(values.get("obd_standards")));
        this.relativeThrottlePos = toDouble(values.get("relative_throttle_pos"));
        this.rpm = toDouble(values.get("rpm"));
        this.shortTermFuel = toDouble(values.get("short_term_fuel"));
        this.throttlePosition = toInteger(values.get("throttle_position"));

        Object timeSinceEngineStart = values.get("time_since_engine_start");

        if (timeSinceEngineStart != null) {
            if (timeSinceEngineStart instanceof Long) {
                this.timeSinceEngineStart = (Long) timeSinceEngineStart;
            } else {
                this.timeSinceEngineStart = ((Integer) timeSinceEngineStart).longValue();
            }
        }

        this.timestamp = toLong(values.get("timestamp"));
        Object vehicleSpeed = values.get("vehicle_speed");

        if (vehicleSpeed != null) {
            if (vehicleSpeed instanceof Integer) {
                this.vehicleSpeed = ((Integer) vehicleSpeed).doubleValue();
            } else if (vehicleSpeed instanceof Double) {
                this.vehicleSpeed = (Double) vehicleSpeed;
            } else {
                this.vehicleSpeed = Double.parseDouble(String.valueOf(vehicleSpeed));
            }
        }

        this.vin = (String) values.get("vin");

    }

    /**
     * An id that identifies a journey for a given VIN.
     *
     * @return a unique string identifying the current journey
     */
    public String getJourneyId() {
        return journeyId;
    }

    /**
     * The data point returned for the OBD II PID 01 47
     *
     * @return the throttle position
     */
    public Double getAbsoluteThrottlePosB() {
        return absoluteThrottlePosB;
    }

    /**
     * The acceleration as determined by the phone
     *
     * @return acceleration based on the accelerometer in an iPhone
     */
    public Double getAcceleration() {
        return acceleration;
    }

    /**
     * The data point returned for the OBD II PID 01 49
     *
     * @return the accelerator throttle position D
     */
    public Double getAcceleratorThrottlePosD() {
        return acceleratorThrottlePosD;
    }

    /**
     * The data point returned for the OBD II PID 01 4A
     *
     * @return the accelerator throttle position E
     */
    public Double getAcceleratorThrottlePosE() {
        return acceleratorThrottlePosE;
    }

    /**
     * The data point returned for the OBD II PID 01 33
     *
     * @return the current barometric pressure
     */
    public Integer getBarometricPressure() {
        return barometricPressure;
    }

    /**
     * The bearing as determined by the phone
     *
     * @return bearing as determined by an iPhone
     */
    public Double getBearing() {
        return bearing;
    }

    /**
     * The data point returned for the OBD II PID 01 3C
     *
     * @return catalyst temp
     */
    public Double getCatalystTemp() {
        return catalystTemp;
    }

    /**
     * The data point returned for the OBD II PID 01 42
     *
     * @return control module voltage
     */
    public Double getControlModuleVoltage() {
        return controlModuleVoltage;
    }

    /**
     * The data point returned for the OBD II PID 01 05
     *
     * @return coolant temp
     */
    public Integer getCoolantTemp() {
        return coolantTemp;
    }

    /**
     * The data point returned for the OBD II PID 01 21
     *
     * @return distance with the "check engine light" on
     */
    public Integer getDistanceWithMilOn() {
        return distanceWithMilOn;
    }

    /**
     * The data point returned for the OBD II PID 01 04
     *
     * @return current engine load
     */
    public Double getEngineLoad() {
        return engineLoad;
    }

    /**
     * The data point returned for the OBD II PID 01 2F
     *
     * @return current amount of fuel in the tank
     */
    public Integer getFuelLevelInput() {
        return fuelLevelInput;
    }

    /**
     * The data point returned for the OBD II PID 01 03
     *
     * @return statuses of the fuel system
     */
    public Integer[] getFuelSystemStatus() {
        return fuelSystemStatus;
    }

    /**
     * The data point returned for the OBD II PID 01 0F
     *
     * @return temperature of intake air
     */
    public Integer getIntakeAirTemp() {
        return intakeAirTemp;
    }

    /**
     * The data point returned for the OBD II PID 01 0B
     *
     * @return intake mainfold pressure
     */
    public Integer getIntakeManifoldPressure() {
        return intakeManifoldPressure;
    }

    /**
     * Current latitude as determined by the phone
     *
     * @return latitude
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * The data point returned for the OBD II PID 01 07
     *
     * @return long term fuel percent
     */
    public Double getLongTermFuel() {
        return longTermFuel;
    }

    /**
     * Current longitude as determined by the phone
     *
     * @return longitude
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * The data point returned for the OBD II PID 01 10
     *
     * @return mass airflow rate
     */
    public Double getMafAirflow() {
        return mafAirflow;
    }

    /**
     * Calculated instantaneous miles per gallon
     *
     * @return current MPG
     */
    public Double getMpgInstantaneous() {
        return mpgInstantaneous;
    }

    /**
     * The data point returned for the OBD II PID 01 1C
     *
     * @return version of the OBD standard supported by the current vehicle
     */
    public Integer getObdStandards() {
        return obdStandards;
    }

    /**
     * The data point returned for the OBD II PID 01 45
     *
     * @return relative throttle position
     */
    public Double getRelativeThrottlePos() {
        return relativeThrottlePos;
    }

    /**
     * The data point returned for the OBD II PID 01 0C
     *
     * @return current engine RPM
     */
    public Double getRpm() {
        return rpm;
    }

    /**
     * The data point returned for the OBD II PID 01 06
     *
     * @return short term fuel percent
     */
    public Double getShortTermFuel() {
        return shortTermFuel;
    }

    /**
     * The data point returned for the OBD II PID 01 11
     *
     * @return throttle position
     */
    public Integer getThrottlePosition() {
        return throttlePosition;
    }

    /**
     * The data point returned for the OBD II PID 01 1F
     *
     * @return time in milliseconds since the vehicle was started
     */
    public Long getTimeSinceEngineStart() {
        return timeSinceEngineStart;
    }

    /**
     * Timestamp for the current data point
     *
     * @return timestamp in milliseconds from epoch
     */
    public Long getTimestamp() {
        return timestamp;
    }

    /**
     * The data point returned for the OBD II PID 01 0D
     *
     * @return speed in K/h
     */
    public Double getVehicleSpeed() {
        return vehicleSpeed;
    }

    /**
     * The current vehicle's vehicle identification number (VIN)
     *
     * @return the vehicle's VIN
     */
    public String getVin() {
        return vin;
    }

}
