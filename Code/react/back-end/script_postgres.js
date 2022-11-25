const { Client } = require('pg');

const client = new Client({
    user: 'postgres',
    host: 'localhost',
    database: 'piloto',
    password: 'postgres',
    port: 5432,
});
module.exports.connect = function(){
    client.connect();
    
}
module.exports.getValuesByTagName = async function(tag, firstN){

    const query = `SELECT timestamp,value
    FROM value va,channel ch  
    WHERE  ch.name=$1 AND ch.id=va.channel_id ORDER BY timestamp 
    DESC LIMIT $2`;

    return client.query(query,[tag,firstN])
        .then((res)=>{
            return res.rows
        })
        .catch((err)=>{
            return err
        });

};



