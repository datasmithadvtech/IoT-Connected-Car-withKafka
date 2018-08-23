
using Microsoft.CSharp.RuntimeBinder;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Dynamic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using Data_Filter.Models;
using Newtonsoft.Json.Linq;
namespace Data_Filter.Controllers
{
    public class HomeController : ApiController
    {
        public static List<JObject> list = new List<JObject>();
        // GET: api/Home
        public IEnumerable<JObject>  Get()
        {
            return list;
        }

        // GET: api/Home/1
		// IEnumerable<JObject> list is act as queue 
		// id = 1 will return the First Json Object from the list and delete it 
        public HttpResponseMessage Get(int id)
        {
            var field = list.FirstOrDefault();
            if (field == null)
            {
                return Request.CreateErrorResponse(HttpStatusCode.NotFound, "There is No Data Yet.");
            }
            else {
                list.Remove(field);
                return Request.CreateResponse(HttpStatusCode.OK, field);
                 }
        }

        // POST: api/Home
		// recieve the Posted json String and make Filteration 
        public HttpResponseMessage Post([FromBody]dynamic postedJson)
        {
            HttpResponseMessage msg = null;
          
            string fuelLevelInput = postedJson.fuel_level_input != null ? postedJson.fuel_level_input : null;
            string massAirFlow = postedJson.maf_airflow != null ? postedJson.maf_airflow : null;
            string rpm = postedJson.rpm != null ? postedJson.rpm : null;
            string intakeManifoldPressure = postedJson.intake_manifold_pressure != null ? postedJson.intake_manifold_pressure : null;
            string intakeAirTemp = postedJson.intake_air_temp != null ? postedJson.intake_air_temp : null;
            string vehicleSpeed = postedJson.vehicle_speed != null ? postedJson.vehicle_speed : null;
            
           
            if ((fuelLevelInput == null || fuelLevelInput == "") || (vehicleSpeed == null || vehicleSpeed == "")) {
                return Request.CreateErrorResponse(HttpStatusCode.BadRequest, "Rejecting $payload because either fuelLevelInput or vehicleSpeed are empty");
            }
            else
            {
                if ((massAirFlow == null || massAirFlow == "")) {
                    if ((rpm !=null || rpm == "") || (intakeAirTemp != null || intakeAirTemp == "") || (intakeManifoldPressure != null || intakeManifoldPressure == "")) {
                        return Request.CreateErrorResponse(HttpStatusCode.BadRequest, "Rejecting $payload because maf related fields are not available");      
                 }
                }
            }
            if(addToList(postedJson))
            msg = Request.CreateResponse(HttpStatusCode.Created, "Added"+list.Count());
              //  Debug.WriteLine(value);
                return msg;    
        }

		// add the filtered Json to the List
        private bool addToList(dynamic item)
        {
            JObject postedJson = (JObject)item;
          
            try { 
            list.Add(postedJson);
                return true;
            }catch(Exception ex)
            {
                Debug.WriteLine("Exception : "+ex.Message );
                return false;
            }
        }
    }
}
