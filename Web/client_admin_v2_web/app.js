const createError = require('http-errors');
const express = require('express');
const path = require('path');
const bodyParser = require('body-parser');
const cookieParser = require('cookie-parser');
const logger = require('morgan');
const ejs = require('ejs');
const passport = require('passport');
const passportConfig = require('./passport');
const session = require('express-session');

const app = express();

const server = require('http').Server(app);
const io = require('socket.io')(server);

const indexRouter   = require('./routes/index');
const loginRouter   = require('./routes/login');
const layoutRouter  = require('./routes/layout')(io);

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
    res.header('Access-Control-Allow-Origin', "http://localhost:3000");
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


server.listen(3000, function(){
    console.log('listening on *:3000');
});