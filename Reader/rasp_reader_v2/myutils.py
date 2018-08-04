import sys

import firebase


def get_frame(camera, raw_img, format="bgr", use_video_port=True):
    return camera.capture_continuous(raw_img, format, use_video_port)

def get_users(fb, pub, name):
    return  fb.firebase.database() \
              .child("Line List")  \
              .child(pub)     \
              .child(name)     \
              .child("USER LIST")

def get_zbar_result(results, prev):
    for result in results:
        temp = result.data.decode('utf-8')
        if temp != prev:
            return temp
    return None


def make_new(chk):
    if chk is False:
        fp = open("info.dat", "r")
        pub = fp.readline().strip()
        name = fp.readline().strip()
        admin = fp.readline().strip()
        fp.close()
    else:
        pub = str(input("공개 ID를 입력해주세요 : "))  # type: str
        name = str(input("리스트 이름을 입력해주세요 : "))
        admin = str(input("개인 ID를 입력해주세요 : "))
    return pub, name, admin


def make_file(pub, name, admin):
    file = open("info.dat", "w+")
    file.write(pub + '\n')
    file.write(name + '\n')
    file.write(admin + '\n')
    file.close()


def init_firebase(conf, pub, name, admin):
    base = firebase.Firebase(conf)
    if base is None:
        print("파이어베이스 초기화 실패")
        sys.exit(0)

    temp = base.getChild("Line List") \
        .child(pub) \
        .child(name) \
        .child("ADMIN_ID") \
        .get().val()
    if temp is None:
        print("리스트 찾기를 실패했습니다.")
        sys.exit(0)
    temp = str(temp)
    if temp != admin:
        print("해당하는 관리자 아이디가 없습니다.")
        sys.exit(0)
    return base
