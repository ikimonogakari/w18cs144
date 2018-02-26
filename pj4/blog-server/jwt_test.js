var jwt = require('jsonwebtoken');

var payload = {
    "usr": "username"
}

let token = jwt.sign(payload, 'secret', { expiresIn: 60 * 60 });
console.log(token);

try {
    let decode = jwt.verify(token, 'secret');
    console.log(decode);
} catch (err) {
    console.log(err);
}

console.log('ends');