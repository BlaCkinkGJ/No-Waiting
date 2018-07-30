const Client = require('node-rest-client').Client;
const client = new Client();

const config = {
    "Authorization" : "KakaoAK 0304176aa20609c1b24fde18acf339d7"
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
            const most_approximate = data["documents"][0];
            const road_address = most_approximate["road_address"];
            const point = {
                "x" : road_address["x"],
                "y" : road_address["y"]
            };
            return callback(point);
        });
    }
}


module.exports = {
    Map : new Map(config)
};
