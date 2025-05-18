import React, {useState} from 'react';
import {Button} from 'react-bootstrap';
import LoginModal from "../auth/LoginModal";
import RegisterModal from "../auth/RegisterModal";
import {useAuth} from "../../context/TokenContext";

const AuthorizationScreen = () => {
    const {login} = useAuth();
    const [showLogin, setShowLogin] = useState(false);
    const [showRegister, setShowRegister] = useState(false);

    const handleAuthSuccess = (token) => {
        login(token)
        console.log('Authentication success, token:', token);
    };

    return (<div>
        <h1>Chislius</h1>
        <div>
            <Button onClick={() => setShowLogin(true)}>Login</Button>
            <Button onClick={() => setShowRegister(true)}>Register</Button>
        </div>
        <LoginModal
            show={showLogin}
            onHide={() => setShowLogin(false)}
            onSuccess={handleAuthSuccess}
        />
        <RegisterModal
            show={showRegister}
            onHide={() => setShowRegister(false)}
            onSuccess={handleAuthSuccess}
        />
    </div>);
};

export default AuthorizationScreen;