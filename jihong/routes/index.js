const express = require('express');
const router  =  express.Router();


router.get('/', (req, res, next) => {
    if (req.isAuthenticated()) {
        res.render('index', {name : 'Logout' });
    } else {
        res.render('index', { name : 'Login' });
    }
});

module.exports = router;