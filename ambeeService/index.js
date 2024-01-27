const express = require("express");
const bodyParser = require('body-parser');
const cors = require('cors');
axios = require('axios');
const cache = require('./cache');


// Create an Express app
const app = express();
const PORT = process.env.PORT || 8081;
const AMBEE_API_KEY = "212fa23cbf05221133f3257d274226363b506543c6464c04c47bc2eb9fb3c469"
const URL_PREFIX = "https://api.ambeedata.com/latest/pollen/by-lat-lng?lat=";
const URL_SUFFIX = "&lng=";

app.use(bodyParser.json());
app.use(cors());

app.get("/helloWorld", (req, res) => {
    res.send("Hello, World!");
});

app.get("/getPollen", cache(900), async (req, res) => {
    try {
        let result = await axios.get("https://api.ambeedata.com/latest/pollen/by-lat-lng?lat=43.615557652935706&lng=7.071861733131095", {headers: {"x-api-key": AMBEE_API_KEY}});
        let simplifiedData = getSimplifiedData(result);
        res.send(simplifiedData);
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
    return {
        grassPollenLevel: grassPollenLevel,
        treePollenLevel: treePollenLevel,
        weedPollenLevel: weedPollenLevel,
        highestPollenRisk: highestPollenRisk
    };
}

function getHighestPollenRisk(grassPollenRisk, treePollenRisk, weedPollenRisk) {
    if (grassPollenRisk === "Very High" || treePollenRisk === "Very High" || weedPollenRisk === "Very High") {
        return "Very High";
    } else if (grassPollenRisk === "High" || treePollenRisk === "High" || weedPollenRisk === "High") {
        return "High";
    } else if (grassPollenRisk === "Moderate" || treePollenRisk === "Moderate" || weedPollenRisk === "Moderate") {
         return "Moderate";
    } else {
        return "Low";
    }
}

app.listen(PORT, () => {
    console.log(`Server is listening on port ${PORT}`);
});