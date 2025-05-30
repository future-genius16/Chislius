import {Container, Image, Nav, Navbar, NavDropdown} from 'react-bootstrap'
import React, {useState} from 'react'
import LogoutModal from '../modal/LogoutModal'
import ChangeNameModal from '../modal/ChangeNameModal'

function MenuNavbar({player}) {
    const [showLogout, setShowLogout] = useState(false)
    const [showChangeName, setShowChangeName] = useState(false)

    return (<>
        <Navbar collapseOnSelect expand="lg" className="bg-white">
            <Container>
                <Navbar.Brand>
                    <Image src={'/images/profiles/' + (player.rating * player.rating) % 12 + '.png'}
                           width="30"
                           height="30"
                           className="d-inline-block align-top" roundedCircle
                    /> {player?.name} | Рейтинг {(player.rating * player.rating) % 100}</Navbar.Brand>
                <Navbar.Toggle aria-controls="responsive-navbar-nav"/>
                <Navbar.Collapse className="justify-content-end" id="responsive-navbar-nav">
                    <Nav className="mb-auto">
                        <NavDropdown title="Настройки" id="collapsible-nav-dropdown">
                            <NavDropdown.Item onClick={() => setShowChangeName(true)}>Сменить имя</NavDropdown.Item>
                            <NavDropdown.Divider/>
                            <NavDropdown.Item onClick={() => setShowLogout(true)}>Выйти</NavDropdown.Item>
                        </NavDropdown>
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
        <LogoutModal
            show={showLogout}
            onHide={() => setShowLogout(false)}
        />
        <ChangeNameModal
            show={showChangeName}
            onHide={() => setShowChangeName(false)}
        />
    </>)
}

export default MenuNavbar