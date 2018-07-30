const express = require('express');
const router = express.Router();
const url = require('url');
const path = require('path');


const DB = require('../database').DB;
const Enter = require('../GPmodule/Enter');

const mainPath = __dirname.slice(0, __dirname.lastIndexOf('\\')); //path of app.js file location
router.use(express.static(path.join(mainPath, '/public/ContactFrom_v13')));

router.get('/', (req, res) => {
    if (req.isAuthenticated()) {
        const q = url.parse(req.url, true).query;
        const publicID = req.user.val().Admin_Public_ID;
        const lineName = q.line_name;
        console.log(publicID + lineName);

        let lineSnapshot;
        let userlist = [];

        let ref = DB.getReference("Line List" + "/" + publicID + "/" + lineName);
        ref = ref.child("INFO");
        ref.once('value').then((snapshot)=>{
            snapshot.forEach((data)=>{
                console.log(data.val());
            });
        });

        //console.log(lineSnapshot)
        res.render('userlist', { lineSnapshot : lineSnapshot, userlist : userlist });
    } else {
        res.redirect('/');
    }
})

module.exports = router;