# No Waiting (NoW)
No Waiting (NoW) is a program aimed at efficiently building lines. It is made using simple hardware, Android applications, and the Web.

## Android Application
It consists largely of `client_admin`,` client_user`, and `NoWaiting`.

### clinet_admin
This is the first version of the management application. To use it, the user must install the application corresponding to `client_user`. You can use this application to create lines and manage your data.

### clinet_user
It is a user application that accesses the data created by the administrator and receives QR codes from the database.

### NoWating
This is a new version of `client_user`. There is an additional location-based service and a more user-friendly UI.

## JSON Databases
Contains an example of configuring Firebase data.

### Reader
It recognizes the QR generated by the user, verifies it with the server data. Note that `rasp_reader` uses ** SimpleCV ** while `rasp_reader_v2` uses ** OpenCV **.

## rasp_reader
It is the earliest version and is designed for recognition through ordinary webcam.

## rasp_reader_v2
The latest version is designed to be recognized through PiCamera. We have also dealt with the GPIO and made it possible to display the state of the pass through the hardware.

## Web
It is based on **node.js + express** with improvement of `client_admin` of existing Android Application, and most front-end part consists of **ejs** file. If you want to run it, you can run it with `node app.js`.

------
Note that Please contact the repository administrator when there is a problem with the license.
