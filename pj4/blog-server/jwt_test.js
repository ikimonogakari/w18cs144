var jwt = require('jsonwebtoken');
var jwt_decode = require('jwt-decode');

var payload = {
    "usr": "cs144"
}

let token = jwt.sign(payload, 'secret', { expiresIn: 60 * 60 });
console.log(token);

try {
    let decode = jwt.verify(token, 'secret');
    //console.log(decode);
} catch (err) {
    console.log(err);
}

let decode = jwt_decode(token);
console.log(decode);

console.log('ends');
