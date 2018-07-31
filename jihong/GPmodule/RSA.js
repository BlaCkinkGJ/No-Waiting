const nodeRSA = require('node-rsa');

const key = new nodeRSA({b:512});


let priv = key.exportKey('pkcs8-private-der');
let pub  = key.exportKey('pkcs8-public-der');

priv = new Buffer(priv, 'binary').toString('base64');
pub = new Buffer(pub, 'binary').toString('base64');


module.exports ={
    keyPair : {
        "private" : priv,
        "public"  : pub
    }
};
