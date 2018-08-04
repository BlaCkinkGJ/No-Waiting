const Client = require('node-rest-client').Client;
const client = new Client();

const config = {
    "Authorization" : "Please input your kakao API key"
};

class Map{
    constructor(config){
        this._config = config
    }
    findAddress(address = "", callback){
        const args = {
            parameters : {"query":address},
            headers : this._config
        };
        const URL = "https://dapi.kakao.com/v2/local/search/address.json"
        client.get(URL, args, (data, res)=>{
            return callback(data)
        });
    }
    findPoint(address = "", callback){
        const args = {
            parameters : {"query" : address},
            headers : this._config
        };
        const URL = "https://dapi.kakao.com/v2/local/search/address.json"
        client.get(URL, args, (data, res)=>{
            let point = {
                "x" : 0.0,
                "y" : 0.0
            };
            if(data["documents"] !== undefined) {
                if (data["documents"].length !== 0) {
                    const most_approximate = data["documents"][0];
                    const road_address = most_approximate["road_address"];
                    point = {
                        "x": parseFloat(road_address["x"]),
                        "y": parseFloat(road_address["y"])
                    };
                }
            }
            return callback(point);
        });
    }
}

module.exports = {
    Map : new Map(config)
};

/***** USAGE *****
 let map = new Map(config);
 map.findPoint("부산광역시 남구 용호로 42번길 25-15",(data)=>{
    console.log(data);
});
 ***** USAGE *****/
