const express = require("express");
const bodyParser = require('body-parser');
const cors = require('cors');
axios = require('axios');

// Create an Express app
const app = express();
const PORT = process.env.PORT || 8080;
const host = "http://localhost:1880";

app.use(bodyParser.json());
app.use(cors());

async function getFromNodeRed(path) {
    let result = await axios.get(host + path);
    return result.data;
}

app.get("/helloWorld", (req, res) => {
    res.send("Hello, World!");
});

app.get("/getCO2", async (req, res) => {
    let result = await getFromNodeRed("/getCO2");
    res.send(result);
});

app.get("/getPollen", async (req, res) => {
    let result = await getFromNodeRed("/getPollen");
    let highestRisk = result.highestPollenRisk;
    res.send(highestRisk);
});

app.get("/getOpenState", async (req, res) => {
    let result = await getFromNodeRed("/getOpenState");
    res.send(result);
});

app.get("/WallPlug12_Switch", async (req, res) => {
    let status = req.query.status;
    let result = await getFromNodeRed("/WallPlug12_Switch?status=" + status);
    console.log(result);
});

app.listen(PORT, () => {
    console.log(`Server is listening on port ${PORT}`);
});