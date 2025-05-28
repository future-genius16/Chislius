import 'bootstrap/dist/css/bootstrap.min.css';
import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import {TokenProvider} from "./context/TokenContext";

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(<React.StrictMode>
    <TokenProvider>
        <App/>
    </TokenProvider>
</React.StrictMode>);