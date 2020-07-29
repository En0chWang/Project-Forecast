const express = require('express');
var request = require('request');

// Init express
const app = express();

var bodyParser = require('body-parser');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

app.all('*', function (req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "Content-Type,Content-Length,Authorization,Acccept,X-Requested-With");
    res.header("Access-Control-Allow-Methods", "PUT,POST,GET,DELETE,OPTIONS");
    res.header("X-Powered-By", '3.2.1');
    if (req.method == "OPTIONS") res.send(200);
    else next();
});

function getStateSeal(state) {
    return new Promise(function (resolve, reject) {
        const google_key = "AIzaSyDAPCMh2SabpU84KuUzvUh1NyBewWzrGHU";
        const search_id = "009900325867064746831:hfchxhdjxrb";
        var url = `https://www.googleapis.com/customsearch/v1?q=${state}%20State%20Seal&cx=${search_id}&imgSize=huge&imgType=news&num=1&searchType=image&key=${google_key}`;
        request(url, function (error, body) {
            if (error) return reject(error);
            json = JSON.parse(body);
            resolve(json);
        });
    });
}

app.get('/get-images', (req, res) => {
    url = "https://www.googleapis.com/customsearch/v1";
    q = req.query.q;
    cx = req.query.cx;
    imgSize = req.query.imgSize;
    imgType = req.query.imgType;
    num = req.query.num;
    searchType = req.query.searchType;
    key = req.query.key;
    enconded = url + "?q=" + q + "&cx=" + cx + "&imgSize=" + imgSize + "&imgType=" + imgType + "&num=" + num + "&searchType=" + searchType + "&key=" + key;
    request(enconded, function (error, data, body) {
        if (error) {
            console.log(error);
        }
        if (!error && data.statusCode == 200) {
            img_json = JSON.parse(forecast_body);
            res.json(img_json);
        }

    });
});




const PORT = process.env.PORT || 3000;

app.listen(PORT, () => console.log(`server start on ${PORT}`));
