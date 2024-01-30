const express = require("express");
const bodyParser = require('body-parser');
const cors = require('cors');
axios = require('axios');
const cache = require('./cache');


// Create an Express app
const app = express();
const PORT = process.env.PORT || 8081;
const AMBEE_API_KEY = "212fa23cbf05221133f3257d274226363b506543c6464c04c47bc2eb9fb3c469";
const AMBEE_URL_PREFIX = "https://api.ambeedata.com/latest/pollen/by-lat-lng?lat=";
const AMBEE_URL_SUFFIX = "&lng=";
const OPEN_WEATHER_API_KEY = "&appid=86588862e9290e719bd1e359defef140";
const OPEN_WEATHER_URL_PREFIX = "https://api.openweathermap.org/data/2.5/";
const POLLUTION_URL_SUFFIX = "air_pollution?lat=";
const WEATHER_URL_SUFFIX = "weather?lat=";
const OPEN_WEATHER_URL_SUFFIX = "&lon=";
const LATITUDE = "43.615557652935706";
const LONGITUDE = "7.071861733131095";

app.use(bodyParser.json());
app.use(cors());

app.get("/helloWorld", (req, res) => {
    res.send("Hello, World!");
});

app.get("/getPollen", cache(900), async (req, res) => {
    try {
        let result = await axios.get(AMBEE_URL_PREFIX+LATITUDE+AMBEE_URL_SUFFIX+LATITUDE, {headers: {"x-api-key": AMBEE_API_KEY}});
        let simplifiedData = getSimplifiedData(result);
        res.send(simplifiedData);
    } catch (error) {
        console.log(error);
        res.status(500).send("Error fetching data");
    }
});

app.get("/getWeather", cache(900), async (req, res) => {
    try {
        let resultPollen = await axios.get(AMBEE_URL_PREFIX+LATITUDE+AMBEE_URL_SUFFIX+LONGITUDE, {headers: {"x-api-key": AMBEE_API_KEY}})
        let simplifiedPollenData = getSimplifiedData(resultPollen);
        let resultPollution = await axios.get(OPEN_WEATHER_URL_PREFIX+POLLUTION_URL_SUFFIX+LATITUDE+OPEN_WEATHER_URL_SUFFIX+LONGITUDE+OPEN_WEATHER_API_KEY);
        let simplidiedPollutionData = resultPollution.data.list[0].main.aqi;
        let resultTemperature = await axios.get(OPEN_WEATHER_URL_PREFIX+WEATHER_URL_SUFFIX+LATITUDE+OPEN_WEATHER_URL_SUFFIX+LONGITUDE+OPEN_WEATHER_API_KEY);
        let simplifiedTemperatureData = resultTemperature.data.main.temp - 273.15;
        let fusionResult = {
            temperatureExterieur: simplifiedTemperatureData,
            pollutionExterieur: simplidiedPollutionData,
            pollenExterieur: simplifiedPollenData
        };

        res.send(fusionResult);
    } catch (error) {
        console.log(error);
        res.status(500).send("Error fetching data");
    }
});





function getSimplifiedData(result) {
    let grassPollenLevel = result.data.data[0].Count.grass_pollen;
    let treePollenLevel = result.data.data[0].Count.tree_pollen;
    let weedPollenLevel = result.data.data[0].Count.weed_pollen;
    let grassPollenRisk = result.data.data[0].Risk.grass_pollen;
    let treePollenRisk = result.data.data[0].Risk.tree_pollen;
    let weedPollenRisk = result.data.data[0].Risk.weed_pollen;
    let highestPollenRisk = getHighestPollenRisk(grassPollenRisk, treePollenRisk, weedPollenRisk);
    return highestPollenRisk;
}

function getHighestPollenRisk(grassPollenRisk, treePollenRisk, weedPollenRisk) {
    if (grassPollenRisk === "Very High" || treePollenRisk === "Very High" || weedPollenRisk === "Very High") {
        return 3;
    } else if (grassPollenRisk === "High" || treePollenRisk === "High" || weedPollenRisk === "High") {
        return 2;
    } else if (grassPollenRisk === "Moderate" || treePollenRisk === "Moderate" || weedPollenRisk === "Moderate") {
         return 1;
    } else {
        return 0;
    }
}

app.listen(PORT, () => {
    console.log(`Server is listening on port ${PORT}`);
});