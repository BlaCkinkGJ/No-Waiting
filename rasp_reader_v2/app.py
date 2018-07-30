print("===== 프로그램 시작 =====")
print("===== import 시작 =====")

from picamera import PiCamera
from picamera.array import PiRGBArray

import myutils
import time
import RPi.GPIO as GPIO
import os.path
import zbar.misc
import sys

print("===== import 완료 =====")


config = {
    "apiKey": "IzaSyAqXZJETwqfRWSjngAEs6dcks7TsQ9H2Tg",
    "authDomain": "no-waiting-de057.firebaseapp.com",
    "databaseURL": "https://no-waiting-de057.firebaseio.com",
    "storageBucket": "no-waiting-de057.firebaseapp.com"
}

GPIO.setmode(GPIO.BCM)
port = {
    "ok": 27,
    "no": 17
}
GPIO.setup(port["ok"], GPIO.OUT)
GPIO.output(port["ok"], False)
GPIO.setup(port["no"], GPIO.OUT)
GPIO.output(port["no"], False)

print("===== GPIO 세팅 완료 =====")

if __name__ == "__main__":

    fname = "info.dat"

    wantNew = True

    publicID = None
    adminID = None
    listName = None

    try:
        if os.path.isfile(fname) is True:
            wantNew = str(input("새롭게 파일 만드시길 원하시나요?(yes) ")) is "yes"

        (publicID, listName, adminID) = myutils.make_new(wantNew)

        FB = myutils.init_firebase(config, publicID, listName, adminID)

        if wantNew is True:
            myutils.make_file(publicID, listName, adminID)

        width, height = 640, 480

        camera  = PiCamera()
        scanner = zbar.Scanner()

        # camera init
        camera.resolution = (width, height)
        camera.framerate  = 32

        raw = PiRGBArray(camera, size=(width, height))

        time.sleep(0.1)  # camera warm-up

        prev = None
        GPIO_Timer = {
            "prev": time.time(),
            "now": time.time()
        }
        print("===== 카메라 시작 =====")
        for frame in myutils.get_frame(camera, raw):
            # image processing
            GPIO_Timer["now"] = time.time()
            img = frame.array
            img = zbar.misc.rgb2gray(img)

            # get data from zbar
            results = scanner.scan(img)
            value = myutils.get_zbar_result(results, prev)

            find = False
            if value is not None:
                data = myutils.get_users(FB, publicID, listName).get().val()
                for index in data:
                    current = data[index]["User_ID"] == value
                    isEnter = data[index]["Enter"] == "can"
                    if current and isEnter:
                        find = True
                        target = myutils.get_users(FB, publicID, listName).child(index)
                        state = {'State': "Check", 'Enter': "cant"}
                        target.update(state)
                        break
                prev = value
                if find is True:
                    print("CAN")
                    GPIO.output(port["ok"], True)
                    GPIO.output(port["no"], False)
                else:
                    print("CANT")
                    GPIO.output(port["ok"], False)
                    GPIO.output(port["no"], True)
                GPIO_Timer["prev"] = GPIO_Timer["now"]

            if GPIO_Timer["now"] - GPIO_Timer["prev"] > 5.0:
                GPIO.output(port["ok"], False)
                GPIO.output(port["no"], False)
                GPIO_Timer["prev"] = GPIO_Timer["now"]
                prev = None

            raw.truncate(0)  # flushing
    except KeyboardInterrupt:
        print("강제 종료 발견됨.")
        sys.exit(0)
    finally:
        GPIO.cleanup()
    print("===== 카메라 종료 =====")
    print("===== 프로그램 종료 =====")
