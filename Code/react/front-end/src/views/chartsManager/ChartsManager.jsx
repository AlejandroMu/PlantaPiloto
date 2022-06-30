import { Fragment, useState } from "react";
import { LineChart } from "../../components/lineChart/LineChart";

const ChartsManager = ()=>{
    const [state, setState] = useState({charts:[<LineChart key ="key"/>,<LineChart key ="key1"/>]})
    const doubleClick = (e)=>{
        console.log(e)
    }
    return (
        <div >
            <h1>Hellos</h1>
            <input type="button" value="Hello" onClick={console.log("clicks")}/>
            {state.charts.map((chart)=>{
                return chart;
            })}
        </div>
    );
}
export default ChartsManager;