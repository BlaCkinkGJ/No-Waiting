const express = require('express');
const router = express.Router();
const url = require('url');
const path = require('path');


const DB = require('../database').DB;
const Enter = require('../GPmodule/Enter');

/* !!!!!!!!!!! waning : below sentence can be executed differently on window and linux !!!!!!!!!! */
const mainPath = __dirname.slice(0, __dirname.lastIndexOf('\\')); //path of app.js file location, this is for window
// const mainPath = __dirname.slice(0, __dirname.lastIndexOf('\'); //this is for linux.

router.use(express.static(path.join(mainPath, '/public/ContactFrom_v13')));

router.get('/', (req, res) => {
    if (req.isAuthenticated()) {
        const q = url.parse(req.url, true).query;
        const publicID = req.user.val().Admin_Public_ID;
        const lineName = q.line_name;

        let accountRef = DB.getReference("Admin_Account" + "/" + publicID);
        let locX, locY;
        let location;
        accountRef.once('value').then((snapshot)=>{
           locX = snapshot.val().x;
           locY = snapshot.val().y;

           location = {
               x : locX.toString(),
               y : locY.toString()
           }

            let INFO;
            let lineRef = DB.getReference("Line List" + "/" + publicID + "/" + lineName);
            let infoRef = lineRef.child("INFO");
            infoRef.once('value').then((snapshot)=>{
                INFO = snapshot.val();

                let userList = [];
                let userListRef = lineRef.child('USER LIST');
                userListRef.once('value').then((snapshot) => {
                    snapshot.forEach((data)=> {
                        userList.push(data.val());
                    });
                    console.log(location);
                    res.render('userlist', { INFO : INFO, userlist : userList, location : location});
                })
            });
        });
    } else {
        res.redirect('/');
    }
})

router.post('/', (req, res) => {
   const checkedUsers = req.body.checkedUsers;
    const q = url.parse(req.url, true).query;
    const publicID = req.user.val().Admin_Public_ID;
    const lineName = q.line_name;

   const lineRef = DB.getReference("Line List/" + publicID + "/" + lineName + '/USER LIST');

   lineRef.once('value').then((snapshot) => {
       console.log(snapshot.val());
   })
    console.log(checkedUsers);
   for (let i = 0; i < checkedUsers.length; i++) {
       lineRef.orderByChild('User_ID').equalTo(checkedUsers[i]).once('value').then((snapshot) => {
           console.log(snapshot.val());
       })
   }

   res.send('Got it');
});

module.exports = router;