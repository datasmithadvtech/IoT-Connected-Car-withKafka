using Newtonsoft.Json;
using System;
using System.ComponentModel;
using System.IO;
using System.Threading;
using System.Windows;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Threading.Tasks;
using System.Net;
using Newtonsoft.Json.Linq;

namespace CarSimulator
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        // 3 threads for each car to read data
        private BackgroundWorker truckWorker = null;
        private BackgroundWorker vanWorker = null;
        private BackgroundWorker carWorker = null;
        //cars' data sets
     
        public string truckDataPath = null;
        public string vanDataPath = null;
        public string carDataPath = null;

        // to start/pause truck 
        public AutoResetEvent truckAutoRest = new AutoResetEvent(false);
        bool stopTruckWorker = false;
        // to start/pause van 
        public AutoResetEvent vanAutoRest = new AutoResetEvent(false);
        bool stopVanWorker = false;
        // to start/pause car 
        public AutoResetEvent carAutoRest = new AutoResetEvent(false);
        bool stopCarWorker = false;

        //journeys' ids
        public string id1 = null;
        public string id2 = null;
        public string id3 = null;

        // http client object to send json to data filter api
        HttpClient client;
        //data filter url
        string url = null;
       
        public MainWindow()
        {
            InitializeComponent();
            // initialize truck Worker  (Thread)
            truckWorker = new BackgroundWorker();
            truckWorker.WorkerSupportsCancellation = true;
            truckWorker.WorkerReportsProgress = true;
            truckWorker.DoWork += startTruck;
            truckWorker.RunWorkerCompleted += WorkerCompleted;

            // initialize van Worker  (Thread)
            vanWorker = new BackgroundWorker();
            vanWorker.WorkerSupportsCancellation = true;
            vanWorker.WorkerReportsProgress = true;
            vanWorker.DoWork += startVan;
            vanWorker.RunWorkerCompleted += WorkerCompleted;

            // initialize car Worker (Thread)
            carWorker = new BackgroundWorker();
            carWorker.WorkerSupportsCancellation = true;
            carWorker.WorkerReportsProgress = true;
            carWorker.DoWork += startCar;
            carWorker.RunWorkerCompleted += WorkerCompleted;

            //get fullPath for datasets dynamically
            var fullPath = System.AppDomain.CurrentDomain.BaseDirectory;
            int endIndex = fullPath.IndexOf("bin");
            fullPath = fullPath.Substring(0, endIndex);
            truckDataPath = fullPath +"datasets\\truckData.out";
            vanDataPath = fullPath + "datasets\\vanData.out";
            carDataPath = fullPath + "datasets\\carData.out";

            // generate unique id for each journey
            id1 = Guid.NewGuid().ToString("N");
            id2 = Guid.NewGuid().ToString("N");
            id3 = Guid.NewGuid().ToString("N");

            //data filter url
            url = "http://data-filter.azurewebsites.net/";

            //initialize http client
            this.client = new HttpClient();
            this.client.BaseAddress = new Uri(url);
            this.client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));

        }
		// start reading truck data sets when user click start button 

        private void buttStartCar1_Click(object sender, RoutedEventArgs e)
        {
            
            if(stopTruckWorker == true)
            {
                stopTruckWorker = false;
                truckAutoRest.Set();  
            }
            else
            {
                truckWorker.RunWorkerAsync();
                
            }
            buttStartCar1.Content = "   Started";
            buttPauseCar1.Content = "     Pause";
        }

		// stop reading truck data sets when user click Pause button 
        private void buttPauseCar1_Click(object sender, RoutedEventArgs e)
        {
            stopTruckWorker = true;
            buttPauseCar1.Content = "    Paused";
            buttStartCar1.Content = "   Continue";
            
        }

		// start reading Van data sets when user click start button 
        private void buttStartCar2_Click(object sender, RoutedEventArgs e)
        {
            if (stopVanWorker == true)
            {
                stopVanWorker = false;
                vanAutoRest.Set();
            }
            else
            {
                vanWorker.RunWorkerAsync();
            }               
            buttStartCar2.Content = "   Started";
            buttPauseCar2.Content = "     Pause";
        }

		// stop reading Van data sets when user click Pause button 
        private void buttPauseCar2_Click(object sender, RoutedEventArgs e)
        {
            stopVanWorker = true;
            buttPauseCar2.Content = "    Paused";
            buttStartCar2.Content = "   Continue";
        }
		

		// start reading Car data sets when user click start button 
        private void buttStartCar3_Click(object sender, RoutedEventArgs e)
        {
            if (stopCarWorker == true)
            {
                stopCarWorker = false;
                carAutoRest.Set();
            }
            else
            {
                carWorker.RunWorkerAsync();
            }
            buttStartCar3.Content = "   Started";
            buttPauseCar3.Content = "     Pause";
        }

		// start reading Car data sets when user click start button 
        private void buttPauseCar3_Click(object sender, RoutedEventArgs e)
        {
            stopCarWorker = true;
            buttPauseCar3.Content = "    Paused";
            buttStartCar3.Content = "   Continue";
        }


        /// <summary>
        /// start Reading Truck Data & Post it to Data filter Service
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        void startTruck(object sender, DoWorkEventArgs e)
        {
            
                try
                {
                   
                    using (StreamReader sr = new StreamReader(truckDataPath))
                    {
                        while (sr.Peek() >= 0)
                        {
                        if (stopTruckWorker == true)
                        {
                            truckAutoRest.WaitOne();
                        }
                        else {

                            string json = sr.ReadLine();
                            JObject item = new JObject();
                            item = JObject.Parse(json);
                            item.Add("journey_id", id1);
                            item.Remove("available_pids_21-40"); 
                            item.Remove("mpg_instantaneous");
                            item.Remove("timestamp");
                            item.Remove("available_pids_41-60");
                            item.Remove("available_pids_0-20");
                            item.Remove("guid");
                            HttpResponseMessage response =  postJson(item);
                            if (response.IsSuccessStatusCode) {
                                // MessageBox.Show("Success!");
                            }
                            else if (response.StatusCode == HttpStatusCode.BadRequest)
                            {
                                //MessageBox.Show("Rejected!");
                            }
                            else MessageBox.Show("Bad Request!");
                            Thread.Sleep(1000);
                        }
                    }
                    }
                }
                catch (Exception ex)
                {
                    MessageBox.Show("Truck The process failed:"+ ex.Message);

                }
                
                

            
            
        }
        /// <summary>
        /// start Reading Van Data & Post it to Data filter Service
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        void startVan(object sender, DoWorkEventArgs e)
        {

            try
            {

                using (StreamReader sr = new StreamReader(vanDataPath))
                {
                    while (sr.Peek() >= 0)
                    {
                        if (stopVanWorker == true)
                        {
                            vanAutoRest.WaitOne();
                        }
                        else
                        {

                            string json = sr.ReadLine();
                            JObject item = new JObject();
                            item = JObject.Parse(json);
                            item.Add("journey_id", id1);
                            item.Remove("available_pids_21-40");
                            item.Remove("mpg_instantaneous");
                            item.Remove("timestamp");
                            item.Remove("available_pids_41-60");
                            item.Remove("available_pids_0-20");
                            item.Remove("guid");
                            HttpResponseMessage response = postJson(item);
                            if (response.IsSuccessStatusCode)
                            {
                               // MessageBox.Show("Success!");
                            }
                            else if (response.StatusCode == HttpStatusCode.BadRequest)
                            {
                               // MessageBox.Show("Rejected!");
                            }
                            else MessageBox.Show("Bad Request!");
                            Thread.Sleep(1000);
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("VAN The process failed:" + ex.Message);   
            }

        }

        /// <summary>
        /// start Reading Car Data & Post it to Data filter Service
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        void startCar(object sender, DoWorkEventArgs e)
        {


            
            try
            {

                using (StreamReader sr = new StreamReader(carDataPath))
                {
                    while (sr.Peek() >= 0)
                    {
                        if (stopCarWorker == true)
                        {
                            carAutoRest.WaitOne();
                        }
                        else
                        {

                            string json = sr.ReadLine();
                            JObject item = new JObject();
                            item = JObject.Parse(json);
                            item.Add("journey_id", id1);
                            item.Remove("vin");
                            item.Remove("available_pids_21-40");
                            item.Remove("mpg_instantaneous");
                            item.Remove("timestamp");
                            item.Remove("available_pids_41-60");
                            item.Remove("available_pids_0-20");
                            item.Remove("guid");      
                            item.Add("vin", "CAR2014HELWAN");
                            HttpResponseMessage response = postJson(item);
                            if (response.IsSuccessStatusCode)
                            {
                              //  MessageBox.Show("Success!");
                            }
                            else if (response.StatusCode == HttpStatusCode.BadRequest)
                            {
                              //  MessageBox.Show("Rejected!");
                            }
                            else MessageBox.Show("Bad Request!");
                            Thread.Sleep(1000);
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("CAR The process failed:" + ex.Message);

            }
        }


        void WorkerCompleted(object sender, RunWorkerCompletedEventArgs e)
        {
            
            MessageBox.Show("Exception : There Is No Data To Read");
        }

        // post the json object
        HttpResponseMessage postJson<TModel>(TModel item)
        {
            var response = client.PostAsJsonAsync("api/Home/",item).Result;
            return response;
        }
    }
}
