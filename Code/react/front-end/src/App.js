import logo from './logo.svg';
import './App.css';
import {
  BrowserRouter,
  Routes,
  Route,
} from "react-router-dom";

import ChartsManager from './views/chartsManager/ChartsManager'
function App() {
  return (
    <div className="App">
      <BrowserRouter>
        <Routes>
          <Route path='/' element = {<ChartsManager />}></Route>
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
