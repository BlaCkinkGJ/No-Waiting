#-*- coding:utf-8 -*-
from tkinter import *

class app:
    def __init__(self, quest):
        self.root = Tk()
        self.root.title = "No Liner"

        frame = Frame(self.root)
        frame.pack()

        window_w = self.root.winfo_screenwidth() / 2
        window_h = self.root.winfo_screenheight() / 2

        self.root.geometry("+{}+{}".format(window_w, window_h))


        self.label = Label(frame, text = quest)
        self.label.pack(side = TOP)
        self.input = Entry(frame)
        self.input.pack(side=LEFT)

        self.button = Button(frame,\
                             text = "입력",\
                             command = self.exit
                            )
        self.button.pack(side=LEFT)
        self.root.mainloop()

    def exit(self):
        self.value = self.input.get()
        self.root.destroy()


