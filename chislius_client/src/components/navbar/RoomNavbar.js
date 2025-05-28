import {Container, Nav, Navbar, NavDropdown} from 'react-bootstrap'
import React, {useState} from 'react'
import LeaveRoomModal from '../room/LeaveRoomModal'

function RoomNavbar({player, data}) {
    const [showLeaveRoom, setShowLeaveRoom] = useState(false)

    return (<>
        <Navbar collapseOnSelect expand="lg" className="bg-body-secondary">
            <Container>
                <Navbar.Brand>Числиус</Navbar.Brand>
                <Navbar.Toggle aria-controls="responsive-navbar-nav"/>
                <Navbar.Collapse className="justify-content-end" id="responsive-navbar-nav">
                    <Nav className="mb-auto">
                        <Nav.Link>
                            <img alt="" src="/images/cards/5.svg" width="30" height="30"/>
                            <b>{player.name}</b>
                        </Nav.Link>
                        <Nav.Link>Рейтинг <b>{player.rating}</b></Nav.Link>
                        <NavDropdown title={'Комната: ' + data.code} id="collapsible-nav-dropdown">
                            <NavDropdown.Item onClick={() => setShowLeaveRoom(true)}>Выйти из комнаты</NavDropdown.Item>
                        </NavDropdown>
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
        <LeaveRoomModal show={showLeaveRoom} onHide={() => setShowLeaveRoom(false)}
        />
    </>)
}

export default RoomNavbar