const firebase = require('firebase');
const googleStorage = require('@google-cloud/storage');

let config = {
  apiKey: "AIzaSyAqXZJETwqfRWSjngAEs6dcks7TsQ9H2Tg",
  authDomain: "no-waiting-de057.firebaseapp.com",
  databaseURL: "https://no-waiting-de057.firebaseio.com",
  storageBucket: "gs://no-waiting-de057.appspot.com"
};
const FB = firebase.initializeApp(config);
const DB = FB.database();

const storage = googleStorage({
    projectId: "no-waiting-de057",
    keyFilename: "./no-waiting-de057-firebase-adminsdk-s5w98-c3cf5ae426.json"
});

const bucket = storage.bucket('gs://no-waiting-de057.appspot.com');
const DBRef = DB.ref('Admin_Account');

/*
dbRef.on('value', function(snapshot) {
    snapshot.forEach(function(data) {
      console.log(data.key);
      console.log(data.val());
    });
});
*/

/*
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
*/

module.exports = {
    DatabaseReference : DBRef,
    Bucket : bucket
};