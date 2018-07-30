const express     = require('express');
const router      = express.Router();
const multer     = require('multer');
const upload     = multer({ dest: 'uploads/'});
const path        = require('path');
const dbRef       = require('../database').DatabaseReference;
const bucket      = require('../database.js').Bucket;


const mainPath = __dirname.slice(0, __dirname.lastIndexOf('\\')); //path of app.js file location
router.use(express.static(path.join(mainPath, '/public')));

router.get('/', (req, res, next) => {
    res.render('login');
});

router.post('/', (req, res) => {
   const id = req.body.ID;
   const password = req.body.pass;

   console.log(id);

   dbRef.orderByChild('Admin_Private_ID').equalTo(id).on('value', (snapshot) => {
      if (snapshot.exists()) {
         snapshot.forEach( (data) => {
            if (data.val().Admin_Private_password === password) {
               found = true;
               res.send('Login Success');
            } else {
               res.send('Invalid password');
            }
         });
      } else {
         res.send('No exists');
      }
   });
});


router.get('/signup', (req, res, next) => {
    res.render('signup');
});

router.post('/signup', upload.single('userfile'), (req, res) => {
    const id = req.body.ID;
    const password = req.body.pass;
    const publicID = req.body.publicID;
    const description = req.body.description;
    const file = req.file;

    //exceptions handler
    if (id === undefined) {

    }

    if (password === undefined) {

    }

    if (publicID === undefined) {

    }

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
                let newChild = dbRef.child(publicID);
                newChild.set(
                    {
                        Admin_Prviate_ID : id,
                        Admin_Private_password : password,
                        Admin_Public_ID : publicID,
                        //Description : description
                        Image : signedUrls[0]
                    }
                )
            });
            //end get download url
        }
    });
});

module.exports = router;