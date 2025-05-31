import React, {useState} from 'react'
import {Container, Image, Nav, Navbar} from 'react-bootstrap'
import LoginModal from '../modal/LoginModal'
import RegisterModal from '../modal/RegisterModal'

const AuthorizationNavbar = () => {
    const [showLogin, setShowLogin] = useState(false)
    const [showRegister, setShowRegister] = useState(false)

    return (<>
        <Navbar className="bg-white">
            <Container>
                <Navbar.Brand>Числиус</Navbar.Brand>
                <Navbar.Toggle/>
                <Navbar.Collapse className="justify-content-end">
                    <Nav>
                        <Nav.Link onClick={() => setShowLogin(true)}><b>Вход</b></Nav.Link>
                        <Nav.Link onClick={() => setShowRegister(true)}><b>Регистрация</b></Nav.Link>
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
        <div className="auth_screen">
            <Container>
                <Image src={'images/main.png'} fluid/>
            </Container>
        </div>
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