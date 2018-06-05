#-*- coding:utf-8 -*-

print "========== import start ==========="
import firebase
print "firebase importing complete...(1/5)"
import App 
print "tkinter importing complete....(2/5)"
import sys
print "sys importing complete...,,,,,(3/5)"
import camera
print "camera importing complete...,,(4/5)"
import time
print "time importing complete...,,,,(5/5)"
print "========== import finish =========="

def question(quest):
    result = App.app(quest)
    try:
        ret = result.value
    except AttributeError:
        print "                              "
        print "                              "
        print "                              "
        print "=== HALT SEQUENCE DETECTED ==="
        print "                              "
        print "  Shutdown the program. Bye~  "
        print "                              "
        print "=== HALT SEQUENCE DETECTED ==="
        print "                              "
        print "                              "
        print "                              "
        sys.exit(0)
    return ret

print "========== start config ==========="
config = {
    "apiKey" : "IzaSyAqXZJETwqfRWSjngAEs6dcks7TsQ9H2Tg",
    "authDomain" : "no-waiting-de057.firebaseapp.com",
    "databaseURL" : "https://no-waiting-de057.firebaseio.com",
    "storageBucket" : "no-waiting-de057.firebaseapp.com"
}

FB = firebase.Firebase(config)
if FB == None:
    print "Cannot access the firebase system"
    print "Please check out your firebase setting!!"
    sys.exit(0)
publicID = str(question("공개 ID 를 입력해주세요").encode('utf-8')) #"유가네 갈비"
listName = str(question("리스트 이름를 입력해주세요").encode('utf-8')) #"list03"
publicID = "sample"
listName = "line2"
getData = FB.getChild("Line List") \
            .child(publicID)\
            .child(listName)\
            .child("ADMIN_ID")\
            .get().val()
if getData == None:
    print "Cannot load data from database"
    sys.exit(0)

AdminID = str(question("개인 ID를 입력해주세요"))
getData = str(getData)

if getData != AdminID:
    print "invalid ID"
    sys.exit(0)

print "========= start camera ============"
QR = camera.QRScan()

print "QR start"
while True:
    try:
        time.sleep(0.1)
        QR.image_from_camera()
        value = QR.scanning() # scan from the user QR
        if(value):
            list_data = FB.getChild("Line List") \
                          .child(publicID)\
                          .child(listName)\
                          .child("USER LIST")\
                          .get().val()
            find = False
            for index in list_data:
                current = list_data[index]["User_ID"]
                if value == current:
                    find = True
                    get = FB.getChild("Line List") \
                      .child(publicID)\
                      .child(listName)\
                      .child("USER LIST")\
                      .child(index.encode("utf-8"))
                    data = {"State" :"Check" }
                    get.update(data)
                    break;
            if find == True:
                print "You can go!"
            else:
                print "Invalid Person"
    except KeyboardInterrupt:
        print "                              "
        print "                              "
        print "                              "
        print "=== HALT SEQUENCE DETECTED ==="
        print "                              "
        print "  Shutdown the program. Bye~  "
        print "                              "
        print "=== HALT SEQUENCE DETECTED ==="
        print "                              "
        print "                              "
        print "                              "
        sys.exit(0)



print "=========== end camera ============"
print "====== shutdown the program ======="
