const http = require('http');
const express = require("express");
const RED = require("node-red");
const bodyParser = require('body-parser');
const cors = require('cors');

// Create an Express app
const app = express();
const PORT = process.env.PORT || 8080;

app.use(bodyParser.json())
app.use(cors())

app.get("/helloWorld", (req, res) => {
    console.log("Hello World!");
    res.send("Hello, World!");
});

// Create a server
var server = http.createServer(app);


app.listen(PORT, () => {
    console.log(`Server is listening on port ${PORT}`);
});