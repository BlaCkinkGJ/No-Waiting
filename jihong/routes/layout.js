const express = require('express');
const router = express.Router();
const path = require('path');

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
    let startDate = req.body.startDate;
    let startTime = req.body.startTime;

    console.log(startDate);
    console.log(startTime);
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



module.exports = router;