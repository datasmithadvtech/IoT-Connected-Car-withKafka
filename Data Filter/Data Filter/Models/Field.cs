using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Data_Filter.Models
{
    public class Field
    {
        public double bearing { get; set; }
        public double catalyst_temp { get; set; }
        public string vin { get; set; }
        public double absolute_throttle_pos_b { get; set; }
        public int fuel_level_input { get; set; }
        public string intake_manifold_pressure { get; set; }
        public double maf_airflow { get; set; }
        public int barometric_pressure { get; set; }
        public double accelerator_throttle_pos_d { get; set; }
        public double accelerator_throttle_pos_e { get; set; }
        public string journeyId { get; set; }
        public double rpm { get; set; }
        public double acceleration { get; set; }
        public double relative_throttle_pos { get; set; }
        public long time_since_engine_start { get; set; }
        public int distance_with_mil_on { get; set; }
        // public long timestamp { get; set; }
        public int[] fuel_system_status { get; set; }
        // public double mpg_instantaneous { get; set; }
        public double control_module_voltage { get; set; }
        public int intake_air_temp { get; set; }
        public double latitude { get; set; }
        public double long_term_fuel { get; set; }
        // public string available_pids_0_20 { get; set; }
        // public string available_pids_41_60 { get; set; }
        public double short_term_fuel { get; set; }
        public double engine_load { get; set; }
        public int obd_standards { get; set; }
        public double longitude { get; set; }
        public int throttle_position { get; set; }
        public double vehicle_speed { get; set; }
        // public string available_pids_21_40 { get; set; }
        public int coolant_temp { get; set; }
    }
}