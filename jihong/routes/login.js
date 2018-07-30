const express       = require('express');
const router        = express.Router();
const multer        = require('multer');
const upload        = multer({ dest: 'uploads/'});
const path          = require('path');
const DBRef         = require('../database').DatabaseReference;
const bucket        = require('../database').Bucket;
const Map           = require('../GPmodule/MapDAT').Map;

const tryLogin = require('../users').tryLogin;
const ixExists = require('../users').isExists;

const passport = require('passport');

const mainPath = __dirname.slice(0, __dirname.lastIndexOf('\\')); //path of app.js file location
router.use(express.static(path.join(mainPath, '/public')));



/*
router.use(session(  {
                   key : data.val().Admin_Public_ID,
                   secret : data.val().Admin_Private_password,
                   cookie : {
                       maxAge : 1000 * 60 * 60 // 1 hour
                   }
                }));
 */

router.get('/', (req, res) => {
    res.render('login')
});

router.post('/', passport.authenticate('local', { failureRedirect: '/login', failureFlash: true }), (req, res) => {
   res.redirect('/layout');
});

router.get('/signup', (req, res, next) => {
    res.render('signup');
});

router.post('/signup', upload.single('userfile'), (req, res) => {
    const id = req.body.ID;
    const password = req.body.pass;
    const publicID = req.body.publicID;
    const address = req.body.address;
    const description = req.body.description;
    const file = req.file;

    let locX, locY;
    Map.findPoint(address, (ptr) => {
        locX = ptr['y'];
        locY = ptr['x'];
    });

    //exceptions handler
    if (id === undefined) {

    }
    //check if id is already exists.
    //...

    if (password === undefined) {

    }

    if (publicID === undefined) {

    }
    //check if public ID is already exists.
    //...

    if (description === undefined) {

    }

    if (file === undefined) {

    }

    //check file is image. if not warning.
    let destName = 'Admin/' + publicID + '/' + file.filename;


    if (file.mimetype.split('/')[0] === 'image') {
        destName = destName + '.' + file.mimetype.split('/')[1];
    } else {

    }

    bucket.upload(path.join(mainPath, file.path), {destination : destName, metadata : { contentType : file.mimetype }}, function(err, file) {
        if (err) {
            console.log('Error');
        } else {
            //start get download url
            file.getSignedUrl({
                action : 'read',
                expires : '03-09-2491'
            }).then(signedUrls => {
                let newChild = DBRef.child(publicID);
                newChild.set(
                    {
                        Admin_Private_password : password,
                        Admin_Private_ID : id,
                        Admin_Public_ID : publicID,
                        Description : description,
                        Image : signedUrls[0],
                        x : locX,
                        y : locY
                    }
                )
            });
            //end get download url
        }
        res.redirect('/login');
    });
});

module.exports = router;