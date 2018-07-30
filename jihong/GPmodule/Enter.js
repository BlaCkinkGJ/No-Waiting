const db = require("../database");

class Enter{
    constructor(publicID, listName){
        this._path = "Line List"+"/"+publicID+"/"+listName + "/" + "USER LIST";
        this._mydb = db.DB
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

// test module
let value = new Enter("유가네 닭갈비 (부산대 점)", "10시 줄");
value.click(1);
setTimeout(()=>{value.click(1)}, 1500);


module.exports = {
    Enter : Enter
};
