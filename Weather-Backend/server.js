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
        const google_key = process.env.GOOGLE_KEY;
        const search_id = process.env.SEARCH_ID;
        var url = `https://www.googleapis.com/customsearch/v1?q=${state}%20State%20Seal&cx=${search_id}&imgSize=huge&imgType=news&num=1&searchType=image&key=${google_key}`;
        request(url, function (error, body) {
            if (error) return reject(error);
            json = JSON.parse(body);
            resolve(json);
        });
    });
}

app.get('/get-weather', (req, res) => {
    latitude = req.query.latitude;
    longitude = req.query.longitude;
    city = req.query.city;
    state = req.query.state;
    country = req.query.country;

    // call weather api to get weather info and send back to client
    var forecast_api = process.env.FORECAST_API;
    var forecast_url = process.env.FORECAST_URL + forecast_api + "/" + latitude + "," + longitude;

    request(forecast_url, function (error, data, forecast_body) {
        if (error) {
            console.log(error);
        }
        if (!error && data.statusCode == 200) {
            forecast_json = JSON.parse(forecast_body);
            msg = { weather: forecast_json, city: req.query.city, state: state, country: country };
            res.json(msg);
        }
    });
});

// get /search-by-address
app.get('/search-by-address', async (req, res) => {
    // call google map geolocation to get latitude and longitude
    const api_key = process.env.GOOGLE_KEY;
    var street = req.query.street.replace(' ', '+');
    var city = req.query.city.split(' ').join('+');
    var state = req.query.state;


    try {
        var stateSeal = await getStateSeal(state);
        console.log(stateSeal);
    } catch (error) {
        console.error(error);
    }

    var url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + street + "+" + city + ",+" + state + "&key=" + api_key;
    var latitude = "";
    var longitude = "";
    request(url, (error, response, body) => {

        if (!error && response.statusCode == 200) {
            json = JSON.parse(body);
            if (json.results.length == 0) {
                res.json({ error: 'Invalid Address.' });
            } else {
                latitude = json.results[0].geometry.location.lat;
                longitude = json.results[0].geometry.location.lng;

                // call weather api to get weather info and send back to client
                var forecast_api = process.env.FORECAST_API;
                var forecast_url = process.env.FORECAST_URL + forecast_api + "/" + latitude + "," + longitude;

                request(forecast_url, function (error, data, forecast_body) {
                    if (error) {
                        console.log(error);
                    }
                    if (!error && data.statusCode == 200) {
                        forecast_json = JSON.parse(forecast_body);
                        msg = { weather: forecast_json, seal: stateSeal, city: req.query.city, state: state };
                        res.json(msg);

                    }
                });
            }

        }
    });
});

app.get('/city-autocomplete', async (req, res) => {
    var google_place_api = process.env.GOOGLE_KEY;
    var url = 'https://maps.googleapis.com/maps/api/place/autocomplete/json?input=' + req.query.city + '&types=(cities)&language = en&key=' + google_place_api;
    request(url, function (error, response, data) {
        if (error) {
            console.log(error)
        }
        if (!error && response.statusCode == 200) {
            city_options = JSON.parse(data);

            res.json(city_options);
        }
    });
});

// get /search-by-current-location
app.get('/search-by-current-location', async (req, res) => {
    var lat = req.query.latitude;
    var lon = req.query.longitude;
    var state = req.query.region;
    var city = req.query.city;
    try {
        var stateSeal = await getStateSeal(state);
        console.log(stateSeal);
    } catch (error) {
        console.error(error);
    }

    // call weather api to get weather info and send back to client
    var forecast_api = process.env.FORECAST_API;
    var forecast_url = process.env.FORECAST_URL + forecast_api + "/" + lat + "," + lon;

    request(forecast_url, function (error, data, forecast_body) {
        if (error) {
            console.log(error);
        }
        if (!error && data.statusCode == 200) {
            forecast_json = JSON.parse(forecast_body);
            msg = { weather: forecast_json, seal: stateSeal, city: city, state: state };
            res.json(msg);
        }
    });
});

app.get('/search-by-weekly-data', async (req, res) => {
    // call weather api to get weather info and send back to client
    var forecast_api = process.env.FORECAST_API;

    // get latitude, longitude, and unix time
    var lat = req.query.latitude;
    var long = req.query.longitude;
    var time = req.query.time;
    var forecast_url = process.env.FORECAST_URL + forecast_api + "/" + lat + "," + long + "," + time;
    request(forecast_url, function (error, response, data) {
        if (error) {
            console.log(error);
        }
        if (!error && response.statusCode == 200) {
            forecast_json = JSON.parse(data);
            msg = { msg: forecast_json };
            res.json(msg);
        }
    });
});


const PORT = process.env.PORT || 3000;

app.listen(PORT, () => console.log(`server start on ${PORT}`));
