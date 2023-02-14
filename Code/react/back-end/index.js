const express = require('express');
const cors = require('cors');
const { getValuesByTagName, connect } = require("./script_postgres");
connect();

const app = express();
app.use(cors());
const port = 3000;
app.get('/', (req, res) => {
    res.send('Hello World, from express');
    
});

app.get('/getValues',(req,res)=>{
    let names = req.query.tags;
    let count = req.query.count;
    console.log("names " +names)
    // getValuesByTagName(name,count).then((data) => {
    //     console.log(data);
    //     res.json(data);
    // });

});

app.listen(port, () => console.log(`Hello world app listening on port ${port}!`))
