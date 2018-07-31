const express = require('express');
const router = express.Router();
const url = require('url');
const path = require('path');


const DB = require('../database').DB;
const FB = require('../database').FB;
const Enter = require('../GPmodule/Enter').Enter;

module.exports = (io) => {
    /* !!!!!!!!!!! waning : below sentence can be executed differently on window and linux !!!!!!!!!! */
    const mainPath = __dirname.slice(0, __dirname.lastIndexOf('\\')); //path of app.js file location, this is for window
// const mainPath = __dirname.slice(0, __dirname.lastIndexOf('\'); //this is for linux.

    router.use(express.static(path.join(mainPath, '/public/ContactFrom_v13')));

    router.get('/', (req, res) => {
        if (req.isAuthenticated()) {
            const q = url.parse(req.url, true).query;
            publicID = req.user.val().Admin_Public_ID;
            lineName = q.line_name;

            let accountRef = DB.getReference("Admin_Account" + "/" + publicID);
            let locX, locY;
            let location;
            let userListRef;

            accountRef.once('value').then((snapshot)=>{
                locX = snapshot.val().x;
                locY = snapshot.val().y;

                location = {
                    x : locX.toString(),
                    y : locY.toString()
                };

                let INFO;
                let lineRef = DB.getReference("Line List" + "/" + publicID + "/" + lineName);
                let infoRef = lineRef.child("INFO");
                infoRef.once('value').then((snapshot)=>{
                    INFO = snapshot.val();

                    io.of('/userlist').on('connection', (socket) => {
                        let userList;
                        console.log('Got it from server');
                        console.log('Line List/' + publicID + '/' + lineName + '/USER LIST');
                        FB.database().ref('Line List/' + publicID + '/' + lineName + '/USER LIST').on('value', (userSnapshot) => {
                            userList = [];
                            userSnapshot.forEach((data)=> {
                                userList.push(data.val());
                            });
                            socket.emit('update', userList)
                            console.log('Emit : ');
                            console.log(userList);
                        });

                        FB.database().ref('Line List/' + publicID).limitToLast(5).on('value', (lineSnapshot) => {
                            let lineEnrollments = [];
                            lineSnapshot.forEach((line) => {
                                lineEnrollments.push(line.val().INFO.Current_Enrollment_State);
                            });
                            socket.emit('updateEnrollments', lineEnrollments);
                            console.log(lineEnrollments);
                        });
                    });

                    res.render('userlist', { INFO : INFO, location : location})
                });
            });
        } else {
            res.redirect('/');
        }
    });

    router.post('/', (req, res) => {
        const numPass = req.body.numPass;
        const q = url.parse(req.url, true).query;
        const publicID = req.user.val().Admin_Public_ID;
        const lineName = q.line_name;

        let enter = new Enter(publicID, lineName);

        if (numPass.length !== 0) {
            enter.click(parseInt(    numPass));
        }

        res.redirect('/layout/line/?line_name=' + lineName);
    });

    return router;
}