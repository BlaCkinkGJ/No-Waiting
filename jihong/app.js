const createError = require('http-errors');
const express = require('express');
const path = require('path');
const cookieParser = require('cookie-parser');
const logger = require('morgan');
const ejs = require('ejs');
const passport = require('passport');
const passportConfig = require('./passport');
const session = require('express-session');

const indexRouter   = require('./routes/index');
const loginRouter   = require('./routes/login');
const layoutRouter  = require('./routes/layout');

const app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'html');
app.engine('html', ejs.renderFile); //consider html as ejs

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());

app.use(session({ secret: 'qwer1234', resave: true, saveUninitialized: false, cookie : { maxAge : 60*60*1000 }}));
app.use(passport.initialize());
app.use(passport.session());
app.use(express.static(path.join(__dirname, 'public')));

passportConfig();

app.use('/', indexRouter);
app.use('/login', loginRouter);
app.use('/layout', layoutRouter);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
    next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
    // set locals, only providing error in development
    res.locals.message = err.message;
    res.locals.error = req.app.get('env') === 'development' ? err : {};

    // render the error page
    res.status(err.status || 500);
    res.send('Error!');
    //res.render('error');
});

app.listen(3000, ()=>{
    console.log('3000 port connected');
})

module.exports = app;