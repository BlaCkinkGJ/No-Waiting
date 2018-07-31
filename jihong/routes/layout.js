const express = require('express');
const router = express.Router();
const path = require('path');
const url = require('url');

const DB = require('../database').DB;
const LineListRef = DB.getReference('Line List');

const lineRouter = require('./line');

const mainPath = __dirname.slice(0, __dirname.lastIndexOf('\\')); //path of app.js file location
router.use(express.static(path.join(mainPath, '/public')));
router.use('/line', lineRouter);

router.get('/register', (req, res) => {
   if (req.isAuthenticated())  {
        res.render('newline');
   } else {
       res.redirect('/');
   }
});

router.post('/register', (req, res) => {
    const lineName = req.body.lineName;
    const maxNumber = req.body.maxNumber;
    const personalInterval = req.body.personalInterval;

    const startDate = req.body.startDate + " " + req.body.startTime;
    const closingDate = req.body.closingDate + " " + req.body.closingTime;

    console.log(startDate);
    console.log(closingDate);

    const publicID = req.user.val().Admin_Public_ID;
    const adminID = req.user.val().Admin_Private_ID;

    let lineRef = DB.getReference("Line List" + "/" + publicID);
    let privateBucketRef = DB.getBucket().child(publicID);


    let newChild = lineRef.child(lineName);
    newChild.set(
        {
            Admin_ID : adminID,
            INFO : {
                Closing_Time : closingDate,
                Current_Enrollment_State : 0,
                Line_Name : lineName,
                Max_Number : parseInt(maxNumber),
                Opening_Time : startDate,
                Personal_Interval : personalInterval,
                Public_ID : publicID,
                Public_Key : "1"
            }
        }
    )

    res.redirect('/layout');
});

router.get('/', (req, res) => {
    if (req.isAuthenticated()) {
        let values = [];

        LineListRef.orderByKey().equalTo(req.user.val().Admin_Public_ID).once('value').then((snapshot) => {
            snapshot.forEach((data) => {
                data.forEach((line) => {
                    values.push(line.val());
                })
            });
            res.render('layout', { image : req.user.val().Image, snapshot : values});
        })
    } else {
        res.redirect('/');
    }
});

router.post('/', (req, res) => {
    const q = url.parse(req.url, true).query;
    const publicID = req.user.val().Admin_Public_ID;
    const lineName = q.line_name;

    console.log(publicID + lineName);

    const lineRef = DB.getReference('Line List/' + publicID);
    let target = lineRef.child(lineName);

    //target.delete();
});

module.exports = router;