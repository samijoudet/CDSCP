const http = require('http');
const express = require("express");
const RED = require("node-red");
const bodyParser = require('body-parser');
const cors = require('cors');
axios = require('axios');

// Create an Express app
const app = express();
const PORT = process.env.PORT || 8080;
const host = "http://localhost:1880";

app.use(bodyParser.json());
app.use(cors());

async function getCO2FromNodeRed() {
    let result = await axios.get(host + "/getCO2");
    return result.data;
}

app.get("/helloWorld", (req, res) => {
    res.send("Hello, World!");
});

app.get("/getCO2", async (req, res) => {
    let result = await getCO2FromNodeRed();
    console.log(result);
    res.send(result);
});

app.listen(PORT, () => {
    console.log(`Server is listening on port ${PORT}`);
});