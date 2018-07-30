import pyrebase # REST API

class Firebase:
    def __init__(self, config_dict):
        self.config   = config_dict
        self.firebase = pyrebase.initialize_app(self.config)
        self.db       = self.firebase.database()

    def getChild(self, child_name):
        return self.db.child(child_name)
