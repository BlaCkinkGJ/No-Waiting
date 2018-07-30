print("===== 프로그램 시작 =====")
print("===== import 시작 =====")
from picamera.array import PiRGBArray
from picamera import PiCamera

import RPi.GPIO as GPIO
import zbar.misc
import time
import cv2
import sys
import os.path

import firebase

print("===== import 완료 =====")


def get_frame(raw_img, format="bgr", use_video_port=True):
    return camera.capture_continuous(raw_img, format, use_video_port)


def get_zbar_result(results, prev):
    for result in results:
        temp = result.data.decode('utf-8')
        if temp != prev:
            return temp
    return None


config = {
    "apiKey": "IzaSyAqXZJETwqfRWSjngAEs6dcks7TsQ9H2Tg",
    "authDomain": "no-waiting-de057.firebaseapp.com",
    "databaseURL": "https://no-waiting-de057.firebaseio.com",
    "storageBucket": "no-waiting-de057.firebaseapp.com"
}

GPIO.setmode(GPIO.BCM)
port = {
    "ok" : 27,
    "no" : 17
}
GPIO.setup(port["ok"], GPIO.OUT)
GPIO.output(port["ok"], False)
GPIO.setup(port["no"], GPIO.OUT)
GPIO.output(port["no"], False)

print("===== GPIO 세팅 완료 =====")

if __name__ == "__main__":

    fname = "info.dat"

    wantNew = False

    publicID = None
    adminID = None
    listName = None

    try:
        if os.path.isfile(fname) is True:
            check = str(input("새롭게 파일 만드시길 원하시나요?(yes) "))
            if check != "yes":
                file = open("info.dat", "r")
                publicID = file.readline().strip()
                listName = file.readline().strip()
                adminID = file.readline().strip()
                file.close()
            else:
                wantNew = True
        else:
            wantNew = True

        if wantNew is True:
            publicID = str(input("공개 ID를 입력해주세요 : "))
            listName = str(input("리스트 이름을 입력해주세요 : "))
            adminID = str(input("개인 ID를 입력해주세요 : "))

        FB = firebase.Firebase(config)
        if FB is None:
            print("파이어베이스 초기화 실패")
            sys.exit(0)

        data = FB.getChild("Line List") \
            .child(publicID) \
            .child(listName) \
            .child("ADMIN_ID") \
            .get().val()
        if data is None:
            print("리스트 찾기를 실패했습니다.")
            sys.exit(0)
        data = str(data)
        if data != adminID:
            print("해당하는 관리자 아이디가 없습니다.")
            sys.exit(0)

        if wantNew is True:
            file = open("info.dat", "w+")
            file.write(publicID + '\n')
            file.write(listName + '\n')
            file.write(adminID + '\n')
            file.close()

        width, height = 640, 480

        camera = PiCamera()
        scanner = zbar.Scanner()

        camera.resolution = (width, height)
        camera.framerate = 24

        raw = PiRGBArray(camera, size=(width, height))

        time.sleep(0.1)  # camera warm-up

        prev = None
        GPIO_Timer ={
            "prev" : time.time(),
            "now"  : time.time()
        }
        print("===== 카메라 시작 =====")
        for frame in get_frame(raw):
            GPIO_Timer["now"] = time.time()
            img = frame.array
            img = zbar.misc.rgb2gray(img)

            results = scanner.scan(img)
            value = get_zbar_result(results, prev)

            find = False
            if value is not None:
                data = FB.getChild("Line List") \
                    .child(publicID) \
                    .child(listName) \
                    .child("USER LIST") \
                    .get().val()
                for index in data:
                    current = data[index]["User_ID"]
                    if value == current:
                        find = True
                        target = FB.getChild("Line List") \
                            .child(publicID) \
                            .child(listName) \
                            .child("USER LIST") \
                            .child(index)
                        change = {"State": "Check"}
                        target.update(change)
                prev = value
                if find is True:
                    print("GO")
                    GPIO.output(port["ok"], True)
                    GPIO.output(port["no"], False)
                else:
                    print("NO")
                    GPIO.output(port["ok"], False)
                    GPIO.output(port["no"], True)
                GPIO_Timer["prev"] = GPIO_Timer["now"]

            print(GPIO_Timer["now"]-GPIO_Timer["prev"])
            if GPIO_Timer["now"] - GPIO_Timer["prev"] > 5.0:
                GPIO.output(port["ok"], False)
                GPIO.output(port["no"], False)
                GPIO_Timer["prev"] = GPIO_Timer["now"]

            # cv2.imshow("Frame", img)  # For test

            key = cv2.waitKey(1) & 0xFF  # get key
            raw.truncate(0)  # flushing
            if key == ord("q"): break
    except KeyboardInterrupt:
        print("강제 종료 발견됨.")
        sys.exit(0)
    finally:
        GPIO.cleanup()
    print("===== 카메라 종료 =====")
    print("===== 프로그램 종료 =====")
