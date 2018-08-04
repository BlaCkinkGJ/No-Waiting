const db = require("../database");

class _Enter{
    constructor(publicID, listName){
        this._path = "Line List"+"/"+publicID+"/"+listName + "/" + "USER LIST";
        this._mydb = db.DB;
        this._root = this._mydb.getReference(this._path);
    }
    click(number){
        this._root.once('value').then((snapshot)=>{
            let people = Object.keys(snapshot.val()).length;
            let counter = 0;
            snapshot.forEach((data)=>{
                if(counter >= number) return true;
                let isEnter = data.child("Enter").val() === "can";
                let isWait  = data.child("State").val() === "wait";
                if(!isEnter && isWait) {
                    console.log("changed : " + data.key);
                    this._root.child(data.key).update({"Enter":"can"});
                    counter++;
                }
            });
        });
    }
}


module.exports = {
    Enter : _Enter
};
