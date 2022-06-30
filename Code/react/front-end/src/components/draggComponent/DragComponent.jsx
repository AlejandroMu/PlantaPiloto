import { useState } from "react";

export function DragComponent(props) {
    const [state,setState] = useState({x:0,y:0})
    let initPos ={}
    const location = ({x,y})=>{
        setState((prev)=>{
            return {
                x:prev.x + x,
                y:prev.y + y
            };
        })
    }
    return (
        <div {...props}
        
            draggable 
            onDragStart ={(e)=>{
                initPos ={
                    x:e.clientX,
                    y:e.clientY
                }
            }}
            onDragEnd ={(e)=>{
                location({
                    x:e.clientX - initPos.x,
                    y:e.clientY - initPos.y
                });
            }}
            style={{position: 'absolute',left:state.x,top:state.y}}
        
        >
            {props.children}
        </div>
    );    
}