const firebase = require('firebase');
const googleStorage = require('@google-cloud/storage');

let __config__ = {
  apiKey: "AIzaSyAqXZJETwqfRWSjngAEs6dcks7TsQ9H2Tg",
  authDomain: "no-waiting-de057.firebaseapp.com",
  databaseURL: "https://no-waiting-de057.firebaseio.com",
  storageBucket: "gs://no-waiting-de057.appspot.com"
};
const __FB__ = firebase.initializeApp(__config__);
const __DB__ = __FB__.database();

const __storage__ = googleStorage({
    projectId: "no-waiting-de057",
    keyFilename: "./no-waiting-de057-firebase-adminsdk-s5w98-c3cf5ae426.json"
});

// module
const bucket = __storage__.bucket('gs://no-waiting-de057.appspot.com');
const DBRef = __DB__.ref('Admin_Account');

// class

class DB{
    // 전역적으로 값을 가져오므로 사용시 유의하도록 할 것.
    constructor(){
        this._config = __config__;
        this._firebase = __FB__;
        this._database = __DB__;
        this._storage = __storage__;
    }
    getReference(path = "Admin_Account") {
        return this._database.ref(path);
    }
    getBucket(path = "gs://no-waiting-de057.appspot.com"){
        return this._storage.bucket(path);
    }
}


module.exports = {
    // USAGE 참조 할 것
    DB : new DB(__config__, __storage__),
    FB : __FB__,
    DatabaseReference : DBRef,
    Bucket : bucket
};



/********** USAGE ***********

dbRef.on('value', function(snapshot) {
    snapshot.forEach(function(data) {
      console.log(data.key);
      console.log(data.val());
      console.log(data.val());
    });
});

bucket.upload('./mypnu-logo.jpg', {destination : 'Admin/마이피누 (부산대)/mypnu-logo.jpg'}, function(err, file){
    file.getSignedUrl({
        action : 'read',
        expires : '03-09-2491'
    }).then(signedUrls => {
      let newChild = dbRef.child('마이피누 (부산대)');
      newChild.set(
          {
              Admin_Prviate_ID : 'test06',
              Admin_Private_password : '1234',
              Admin_Public_ID : '마이피누 (부산대)',
              Image : signedUrls[0]
          }
      )
    })
});

 ********** USAGE **********/
