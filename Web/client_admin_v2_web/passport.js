const passport = require('passport');
const LocalStrategy = require('passport-local').Strategy;

const tryLogin = require('./users').tryLogin;
const DBRef = require('./database').DatabaseReference;

module.exports = () => {
    passport.use(new LocalStrategy({
            usernameField: 'ID',
            passwordField: 'pass',
            passReqToCallback: true
        }
        , function (req, ID, password, done) {
            tryLogin(ID, password, (result) => {
                if (result) {
                    let user = {
                        'ID': ID,
                    };
                    return done(null, user);
                } else {
                    return done(null, false);
                }
            });
        }
    ));

    passport.serializeUser(function (user, done) {
        done(null, user.ID);
    });

    passport.deserializeUser(function (id, done) {
        DBRef.orderByChild('Admin_Private_ID').equalTo(id).once('value').then((snapshot) => {
            if (snapshot.exists()) {
                snapshot.forEach((data) => {
                    done(null, data);
                })
            } else {
                done(null, false);
            }
        })
    });
};