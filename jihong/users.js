const DBRef = require('./database.js').DatabaseReference;

module.exports = {
    //id, password에 해당하는 정보가 있을경우 true, 이외에는 false 반환.
    tryLogin : (id, password, callback) => {
        DBRef.orderByChild('Admin_Private_ID').equalTo(id).once('value').then((snapshot) => {
            if (snapshot.exists()) {
                snapshot.forEach( (data) => {
                    if (data.val().Admin_Private_password === password) {
                        return callback(true);
                    } else {
                        return callback(false);
                    }
                });
            } else {
                return callback(false);
            }
        });
    },
    //id가 존재하면 true 이외에는 false를 반환.
    isExists : (id, callback) => {

        DBRef.orderByChild('Admin_Private_ID').equalTo(id).once('value').then((snapshot) => {
            if (snapshot.exists())
                return callback(true);
            else
                return callback(false);
    })}
};