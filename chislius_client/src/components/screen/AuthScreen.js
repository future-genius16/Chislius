import React, {useState} from 'react'
import {Container, Nav, Navbar} from 'react-bootstrap'
import LoginModal from '../user/LoginModal'
import RegisterModal from '../user/RegisterModal'

const AuthorizationNavbar = () => {
    const [showLogin, setShowLogin] = useState(false)
    const [showRegister, setShowRegister] = useState(false)

    return (<>
        <Navbar className="bg-body-secondary">
            <Container>
                <Navbar.Brand>Числиус</Navbar.Brand>
                <Navbar.Toggle/>
                <Navbar.Collapse className="justify-content-end">
                    <Nav>
                        <Nav.Link onClick={() => setShowLogin(true)}>Вход</Nav.Link>
                        <Nav.Link onClick={() => setShowRegister(true)}>Регистрация</Nav.Link>
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
        <LoginModal
            show={showLogin}
            onHide={() => setShowLogin(false)}
        />
        <RegisterModal
            show={showRegister}
            onHide={() => setShowRegister(false)}
        />
    </>)
}

export default AuthorizationNavbar